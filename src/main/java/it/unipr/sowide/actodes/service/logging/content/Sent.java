package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Sent} class is used for informing about
 * the sending of a message.
 *
**/

public final class Sent extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Message message;

  /**
   * Class constructor.
   *
   * @param r  the reference to the sender actor.
   * @param b  the behavior qualified class name.
   * @param m  the message.
   *
  **/
  public Sent(final Reference r, final String b, final Message m)
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
