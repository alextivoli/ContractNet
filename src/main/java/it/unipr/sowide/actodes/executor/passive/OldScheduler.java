package it.unipr.sowide.actodes.executor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.passive.OldActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code OldScheduler} class uses the "cooperative scheduling"
 * mechanism for executing {@code OldActor} actors.
 *
 * @see OldActor
 *
**/

public final class OldScheduler extends Scheduler<OldActor>
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public OldScheduler()
  {
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public OldScheduler(final Termination t)
  {
    super(t);
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  public OldScheduler(final Behavior b)
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
  public OldScheduler(final Behavior b, final Termination t)
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
  public OldScheduler(final Builder b)
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
  public OldScheduler(final Builder b, final Termination t)
  {
    super(b, t);
  }

  /** {@inheritDoc} **/
  @Override
  public OldActor newActor(final Behavior b) throws Exception
  {
    return new OldActor(new CopyOnWriteArrayList<>());
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.actors.add((OldActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  public void cycle()
  {
    for (OldActor a : this.actors)
    {
      a.step();
    }
  }
}
