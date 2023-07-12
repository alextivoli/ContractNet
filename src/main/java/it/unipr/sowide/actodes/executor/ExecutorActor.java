package it.unipr.sowide.actodes.executor;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.passive.Scheduler;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code ExecutorActor} class defines the executor actor of an actor
 * space, i.e., the actor that manages the execution of the other actors
 * (excluding the other special actor, i.e., the service provider)
 * of the actor space.
 *
**/

public class ExecutorActor extends Actor implements Runnable
{
  private static final long serialVersionUID = 1L;

  // non processed message queue.
  private final LinkedBlockingQueue<Message> queue;
  // processed message queue.
  private final ArrayList<Message> list;
  // Current message index;
  private int index;

  /**
   * Class constructor.
   *
  **/
  public ExecutorActor()
  {
    this.queue = new LinkedBlockingQueue<Message>();
    this.list  = new ArrayList<Message>();
    this.index = 0;
    this.phase = Phase.CREATED;
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
  }

  /**
   * Performs the actor execution.
   *
  **/
  @Override
  public void run()
  {
    if (this.phase == Phase.CREATED)
    {
      start();
    }

    while (true)
    {
      if (this.phase == Phase.KILLED)
      {
        return;
      }

      Message m  = null;
      Behavior b = null;

      try
      {
        if (this.index < this.list.size())
        {
          m = this.list.get(this.index);
        }
        else
        {
          m = this.queue.take();
        }
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }

      Entry<MessagePattern, MessageHandler> e = getDef(m);

      if (e != null)
      {
        if (this.index < this.list.size())
        {
          this.list.remove(this.index);
        }

        b = processMessage(m, e.getKey(), e.getValue());

        if (b != null)
        {
          if (b.equals(Shutdown.SHUTDOWN))
          {
            shutdown();
            return;
          }

          become(b);
        }
      }
      else if (this.index < this.list.size())
      {
        Logger.LOGGER.logUnmatchedMessage(this.behavior, m);

        this.index++;
      }
      else
      {
        this.list.add(m);

        this.index++;
      }

      processTimeout();
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
    ((Executor<?>) this.behavior).stopAll();
    ((Executor<?>) this.behavior).closeAll();

    super.shutdown();
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
    this.index = 0;
  }

  /**
   * Processes a message.
   *
   * @param m  the message.
   * @param p  the message pattern.
   * @param h  the processing handler.
   *
   * @return the next behavior.
   *
  **/
  protected Behavior processMessage(final Message m,
      final MessagePattern p, final MessageHandler h)
  {
    if (m != Scheduler.CYCLEMESSAGE)
    {
      return super.processMessage(m, p, h);
    }
    else
    {
     return h.process(m);
    }
  }
}
