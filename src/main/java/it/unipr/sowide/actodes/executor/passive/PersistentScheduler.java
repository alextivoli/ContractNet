package it.unipr.sowide.actodes.executor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.passive.SavableActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.executor.Termination;
import it.unipr.sowide.actodes.registry.PersistentRegistry;
import it.unipr.sowide.actodes.registry.Registry;
import it.unipr.sowide.actodes.service.logging.RegistryWrapper;

/**
 *
 * The {@code PersistentScheduler} class uses the "cooperative scheduling"
 * mechanism for executing {@code SavableActor} actors.
 *
 * It may avoid the execution of some actors when their number of idle cycles
 * exceeds a threshold that depends on the number of scheduled actors.
 *
 * Such idle actors are moved in a persistent storage and the scheduler moves
 * them in the memory and executes them again when they receive some new
 * messages from some other actors.
 *
 * Moreover.such a scheduler performs (if enabled) the logging of the number
 * of running actors after the end of each scheduling cycle.
 *
 * @see SavableActor
 *
**/

public final class PersistentScheduler extends Scheduler<SavableActor>
{
  private static final long serialVersionUID = 1L;

  // Storage selector.
  private Selector selector;

  /**
   * Class constructor.
   *
  **/
  public PersistentScheduler()
  {
    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Selector s)
  {
    this.selector = s;
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public PersistentScheduler(final Termination t)
  {
    super(t);

    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Termination t, final Selector s)
  {
    super(t);

    this.selector = s;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  public PersistentScheduler(final Behavior b)
  {
    super(b);

    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Behavior b, final Selector s)
  {
    super(b);

    this.selector = s;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
  **/
  public PersistentScheduler(final Behavior b, final Termination t)
  {
    super(b, t);

    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Behavior b,
      final Termination t, final Selector s)
  {
    super(b, t);

    this.selector = s;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
  **/
  public PersistentScheduler(final Builder b)
  {
    super(b);

    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Builder b, final Selector s)
  {
    super(b);

    this.selector = s;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public PersistentScheduler(final Builder b, final Termination t)
  {
    super(b, t);

    this.selector = new DefaultSelector();
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param t  the simulation termination condition.
   *
   * @param s
   *
   * the selector used for moving inactive actors in the persistent storage.
   *
  **/
  public PersistentScheduler(final Builder b,
      final Termination t, final Selector s)
  {
    super(b, t);

    this.selector = s;
  }

  /**
   * Creates the registry managing the references of the
   * actors of the actor space.
   *
   * Note the it returns an instance of the {PersistentRegistry} class.
   *
   * @return the registry.
   *
   * @see it.unipr.sowide.actodes.registry.PersistentRegistry
   *
  **/
  protected Registry registry()
  {
    return new PersistentRegistry();
  }

  /** {@inheritDoc} **/
  @Override
  public SavableActor newActor(final Behavior b) throws Exception
  {
    return new SavableActor(new CopyOnWriteArrayList<>());
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.actors.add((SavableActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  public void cycle()
  {
    for (SavableActor a : this.actors)
    {
      a.step();

      if (this.selector.eval(a.getCycles(), this.actors.size()))
      {
        remove(a);

        ((PersistentRegistry) Controller.CONTROLLER.getRegistry()).store(
            a.getReference());
      }
    }
  }

  /**
   * Diffuses a message to the actors of the actor space.
   *
   * @param m  the message.
   *
  **/
  @Override
  public void broadcast(final Message m)
  {
    Registry r = Controller.CONTROLLER.getRegistry();

    if (r instanceof RegistryWrapper)
    {
      ((PersistentRegistry) ((RegistryWrapper) r).getWrapped()).getAll();
    }
    else
    {
      ((PersistentRegistry) r).getAll();
    }

    super.broadcast(m);
  }
}
