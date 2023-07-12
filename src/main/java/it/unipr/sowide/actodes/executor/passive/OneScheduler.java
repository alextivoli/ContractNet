package it.unipr.sowide.actodes.executor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.passive.OneActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code OneScheduler} class uses the "cooperative scheduling"
 * mechanism for executing {@code OneActor} actors.
 *
 * @see OneActor
 *
**/

public final class OneScheduler extends Scheduler<OneActor>
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public OneScheduler()
  {
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public OneScheduler(final Termination t)
  {
    super(t);
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  public OneScheduler(final Behavior b)
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
  public OneScheduler(final Behavior b, final Termination t)
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
  public OneScheduler(final Builder b)
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
  public OneScheduler(final Builder b, final Termination t)
  {
    super(b, t);
  }

  /** {@inheritDoc} **/
  @Override
  public OneActor newActor(final Behavior b) throws Exception
  {
    return new OneActor(new CopyOnWriteArrayList<>());
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.actors.add((OneActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  public void cycle()
  {
    for (OneActor a : this.actors)
    {
      a.step();
    }
  }
}
