package it.unipr.sowide.actodes.distribution.activemq;

import java.util.HashSet;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

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
 * The {@code ActiveMqConnector} class manages the communication towards
 * remote actor spaces taking advantage of the JMS ActiveMQ implementation.
 *
**/

public final class ActiveMqConnector extends Connector implements Runnable
{
  private static final long serialVersionUID = 1L;

  private static final String URL    = "tcp://localhost:61616";
  private static final String PARAMS = "persistent=false&useJmx=false";

  private static final String QUEUE = "registration";

  //JMS provider.
  private BrokerService provider;
  // JMS connection.
  private QueueConnection connection;
  // JMS queue receiver.
  private QueueReceiver receiver;
  // JMS queue session.
  private QueueSession session;
  // JMS queue for incoming message.
  private Queue queue;
  // JMS queue session.
  private QueueSession discover;
  // JMS queue for incoming message.
  private Queue registration;
  // Connector thread.
  private Thread thread;

  /**
   * Class constructor.
   *
   * This connector uses the default configuration
   * and does not act as communication broker.
   *
  **/
  public ActiveMqConnector()
  {
    this.provider = null;

    configure(URL, PARAMS);
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
  public ActiveMqConnector(final boolean f)
  {
    this.provider = null;

    setBroker(f);

    configure(URL, PARAMS);
  }

  /**
   * Class constructor.
   *
   * @param u  the URL of the JMS broker.
   *
   * This connector does not act as communication broker.
   *
   */
  public ActiveMqConnector(final String u)
  {
    this.provider = null;

    configure(u, null);
  }

  /**
   * Class constructor.
   *
   * @param u  the URL of an embedded JMS broker.
   * @param i  the initialization parameters of the JMS embedded broker.
   *
   * This connector act as communication broker.
   *
   */
  public ActiveMqConnector(final String u, final String i)
  {
    this.provider = null;

    setBroker(true);

    configure(u, i);
  }

  private void configure(final String u, final String i)
  {
    QueueConnectionFactory cf = null;

    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

    try
    {
      if (isBroker())
      {
        // create an embedded broker
        this.provider = BrokerFactory.createBroker(
                "broker:(" + u + ")?" + i);

        this.provider.start();

        cf = new ActiveMQConnectionFactory(u);
      }
      else
      {
        cf = new ActiveMQConnectionFactory(u);
      }

      this.connection = (QueueConnection) cf.createConnection();

      this.connection.start();
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
        this.discover = this.connection.createQueueSession(
            false, Session.AUTO_ACKNOWLEDGE);

        this.registration = this.discover.createQueue(QUEUE);

        MessageConsumer consumer =
            this.discover.createConsumer(this.registration);

        consumer.setMessageListener(new MessageListener()
        {
          @Override
          public void onMessage(final javax.jms.Message m)
          {
            try
            {
              HashSet<String> s = new HashSet<>();

              s.add(Behavior.PROVIDER.toString());

              Set<Reference> providers =
                  Controller.CONTROLLER.getDispatcher().providers();

              for (Reference r : providers)
              {
                s.add(r.toString());
              }

              ObjectMessage objMessage =
                  ActiveMqConnector.this.discover.createObjectMessage();

              objMessage.setObject(s);
              ActiveMqConnector.this.discover.createProducer(null).send(
                  m.getJMSReplyTo(), objMessage);
            }
            catch (JMSException e)
            {
              ErrorManager.notify(e);

              return;
            }
          }
        });
      }

      this.session = this.connection.createQueueSession(
          false, Session.AUTO_ACKNOWLEDGE);

      this.queue    = this.session.createQueue(Behavior.PROVIDER.toString());
      this.receiver = this.session.createReceiver(this.queue);
      this.thread   = new Thread(this);
    }
    catch (Exception e)
    {
      ErrorManager.kill(e);
    }

    this.thread.start();

    super.start();
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
        Destination tempDest = this.session.createTemporaryQueue();
        MessageConsumer responseConsumer =
            this.session.createConsumer(tempDest);

        TextMessage m = this.session.createTextMessage();

        m.setJMSReplyTo(tempDest);
        this.session.createSender(this.session.createQueue(QUEUE)).send(m);
        ObjectMessage r = (ObjectMessage) responseConsumer.receive();

        for (String n : (Set<String>) r.getObject())
        {
          if (!p.equals(n))
          {
            s.add(new SpecialReference(n));
          }
        }
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
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
      return new JmsConnection(
          d, this.session,
          this.session.createSender(
          this.session.createQueue(d.toString())));
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return null;
    }
  }


  /** {@inheritDoc} **/
  @Override
  protected void destroy(final Reference d)
  {
    try
    {
      this.provider.getAdminView().removeQueue(d.toString());
    }
    catch (Exception e)
    {
      return;
    }
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
    while (true)
    {
      Message m;

      try
      {
        m = (Message) ((ObjectMessage) this.receiver.receive()).getObject();
      }
      catch (Exception e)
      {
        return;
      }

      manage(m);
    }
  }

  /** {@inheritDoc} **/
  @Override
  protected void destroy()
  {
    try
    {
      this.session.close();

      this.connection.close();

      if (this.provider != null)
      {
        this.provider.stop();
      }
    }
    catch (Exception e)
    {
      return;
    }
  }
}
