package it.unipr.sowide.actodes.executor.passive;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.passive.SharedActor;
import it.unipr.sowide.actodes.actor.passive.CycleListActor.TimeoutMeasure;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code SharedScheduler} class uses the "cooperative scheduling"
 * mechanism for executing {@code SharedActor} actors.
 *
 * @see SharedActor
 *
**/

public final class SharedScheduler extends Scheduler<SharedActor>
{
  private static final long serialVersionUID = 1L;

  // Non processed message queue.
  private final CopyOnWriteArrayList<Message> queue;

  private TimeoutMeasure measure;

  /**
   * Class constructor.
   *
   * @param m  the timeout measure unit.
   *
  **/
  public SharedScheduler(final TimeoutMeasure m)
  {
    this.queue = new CopyOnWriteArrayList<>();

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   * @param m  the timeout measure unit.
   *
  **/
  public SharedScheduler(final Termination t, final TimeoutMeasure m)
  {
    super(t);

    this.queue = new CopyOnWriteArrayList<>();

    this.measure = m;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param m  the timeout measure unit.
   *
  **/
  public SharedScheduler(final Behavior b, final TimeoutMeasure m)
  {
    super(b);

    this.queue = new CopyOnWriteArrayList<>();

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
  public SharedScheduler(final Behavior b, final Termination t,
      final TimeoutMeasure m)
  {
    super(b, t);

    this.queue = new CopyOnWriteArrayList<>();

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
  public SharedScheduler(final Builder b, final TimeoutMeasure m)
  {
    super(b);

    this.queue = new CopyOnWriteArrayList<>();

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
  public SharedScheduler(final Builder b, final Termination t,
      final TimeoutMeasure m)
  {
    super(b, t);

    this.queue = new CopyOnWriteArrayList<>();

    this.measure = m;
  }

  /**
   * Gets the message queue shared among the actors.
   *
   * @return the queue;
   *
  **/
  public List<Message> getQueue()
  {
    return this.queue;
  }

  /** {@inheritDoc} **/
  @Override
  public SharedActor newActor(final Behavior b) throws Exception
  {
    return new SharedActor(this.queue, this.measure);
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.actors.add((SharedActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  public void broadcast(final Message m)
  {
    this.queue.add(m);
  }

  /** {@inheritDoc} **/
  @Override
  public void multicast(final Message m, final Set<Reference> s)
  {
    this.queue.add(m);
  }

  /** {@inheritDoc} **/
  @Override
  public void cycle()
  {
    this.queue.add(null);

    for (SharedActor s : this.actors)
    {
      s.step();
    }

    if (this.queue.size() > 0)
    {
      Message m = this.queue.get(0);

      while (m != null)
      {
        this.queue.remove(0);

        m = this.queue.get(0);
      }

      this.queue.remove(0);
    }
  }
}
