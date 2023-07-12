package it.unipr.sowide.actodes.executor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.passive.CycleActor;
import it.unipr.sowide.actodes.actor.passive.CycleListActor.TimeoutMeasure;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code CycleScheduler} class uses the "cooperative scheduling"
 * mechanism for executing {@code CycleActor} actors.
 *
 * @see CycleActor
 *
**/

public class CycleScheduler extends Scheduler<CycleActor>
{
  private static final long serialVersionUID = 1L;

  private TimeoutMeasure measure;

  /**
   * Class constructor.
   *
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final TimeoutMeasure m)
  {
    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final Termination t,
      final TimeoutMeasure m)
  {
    super(t);

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final Behavior b, final TimeoutMeasure m)
  {
    super(b);

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final Behavior b, final Termination t,
      final TimeoutMeasure m)
  {
    super(b, t);

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final Builder b,
      final TimeoutMeasure m)
  {
    super(b);

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param t  the simulation termination condition.
   * @param m  the timeout measure unit.
   *
  **/
  public CycleScheduler(final Builder b, final Termination t,
      final TimeoutMeasure m)
  {
    super(b, t);

    this.measure = m;
  }

  /** {@inheritDoc} **/
  @Override
  public CycleActor newActor(final Behavior b) throws Exception
  {
    return new CycleActor(new CopyOnWriteArrayList<>(), this.measure);
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.actors.add((CycleActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  public void cycle()
  {
    for (CycleActor a : this.actors)
    {
      a.step();
    }

    for (CycleActor a : this.actors)
    {
      a.retainLength();
    }
  }
}
