package it.unipr.sowide.actodes.provider;

import java.util.Map.Entry;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code CycleActor} class defines an implementation of an actor
 * using the passive threading solution.
 *
 * Each execution step, the actor processes all the messages in the mailbox
 * received in the previous execution cycle.
 *
**/

public class ProviderActor extends Actor
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public ProviderActor()
  {
  }

  /**
   * Initializes the actor.
   *
  **/
  public void init()
  {
    start();
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Message m)
  {
    Entry<MessagePattern, MessageHandler> e = getDef(m);

    if (e != null)
    {
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
          return;
        }
      }
    }
    else
    {
      Logger.LOGGER.logUnmatchedMessage(this.behavior, m);
    }
  }

  /** {@inheritDoc} **/
  @Override
  protected void rewind()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public Iterable<Message> getMailbox()
  {
    return null;
  }
}
