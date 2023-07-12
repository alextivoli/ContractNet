package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Unprocessed} class is used for informing about
 * the fact that a message does not match any message pattern
 * of the message pattern - action pairs of the current behavior.
 *
**/

public final class Unprocessed extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Message message;

  /**
   * Class constructor.
   *
   * @param r  the reference of the actor.
   * @param b  the behavior qualified class name.
   * @param m  the message.
   *
  **/
  public Unprocessed(final Reference r, final String b, final Message m)
  {
    super(r, b);

    this.message   = m;
  }

  /**
   * Gets the message.
   *
   * @return the message.
   *
  **/
  public Message getMessage()
  {
    return this.message;
  }
}
