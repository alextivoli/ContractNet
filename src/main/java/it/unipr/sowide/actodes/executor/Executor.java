package it.unipr.sowide.actodes.executor;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.registry.Registry;

/**
 *
 * The {@code Executor} class provides a partial implementation of the behaviors
 * used by the executor actor for managing the execution of the other actors
 * (excluding the other special actor, i.e., the service provider)
 * of the actor space.
 *
 * Its execution is terminated when:
 *
 * i) the executor actor receives a kill message from the actor provider;
 *
 * ii) the executor actor has no more actors to manage and there is not
 * an external entity (i.e., a provider of another actor space) that
 * can create new actors;
 *
 * iii) an optional termination condition becomes true.
 *
 * Note that the executor can receive stop and start requests
 * from the service provider. However, default implementations
 * are not able to manage such types of request.
 *
 * However, it can be simply done by managing the request from some message
 * pattern - handler pairs that take advantage of the {@code stopAll} and
 * {@code startAll} methods.
 *
 * @see stopAll
 * @see startAll
 *
**/

public abstract class Executor<A extends Actor> extends Behavior
{
  private static final long serialVersionUID = 1L;

  private final Behavior behavior;
  private final Builder builder;

  /**
   * A handler that processes a kill message performing the shutdown
   * of the actor when the sender is the "provider" actor.
   *
   * Notes that it does not send a reply to the kill request message.
   *
  **/
  protected static final MessageHandler KILLER = (m) -> {
    if (m.getSender().equals(Behavior.PROVIDER))
    {
      return Shutdown.SHUTDOWN;
    }

    return null;
  };

  /**
   * Class constructor.
   *
  **/
  protected Executor()
  {
    this.behavior = null;
    this.builder  = null;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  protected Executor(final Behavior b)
  {
    this.behavior = b;
    this.builder  = null;
  }

  /**
   * This method creates the initial set of actors.
   *
  **/
  protected void build()
  {
    if (Controller.CONTROLLER.getRegistry().actors().isEmpty())
    {
      if (this.behavior != null)
      {
        actor(this.behavior);
      }
      else if (this.builder != null)
      {
        this.builder.build(this);
      }
    }
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actors of the application.
   *
  **/
  protected Executor(final Builder b)
  {
    this.behavior = null;
    this.builder  = b;
  }

  /**
   * Creates the registry managing the references of the
   * actors of the actor space.
   *
   * @return the registry.
   *
   * Note that when the registry is not already created, it creates it
   * by calling the {@code registry} method.
   *
  **/
  public Registry getRegistry()
  {
    if (Controller.CONTROLLER.getRegistry() == null)
    {
      return registry();
    }

    return Controller.CONTROLLER.getRegistry();
  }

  /**
   * Creates the registry managing the references of the
   * actors of the actor space.
   *
   * Note the it returns an instance of the {Registry} class.
   * Therefore, the executor actors that need a different type
   * of registry must overridden this method.
   *
   * @return the registry.
   *
   * @see it.unipr.sowide.actodes.registry.Registry
   *
  **/
  protected Registry registry()
  {
    return new Registry();
  }

  /**
   * Creates a new actor.
   *
   * @param c  the creator reference.
   * @param b  the actor behavior.
   *
   * @return
   *
   * the new actor reference or <code>null</code>
   * if the creation of the actor failed.
   *
  **/
  public final Reference actor(final Reference c, final Behavior b)
  {
    try
    {
      Actor a = newActor(b);

      Reference r = Controller.CONTROLLER.getRegistry().add(a);

      if (r != null)
      {
        a.configure(c, r, b);
        b.configure(a);
        add(a);

        return r;
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    return null;
  }

  /**
   * Creates an actor.
   *
   * @param b  the actor behavior.
   *
   * @return the actor.
   *
   * @throws Exception if the creation of the actor fails.
   *
  **/
  public abstract A newActor(Behavior b) throws Exception;

  /**
   * Adds an actor.
   *
   * @param a  the actor.
   *
  **/
  public abstract void add(Actor a);

  /**
   * Removes an actor.
   *
   * @param a  the actor.
   *
  **/
  public abstract void remove(Actor a);

  /**
   * Start the management of actors.
   *
  **/
  public abstract void manage();

  /**
   * Checks if there are no actors to be managed.
   *
   * @return <code>true</code> if here are no actors to be managed.
   *
  **/
  public abstract boolean isEmpty();

  /**
   * Gets the current execution time.
   *
   * @return the time.
   *
  **/
  public abstract long time();

  /**
   * Stops all the managed actors.
   *
  **/
  protected abstract void stopAll();

  /**
   * Checks if the execution is stopped.
   *
   * @return <code>true</code> if the executor actor is stopped.
   *
  **/
  public boolean isStopped()
  {
    return false;
  }

  /**
   * Starts all the managed actors.
   *
  **/
  protected abstract void startAll();

  /**
   * Kills all the managed actors.
   *
  **/
  protected abstract void closeAll();

  /**
   * Diffuses a message to the actors of the actor space.
   *
   * @param m  the message.
   *
  **/
  public void broadcast(final Message m)
  {
    Controller.CONTROLLER.getRegistry().actors().forEach(
        a -> a.getReference().push(m));
  }

  /**
   * Diffuses a message to a set of actors of the actor space.
   *
   * @param m  the message.
   * @param s  the references of the actors.
   *
  **/
  public void multicast(final Message m, final Set<Reference> s)
  {
    for (Reference r : s)
    {
      r.push(m);
    }
  }
}
