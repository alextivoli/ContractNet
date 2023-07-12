package it.unipr.sowide.actodes.actor.passive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.group.MulticastReference;

/**
 *
 * The {@code SharedActor} class defines an implementation of an actor
 * using the passive threading solution.
 *
 * The mailboxes of all the actors get the messages from the same queue.
 *
 * Each execution step, the actor filters the messages in the queue
 * received in the previous scheduling cycle processing the ones that:
 * have itself as destination, are broadcast messages and are multicast
 * messages of groups to which it belongs.
 *
**/

public class SharedActor extends CycleListActor
{
  private static final long serialVersionUID = 1L;

  // Processed message list.
  private final List<Message> list;
  // Current local message index;
  private int lIndex;
  // Current global message index;
  private int gIndex;

  /**
   * Class constructor.
   *
   * @param q  the message queue.
   * @param m  the timeout measure unit.
   *
  **/
  public SharedActor(final CopyOnWriteArrayList<Message> q,
      final TimeoutMeasure m)
  {
    super(q, m);

    this.list = new ArrayList<Message>();

    this.lIndex = 0;
    this.gIndex = 0;
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
    this.lIndex = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void processMessages()
  {
    this.gIndex = 0;

    while ((this.queue.get(this.gIndex) != null)
        || ((this.lIndex) < this.list.size()))
    {
      Message m = findMessage();

      if (m != null)
      {
        Entry<MessagePattern, MessageHandler> e = getDef(m);

        if (e != null)
        {
          if (this.lIndex < this.list.size())
          {
            this.list.remove(this.lIndex);
          }

          Behavior b = processMessage(m, e.getKey(), e.getValue());

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
        else if (this.lIndex < this.list.size())
        {
          Logger.LOGGER.logUnmatchedMessage(this.behavior, m);

          this.lIndex++;
        }
        else
        {
          this.list.add(m);

          this.lIndex++;
        }
      }
      else
      {
        break;
      }
    }
  }

  private Message findMessage()
  {
    try
    {
      if (this.lIndex < this.list.size())
      {
        return this.list.get(this.lIndex);
      }
      else
      {
        Message m = null;

        do
        {
          m = this.queue.get(this.gIndex++);

          if (m == null)
          {
            break;
          }
        }
        while (!isEnabled(m));

        return m;
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return null;
    }
  }

  /**
   * Checks if the actor must process the message.
   *
   * @param m  the message.
   *
   * @return <code>true</code> if the actor must process the message.
   *
  **/
  protected boolean isEnabled(final Message m)
  {
    List<Reference> l = m.getReceivers();

    if (l.contains(Behavior.SPACE)
        || l.contains(Behavior.APP)
        || l.contains(getReference()))
    {
      return true;
    }
    else
    {
      for (Reference r : l)
      {
        if ((r instanceof MulticastReference)
            && ((MulticastReference) r).belongTo(getReference()))
        {
          return true;
        }
      }
    }

    return false;
  }
}
