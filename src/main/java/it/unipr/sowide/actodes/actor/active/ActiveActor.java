package it.unipr.sowide.actodes.actor.active;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code ActiveActor} class defines an actor using the active
 * threading solution.
 *
**/

public class ActiveActor extends Actor implements Runnable
{
  private static final long serialVersionUID = 1L;

  private static final long WAITTIME = 100;

  private static final Message STOPALLMESSAGE =
      new Message(0, null, null, null, 0, null, 0);

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
  public ActiveActor()
  {
    this.queue = new LinkedBlockingQueue<Message>();
    this.list  = new ArrayList<Message>();
    this.index = 0;
    this.phase = Phase.CREATED;
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
    this.index = 0;
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
          m = this.queue.poll(WAITTIME, TimeUnit.MILLISECONDS);
        }
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }

      if (m == null)
      {
        processTimeout();
        continue;
      }
      if (m == STOPALLMESSAGE)
      {
        return;
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

  /**
   * Stops the execution of the actor when the executor actor stops
   * the execution of the application.
   *
  **/
  public void stopAll()
  {
    try
    {
      this.queue.put(STOPALLMESSAGE);
    }
    catch (Exception e)
    {
      return;
    }
  }
}
