package it.unipr.sowide.actodes.executor.passive;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.passive.PassiveActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.Termination;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Scheduler} class provides a partial implementation
 * of a behavior of the executor actor used for creating and executing
 * passive actors.
 *
 * @see PassiveActor
 *
**/

public abstract class Scheduler<A extends PassiveActor> extends Executor<A>
{
  private static final long serialVersionUID = 1L;

  /**
   *
   * The {@code Cycle} enumeration defines an event that an executor actor
   * for passive actors may use for informing about the end of an execution
   * cycle of the actors of the actor space.
   *
   * This enumeration has a single element: <code>CYCLE</code> and it is used
   * for proving a thread safe implementation of the singleton pattern.
   *
  **/
  public enum Cycle implements Serializable
  {
    /**
     * The singleton instance.
     *
    **/
    CYCLE;
  }

  /**
   * Cycle message instance.
   *
  **/
  public static final Message CYCLEMESSAGE = new Message(
      0L, Behavior.EXECUTOR, List.of(SPACE), Cycle.CYCLE,
      0L, Message.Type.ONEWAY, Message.NOINREPLYTO);

  private Termination termination;
  private long time;

  /**
   * Scheduled actors.
   *
  **/
  protected final CopyOnWriteArrayList<A> actors;

  /**
   * Current execution step.
   *
  **/
  protected long step;

  /**
   * Class constructor.
   *
  **/
  protected Scheduler()
  {
    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = null;
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  protected Scheduler(final Termination t)
  {
    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = t;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  protected Scheduler(final Behavior b)
  {
    super(b);

    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = null;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
  **/
  protected Scheduler(final Behavior b, final Termination t)
  {
    super(b);

    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = t;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actors of the application.
   *
  **/
  protected Scheduler(final Builder b)
  {
    super(b);

    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = null;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actors of the application.
   *
   * @param t  the simulation termination condition.
   *
  **/
  protected Scheduler(final Builder b, final Termination t)
  {
    super(b);

    this.actors      = new CopyOnWriteArrayList<>();
    this.step        = 0;
    this.termination = t;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      build();
      manage();

      this.time = System.nanoTime();

      return null;
    };

    c.define(Behavior.START, a);

    a = (m) -> {
      cycle();

      Logger.LOGGER.logStep(this.step);

      this.step++;

      if (isTerminated())
      {
        closeAll();

        send(PROVIDER, Kill.KILL);
      }
      else if (!isStopped())
      {
        getReference().push(CYCLEMESSAGE);
      }

      return null;
    };

    c.define(CYCLE, a);

    c.define(Behavior.KILL, KILLER);
  }

  /**
   * Checks the value of the termination condition.
   *
   * @return <code>true</code> if the execution must terminate.
   *
  **/
  public boolean isTerminated()
  {
    return (this.termination != null) && this.termination.eval(this);
  }

  /**
   * Performs a cycle of execution of each scheduled actors.
   *
  **/
  public abstract void cycle();

  /**
   * Gets the current number of execution cycles.
   *
   * @return the number.
   *
  **/
  public long cycles()
  {
    return this.step;
  }

  /** {@inheritDoc} **/
  @Override
  protected void stopAll()
  {
  }

  /** {@inheritDoc} **/
  @Override
  protected void startAll()
  {
    getReference().push(CYCLEMESSAGE);
  }

  /** {@inheritDoc} **/
  @Override
  protected void closeAll()
  {
    for (Actor a: this.actors)
    {
      a.shutdown();
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final Actor a)
  {
    this.actors.remove(a);
  }

  /** {@inheritDoc} **/
  @Override
  public void manage()
  {
    if (!isEmpty())
    {
      getReference().push(CYCLEMESSAGE);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public long time()
  {
    return System.nanoTime() - this.time;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean isEmpty()
  {
    return this.actors.isEmpty();
  }
}
