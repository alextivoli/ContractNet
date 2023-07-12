package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Processing} class is used for informing
 * about the start of the processing of a message.
 *
**/

public final class Processing extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final MessagePattern pattern;
  private final Message message;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param b  the behavior qualified class name.
   * @param p  the message pattern.
   * @param m  the message.
   *
  **/
  public Processing(final Reference r, final String b,
      final MessagePattern p, final Message m)
  {
    super(r, b);

    this.pattern   = p;
    this.message   = m;
  }

  /**
   * Gets the message pattern.
   *
   * @return the pattern.
   *
  **/
  public MessagePattern getPattern()
  {
    return this.pattern;
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
