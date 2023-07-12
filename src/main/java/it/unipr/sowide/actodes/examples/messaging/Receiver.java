package it.unipr.sowide.actodes.examples.messaging;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.interaction.Status;

/**
 *
 * The {@code Receiver} class defines a behavior that waits for a specific
 * number of messages from a {@code Sender} actor and then kills itself.
 *
 * @see Sender
 *
**/

public final class Receiver extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Actor number.
  private int messages;

  /**
   * Class Constructor.
   *
   * @param n  the number of messages.
   *
  **/
  public Receiver(final int n)
  {
    this.messages = n;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      this.messages--;

      send(m, Status.ALIVE);

      if (this.messages == 0)
      {
        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
