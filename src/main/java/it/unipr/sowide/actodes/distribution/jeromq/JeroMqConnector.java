package it.unipr.sowide.actodes.distribution.jeromq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpecialReference;
import it.unipr.sowide.actodes.distribution.Connection;
import it.unipr.sowide.actodes.distribution.Connector;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code JeroMqConnector} class manages the communication towards
 * remote actor spaces taking advantage of the JeroMQ implementation.
 *
**/

public final class JeroMqConnector extends Connector implements Runnable
{
  private static final long serialVersionUID = 1L;

  private static final String URL = "tcp://localhost:6159";

  private ZContext context;
  private Broker broker;

  private ZMQ.Socket dealer;
  private ZMQ.Socket queue;

  // Connector thread.
  private Thread thread;

  /**
   * Class constructor.
   *
   * This connector uses the default configuration
   * and does not act as communication broker.
   *
  **/
  protected JeroMqConnector()
  {
    this.broker = null;

    configure(URL);
  }

  /**
   * Class constructor.
   *
   * @param f  a boolean flag indicating if the connector act as communication
   * broker (value = <code>true</code>) or not (value = <code>false</code>).
   *
   * This connector uses the default configuration.
   *
   */
  public JeroMqConnector(final boolean f)
  {
    this.broker = null;

    setBroker(f);

    configure(URL);
  }

  /**
   * Class constructor.
   *
   * @param f  a boolean flag indicating if the connector act as communication
   * broker (value = <code>true</code>) or not (value = <code>false</code>).
   *
   * @param u  the URL of the JeroMQ broker.
   *
   */
  public JeroMqConnector(final boolean f, final String u)
  {
    this.broker = null;

    setBroker(f);

    configure(u);
  }

  /**
   * Configures the JeroMQ connector.
   *
   * @param v  the arguments.
   *
   * It can be either configured without arguments (i.e., the default
   * configuration) or with an argument:
   *
   * a string representing the URL of the embedded JeroMQ broker.
   *
   * In the case of default configuration the value of the URL of the embedded
   * broker is <tt>tcp://localhost:61612</tt>.
   *
   * @throws ConfigurationException if the configuration failed.
   *
  **/
  private void configure(final String u)
  {
    try
    {
      String url = u;

      this.context = new ZContext();

      if (isBroker())
      {
        // create an embedded broker
        this.broker = new Broker(url);
      }

      this.dealer = this.context.createSocket(SocketType.REQ);

      this.dealer.connect(url);

      this.queue = this.context.createSocket(SocketType.DEALER);
    }
    catch (Exception e)
    {
      ErrorManager.kill(e);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
    try
    {
      if (isBroker())
      {
        this.broker.start();
      }

      this.thread = new Thread(this);

      String p = Behavior.PROVIDER.getLocation().substring(
           0, Behavior.PROVIDER.getLocation().indexOf("@"));

      this.queue.bind("tcp://localhost:" + p);

      this.thread.start();

      super.start();
    }
    catch (Exception e)
    {
      ErrorManager.kill(e);
    }
  }

  private final class Broker implements Runnable
  {
    private Thread thread;
    private final ZContext context;
    private final ZMQ.Socket socket;

    private Broker(final String u)
    {
      // create an embedded broker
      this.context = new ZContext(2);
      this.socket  = this.context.createSocket(SocketType.REP);

      this.socket.bind(u);
    }

    @Override
    public void run()
    {
      try
      {
        while (!this.thread.isInterrupted())
        {
          this.socket.recv();

          HashSet<String> s = new HashSet<>();

          s.add(Behavior.PROVIDER.toString());

          Set<Reference> providers =
              Controller.CONTROLLER.getDispatcher().providers();

          for (Reference r : providers)
          {
            s.add(r.toString());
          }

          ByteArrayOutputStream b = new ByteArrayOutputStream();
          ObjectOutputStream o    = new ObjectOutputStream(b);

          o.writeObject(s);
          o.flush();
          o.close();
          b.close();

          this.socket.send(b.toByteArray());
        }

        this.context.destroySocket(this.socket);
        this.context.destroy();
      }
      catch (Exception e)
      {
        return;
      }
    }

    public void start()
    {
      this.thread = new Thread(this);

      this.thread.start();
    }

    public void destroy()
    {
      try
      {
        this.thread.interrupt();
      }
      catch (Exception e)
      {
        return;
      }
    }
 }

  /** {@inheritDoc} **/
  @Override
  @SuppressWarnings("unchecked")
  protected Set<Reference> discover()
  {
    HashSet<Reference> s = new HashSet<>();

    if (!isBroker())
    {
      String p = Behavior.PROVIDER.toString();

      try
      {
        this.dealer.send(" ");

        ObjectInputStream o = new ObjectInputStream(
            new ByteArrayInputStream(this.dealer.recv()));

        Object m = o.readObject();

        o.close();

        Set<String> str = (Set<String>) m;

        for (String n : str)
        {
          if (!p.equals(n))
          {
            s.add(new SpecialReference(n));
          }
        }
      }
      catch (Exception e)
      {
        ErrorManager.kill(e);
      }
    }

    return s;
  }

  /** {@inheritDoc} **/
  @Override
  protected Connection create(final Reference d)
  {
    try
    {
      return new JeroMqConnection(d,
          this.context.createSocket(SocketType.DEALER));
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    return null;
  }

  /** {@inheritDoc} **/
  @Override
  protected void destroy(final Reference d)
  {
    JeroMqConnection connection = (JeroMqConnection) get(d);

    if (connection == null)
    {
      return;
    }

    connection.close(this.context);
  }

  /**
   *
   * {@inheritDoc}
   *
   * Manages the received message.
   *
  **/
  @Override
  public void run()
  {
    try
    {
      while (!this.thread.isInterrupted())
      {
        ObjectInputStream o =
            new ObjectInputStream(new ByteArrayInputStream(this.queue.recv()));

        Object m = o.readObject();

        o.close();
        manage((Message) m);
      }

      this.context.destroySocket(this.dealer);
      this.context.destroySocket(this.queue);
      this.context.destroy();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return;
    }
  }

  /** {@inheritDoc} **/
  @Override
  protected void destroy()
  {
    try
    {
      if (this.broker != null)
      {
        this.broker.destroy();
      }

      this.thread.interrupt();
    }
    catch (Exception e)
    {
      return;
    }
  }
}
