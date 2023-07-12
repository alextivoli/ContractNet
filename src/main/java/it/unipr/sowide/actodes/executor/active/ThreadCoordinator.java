package it.unipr.sowide.actodes.executor.active;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.active.ActiveActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code ThreadCoordinator} class simply uses the Java threads
 * for the execution of active actors.
 *
 * @see ActiveActor
 *
**/

public final class ThreadCoordinator extends Coordinator
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public ThreadCoordinator()
  {
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public ThreadCoordinator(final Termination t)
  {
    super(t);
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  public ThreadCoordinator(final Behavior b)
  {
    super(b);
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
  **/
  public ThreadCoordinator(final Behavior b, final Termination t)
  {
    super(b, t);
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
  **/
  public ThreadCoordinator(final Builder b)
  {
    super(b);
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
  public ThreadCoordinator(final Builder b, final Termination t)
  {
    super(b, t);
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    new Thread((ActiveActor) a).start();
  }
}
