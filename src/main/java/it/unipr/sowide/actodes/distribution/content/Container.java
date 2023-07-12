package it.unipr.sowide.actodes.distribution.content;

import java.util.List;

import it.unipr.sowide.actodes.actor.Message;

/**
 *
 * The {@code Container} class is used for delivering a group
 * of messages through a single transmission.
 *
**/

public final class Container implements Network
{
  // Serialization identifier.
  private static final long serialVersionUID = 1L;

  // Source actor space.
  private final List<Message> messages;

  /**
   * Class constructor.
   *
   * @param l  the list of messages.
   *
  **/
  public Container(final List<Message> l)
  {
    this.messages = l;

  }

  /**
   * Gets the list of messages.
   *
   * @return the messages.
   *
  **/
  public List<Message> getMessages()
  {
    return this.messages;
  }
}
