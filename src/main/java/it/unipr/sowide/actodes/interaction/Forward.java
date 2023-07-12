package it.unipr.sowide.actodes.interaction;

import it.unipr.sowide.actodes.actor.Message;

/**
 *
 * The {@code Forward} class defines a request asking anactor
 * to forward a message to another actor.
 *
 * Of course, the receiver can refuse to satisfy the request.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see Done
 * @see Error
 *
**/

public class Forward implements Request
{
  private static final long serialVersionUID = 1L;

  private Message message;

  /**
   * Class constructor.
   *
   * @param m  the message.
   *
  **/
  public Forward(final Message m)
  {
    this.message = m;
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
