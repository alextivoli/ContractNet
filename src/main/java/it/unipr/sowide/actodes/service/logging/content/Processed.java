package it.unipr.sowide.actodes.service.logging.content;

import java.io.Serializable;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Processed} class is used for informing about
 * the end of the processing of a message.
 *
**/

public final class Processed extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Serializable state;
  private final MessagePattern pattern;
  private final Message message;
  private final long time;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param b  the behavior qualified class name.
   * @param s  the behavior state information.
   * @param p  the message pattern.
   * @param m  the message.
   * @param t  the processing time.
   *
  **/
  public Processed(final Reference r, final String b,
      final Serializable s, final MessagePattern p,
      final Message m, final long t)
  {
    super(r, b);

    this.state   = s;
    this.pattern = p;
    this.message = m;
    this.time    = t;
  }

  /**
   * Gets the behavior state information.
   *
   * @return the state information.
   *
  **/
  public Serializable getState()
  {
    return this.state;
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

  /**
   * Gets the processing time.
   *
   * @return the time.
   *
  **/
  public long getTime()
  {
    return this.time;
  }
}
