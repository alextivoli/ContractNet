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
 * The {@code OneActor} class defines an implementation of an actor
 * using the passive threading solution.
 *
 * Each execution step, the actor can process at most a message.
 *
**/

public final class OneActor extends ListActor
{
  private static final long serialVersionUID = 1L;

  // Current message index;
  private int index;

  /**
   * Class constructor.
   *
   * @param q  the message queue.
   *
  **/
  public OneActor(final CopyOnWriteArrayList<Message> q)
  {
    super(q);

    this.index = -1;
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
    this.index = -1;
  }

  /** {@inheritDoc} **/
  @Override
  public void step()
  {
    switch (this.phase)
    {
      case RUNNING:
        Message m = null;

        while ((this.index + 1) < this.queue.size())
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

            Behavior b = processMessage(m, e.getKey(), e.getValue());

            if (b != null)
            {
              if (b.equals(Shutdown.SHUTDOWN))
              {
                shutdown();
              }
              else
              {
                become(b);
              }
            }

            return;
          }
          else
          {
            Logger.LOGGER.logUnmatchedMessage(this.behavior, m);
          }
        }

        processTimeout();

        break;

      case CREATED:
        start();

        break;

      default:
        break;
    }
  }
}
