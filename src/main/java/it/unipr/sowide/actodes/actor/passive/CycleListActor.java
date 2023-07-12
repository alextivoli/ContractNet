package it.unipr.sowide.actodes.actor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.executor.passive.Scheduler;

/**
 *
 * The {@code CycleListActor} class provides a partial implementation
 * of an actor using the passive threading solution, maintaining messages
 * into a list and processing all the messages in the mailbox received
 * in the previous execution cycle.
 *
**/

public abstract class CycleListActor extends PassiveActor
{
  private static final long serialVersionUID = 1L;

  /**
   * Defines the possible timeout measure units.
   *
   */
  public enum TimeoutMeasure
  {
    /**
     * Number of scheduling cycles.
     *
    **/
    CY,
    /**
     * Milliseconds.
     *
    **/
    MS;
  }

  // non processed message queue.
  protected final CopyOnWriteArrayList<Message> queue;

  // Cycle message processing handler.
  protected MessageHandler cycler;

  private TimeoutMeasure measure;

  // current timeout time.
  protected long currentTime;

  /**
   * Class constructor.
   *
   * @param q  the message queue.
   * @param m  the timeout measure unit.
   *
  **/
  protected CycleListActor(final CopyOnWriteArrayList<Message> q,
      final TimeoutMeasure m)
  {
    this.queue   = q;
    this.measure = m;

    this.phase = Phase.CREATED;

    this.currentTime = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public Iterable<Message> getMailbox()
  {
    return this.queue;
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Message m)
  {
    this.queue.add(m);
  };

  /** {@inheritDoc} **/
  @Override
  protected void setSpecialDefs()
  {
    super.setSpecialDefs();

    MessageHandler t = this.statDefs.get(Behavior.CYCLE);

    if (t != null)
    {
      this.cycler = t;

      this.statDefs.remove(Behavior.CYCLE);
    }
    else
    {
      this.cycler = Behavior.DUMMY;
    }
  }

  /**
   * Gets the absolute timeout value.
   *
   * @param t  the relative timeout value.
   *
   * @return the absolute timeout value.
   *
   * Note that the timeout value can be expressed either
   * in milliseconds or in execution cycles.
   *
  **/
  @Override
  protected long getTime(final long t)
  {
    if (this.measure == TimeoutMeasure.MS)
    {
      return System.currentTimeMillis() + t;
    }

    return this.currentTime + t;
  }

  /** {@inheritDoc} **/
  @Override
  protected boolean isTimeoutExpired(final long t)
  {
    if (t > 0)
    {
      if (this.measure == TimeoutMeasure.MS)
      {
        return (t <= System.currentTimeMillis());
      }

      return (t <= this.currentTime);
    }

    return false;
  }

  /** {@inheritDoc} **/
  @Override
  public void step()
  {
    switch (this.phase)
    {
      case RUNNING:
        processMessages();
        processTimeout();

        this.currentTime++;

        Behavior b = processMessage(Scheduler.CYCLEMESSAGE,
            Behavior.CYCLE, this.cycler);

        if (b != null)
        {
          if (b.equals(Shutdown.SHUTDOWN))
          {
            shutdown();
            return;
          }

          become(b);
        }

        break;

      case CREATED:
        start();
        break;

      default:
        break;
    }
  }

  /**
   * Processes incoming messages.
   *
  **/
  protected abstract void processMessages();
}
