package it.unipr.sowide.actodes.distribution;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Connector} class provides a partial implementation of the
 * manager of the connections towards some remote actor spaces.
 *
**/

public abstract class Connector implements Serializable
{
  private static final long serialVersionUID = 1L;

  private static final long DELAY = 1000;

  // Remote reference space - connection map.
  private final ConcurrentMap<Reference, Connection> connections;
  // Broker role flag.
  private boolean broker;

  /**
   * Class constructor.
   *
   */
  protected Connector()
  {
    this.connections = new ConcurrentHashMap<>();

    SpaceInfo.INFO.getConfiguration().addService(new Networker());

    this.broker = false;
  }

  /**
   * Starts the connector.
   *
  **/
  public void start()
  {
    for (Reference a : discover())
    {
      if (!a.equals(Behavior.PROVIDER))
      {
        Controller.CONTROLLER.getDispatcher().add(a);
      }
    }
  }

  /**
   * Shutdowns the connector.
   *
  **/
  public final void shutdown()
  {
    for (Reference i : this.connections.keySet())
    {
      remove(i);
    }

    try
    {
      Thread.sleep(DELAY);
    }
    catch (InterruptedException e)
    {
      ErrorManager.notify(e);
    }

    destroy();
  }

  /**
   * Gets the connection towards a remote actor space.
   *
   * @param d  the remote actor space service provider reference.
   *
   * @return the connection.
   *
  **/
  public final Connection get(final Reference d)
  {
    return this.connections.get(d);
  }

  /**
   * Gets the references of the actor space services providers that
   * identify the connected actor spaces.
   *
   * @return the references.
   *
  **/
  public final Set<Reference> providers()
  {
    return this.connections.keySet();
  }

  /**
   * Manages a received message.
   *
   * @param m  the message.
   *
  **/
  public final void manage(final Message m)
  {
    Controller.CONTROLLER.getDispatcher().receive(m);
  }

  /**
   * Adds a connection.
   *
   * @param d  the remote actor space service provider reference.
   *
  **/
  public void add(final Reference d)
  {
    if (this.connections.get(d) == null)
    {
      Connection c = create(d);

      if (c != null)
      {
        this.connections.put(d, c);
      }
    }
  }

  /**
   * Deletes a connection.
   *
   * @param d  the remote actor space service provider reference.
   *
  **/
  public void remove(final Reference d)
  {
    destroy(d);
    this.connections.remove(d);
  }

  /**
   * Checks if the connector acts as communication broker.
   *
   * A broker connector starts both the process for connecting the actor
   * spaces and the process for closing all the connections between them.
   *
   * @return <code>true</code> if the connector acts as the broker.
   *
  **/
  public final boolean isBroker()
  {
    return this.broker;
  }

  /**
   * Sets whether or not this connector acts as broker.
   *
   * A broker connector starts both the process for connecting the actor
   * spaces and the process for closing all the connections between them.
   *
   * @param f  if <code>true</code>, then the connector acts as broker.
   *
  **/
  public final void setBroker(final boolean f)
  {
    this.broker = f;
  }

  /**
   * Discovers the actor space service provider references
   * of the connected actor spaces.
   *
   * @return the references.
   *
  **/
  protected abstract Set<Reference> discover();

  /**
   * Creates a connection for a specific communication technology.
   *
   * @param d  the remote actor space service provider reference.
   *
   * @return the new connection.
   *
  **/
  protected abstract Connection create(Reference d);

  /**
   * Destroys a connection for a specific communication technology.
   *
   * @param d  the remote actor space service provider reference.
   *
  **/
  protected abstract void destroy(Reference d);

  /**
   * Performs communication technology dependent shutdown operations.
   *
  **/
  protected abstract void destroy();
}
