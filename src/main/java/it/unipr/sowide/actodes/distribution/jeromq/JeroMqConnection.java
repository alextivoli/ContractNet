package it.unipr.sowide.actodes.distribution.jeromq;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.distribution.Connection;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code JeroMqConnection} class supports the communication
 * towards a remote actor space taking advantage of JeroMq technology.
 *
**/

public final class JeroMqConnection implements Connection
{
  // Destination service provider reference.
  private final Reference destination;
  private final ZMQ.Socket sender;
  private final String url;

  /**
   * Class constructor.
   *
   * @param r
   *
   * the actor space service provider reference of the destination actor space.
   *
   * @param s  the socket.
   *
  **/
  public JeroMqConnection(final Reference r, final ZMQ.Socket s)
  {
    this.destination = r;
    this.sender      = s;

    this.url = "tcp://localhost:" + this.destination.getLocation().substring(
        0, this.destination.getLocation().indexOf("@"));

    this.sender.connect(this.url);
  }

  /** {@inheritDoc} **/
  @Override
  public Reference getDestination()
  {
    return this.destination;
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized boolean forward(final Message m)
  {
    try
    {
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      ObjectOutputStream o    = new ObjectOutputStream(b);

      o.writeObject(m);
      o.flush();
      o.close();
      b.close();
      this.sender.send(b.toByteArray());

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }

  /**
   * Closes the connection.
   *
   * @param c  the context.
   *
  **/
  protected void close(final ZContext c)
  {
    try
    {
      this.sender.disconnect(this.url);
      c.destroySocket(this.sender);
    }
    catch (Exception e)
    {
      return;
    }
  }
}
