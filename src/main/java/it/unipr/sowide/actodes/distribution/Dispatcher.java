package it.unipr.sowide.actodes.distribution;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpecialReference;
import it.unipr.sowide.actodes.distribution.content.Connected;
import it.unipr.sowide.actodes.distribution.content.Container;
import it.unipr.sowide.actodes.distribution.content.Disconnected;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.registry.Registry;
import it.unipr.sowide.actodes.service.group.MulticastReference;

/**
 *
 * The {@code Dispatcher} class provides a partial implementation of a message
 * dispatcher.
 *
 * A dispatcher manages the exchange of messages with the other actor spaces
 * of the application.
 *
**/

public abstract class Dispatcher implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Connector.
  private transient Connector connector;
  // Actor spaces local actor executor references.
  private final CopyOnWriteArraySet<Reference> executors;
  // Actor spaces local broadcast references.
  private final CopyOnWriteArraySet<Reference> lbroadcasts;
  // Broker actor space reference.
  private Reference broker;

  /**
   * Registry instance.
   *
  **/
  protected Registry registry;

  /**
   * Actor space location - connection map.
   *
  **/
  protected final ConcurrentMap<String, Connection> connections;

  /**
   * Class constructor.
   *
  **/
  public Dispatcher()
  {
    this.connector   = null;
    this.broker      = null;
    this.connections = new ConcurrentHashMap<>();
    this.executors  = new CopyOnWriteArraySet<>();
    this.lbroadcasts = new CopyOnWriteArraySet<>();
  }

  /**
   * Gets the references of the services providers
   * of the connected actor spaces.
   *
   * @return the references.
   *
  **/
  public Set<Reference> providers()
  {
    if (this.connector != null)
    {
      return this.connector.providers();
    }
    else
    {
      return new HashSet<Reference>();
    }
  }

  /**
   * Gets the actor executor references of the connected actor spaces.
   *
   * @return the references.
   *
  **/
  public Set<Reference> executors()
  {
    return this.executors;
  }

  /**
   * Gets the local broadcast references of the connected actor spaces.
   *
   * @return the references.
   *
  **/
  public Set<Reference> lbroadcasts()
  {
    return this.lbroadcasts;
  }

  /**
   * Gets the connector.
   *
   * @return the connector.
   *
  **/
  public Connector getConnector()
  {
    return this.connector;
  }

  /**
   * Gets the reference of the service provider of the actor space
   * acting as communication broker.
   *
   * @return the reference.
   *
  **/
  public Reference getBroker()
  {
    return this.broker;
  }

  /**
   * Sets the references of the actor space acting as communication
   * broker of the connected actor spaces.
   *
   * @param r  the reference.
   *
  **/
  public void setBroker(final Reference r)
  {
    this.broker = r;
  }

  /**
   * Adds a connection towards a remote actor space.
   *
   * @param d  the remote actor space service provider reference.
   *
  **/
  public void add(final Reference d)
  {
    Connection c = this.connector.get(d);

    if (c == null)
    {
      this.connector.add(d);

      c = this.connector.get(d);

      if (c != null)
      {
        this.connections.put(d.getLocation(), c);
        this.executors.add(
            new SpecialReference(Reference.address(
                Behavior.EXECUTOR.getName(), d.getLocation())));

        this.lbroadcasts.add(
            new SpecialReference(Reference.address(
                Behavior.SPACE.getName(), d.getLocation())));

        boolean mf = this.connector.isBroker();

        Connected n = new Connected(Behavior.PROVIDER, d, mf);

        c.forward(new Message(
            0, Behavior.PROVIDER, List.of(d), n, System.currentTimeMillis(),
            Type.ONEWAY, Message.NOINREPLYTO));
      }
    }
  }

  /**
   * Deletes a connection.
   *
   * @param d the remote actor space service provider reference.
   *
  **/
  public void delete(final Reference d)
  {
    Connection c = this.connections.get(d.getLocation());

    if (!d.equals(Behavior.PROVIDER) && (c != null))
    {
      this.connections.remove(d.getLocation());
      this.executors.remove(
          new ActorReference(Reference.address(
              Behavior.EXECUTOR.getName(), d.getLocation()), null));
      this.lbroadcasts.remove(
          new ActorReference(Reference.address(
              Behavior.SPACE.getName(), d.getLocation()), null));

      Disconnected n = new Disconnected(Behavior.PROVIDER, d);

      if (c != null)
      {
        c.forward(new Message(
          0, Behavior.PROVIDER, List.of(d), n, System.currentTimeMillis(),
          Type.ONEWAY, Message.NOINREPLYTO));
      }

      this.connector.remove(d);
    }
  }

  /**
   * Forwards a message to a remote destination.
   *
   * @param m  the message.
   *
   * Note that if the actor associate with the destination reference
   * does not exist, then the possible reply is an error message.
   *
   * @see it.unipr.sowide.actodes.interaction.Error#UNREACHABLEACTOR
   *
  **/
  public abstract void deliver(Message m);

  /**
   * Receives a message from a remote destination.
   *
   * @param m  the message.
   *
  **/
  public void receive(final Message m)
  {
    Reference r = m.getReceiver();

    if (r.equals(Behavior.PROVIDER) || (r instanceof MulticastReference))
    {
      Behavior.PROVIDER.push(m);
    }
    else if (r.equals(Behavior.EXECUTOR))
    {
      if (m.getContent() instanceof Container)
      {
        for (Message e : ((Container) m.getContent()).getMessages())
        {
          Reference d = e.getReceiver();
          Actor a     = this.registry.get(d);

          if (a != null)
          {
            a.getReference().push(e);
          }
          else if (!d.toString().equals(Behavior.PROVIDER.getLocation()))
          {
            deliver(e);
          }
          else if (d.equals(Behavior.EXECUTOR))
          {
            Behavior.EXECUTOR.push(e);
          }
          else if (e.getType() == Type.TWOWAY)
          {
            deliver(new Message(
                e.getIdentifier(), Behavior.PROVIDER, List.of(e.getSender()),
                Error.UNREACHABLEACTOR, e.getTime(),
                Type.ONEWAY, e.getIdentifier()));
          }
        }
      }
      else
      {
        Behavior.EXECUTOR.push(m);
      }
    }
    else if (r.equals(Behavior.SPACE))
    {
      Behavior.SPACE.push(m);
    }
    else
    {
      if (r.getLocation().equals(Behavior.PROVIDER.getLocation()))
      {
        Actor a = this.registry.get(r);

        if (a != null)
        {
          a.getReference().push(m);

          return;
        }

        if (m.getType() == Type.TWOWAY)
        {
          deliver(new Message(
              m.getIdentifier(), Behavior.PROVIDER, List.of(m.getSender()),
              Error.UNREACHABLEACTOR, m.getTime(),
              Type.ONEWAY, m.getIdentifier()));
        }
      }
      else
      {
        deliver(m);
      }
    }
  }

  /**
   * Starts the dispatcher.
   *
  **/
  public void start()
  {
    this.registry  = Controller.CONTROLLER.getRegistry();
    this.connector = Controller.CONTROLLER.getConnector();

    if (this.connector != null)
    {
      this.connector.start();

      if (this.connector.isBroker())
      {
        this.broker = Behavior.PROVIDER;
      }
    }
    else
    {
      this.broker = Behavior.PROVIDER;
    }
  }

  /**
   * Closes all the connections to the remote actor spaces.
   *
  **/
  public void shutdown()
  {
    if (this.connector != null)
    {
      this.connector.shutdown();

      this.connector = null;

      Controller.CONTROLLER.shutdown();
    }
  }
}
