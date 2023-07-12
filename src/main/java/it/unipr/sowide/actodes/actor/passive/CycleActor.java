package it.unipr.sowide.actodes.actor.passive;

import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code CycleActor} class defines an implementation of an actor
 * using the passive threading solution.
 *
 * Each execution step, the actor processes all the messages in the mailbox
 * received in the previous execution cycle.
 *
 * If the actor needs to perform some actions when the actor receives a cycle
 * message, then its behaviors need to associate a handler (performing such
 * actions) with the pattern that matches a cycle message.
 *
 * @see Behavior#CYCLE
 *
**/

public class CycleActor extends CycleListActor
{
  private static final long serialVersionUID = 1L;

  // Current message index;
  private int index;
  // Current messages number.
  private int length;

  /**
   * Class constructor.
   *
   * @param q  the message queue.
   * @param m  the timeout measure unit.
   *
  **/
  public CycleActor(final CopyOnWriteArrayList<Message> q,
      final TimeoutMeasure m)
  {
    super(q, m);

    this.index  = -1;
    this.length = 0;
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
    this.index = -1;
  }

  /** {@inheritDoc} **/
  @Override
  public void processMessages()
  {
    Message m = null;

    while ((this.index + 1) < this.length)
    {
      try
      {
        this.index++;

        m = this.queue.get(this.index);
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }

      Entry<MessagePattern, MessageHandler> e = getDef(m);

      if (e != null)
      {
        this.queue.remove(this.index--);
        this.length--;

        Behavior b = processMessage(m, e.getKey(), e.getValue());

        if (b != null)
        {
          if (b.equals(Shutdown.SHUTDOWN))
          {
            shutdown();
            return;
          }
          else
          {
            become(b);
          }
        }
      }
      else
      {
        Logger.LOGGER.logUnmatchedMessage(this.behavior, m);
      }
    }
  }

  /**
   * Retains the current number of messages in the mailbox.
   *
  **/
  public void retainLength()
  {
    this.length = this.queue.size();
  }
}
