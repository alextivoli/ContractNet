package it.unipr.sowide.actodes.actor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Message} class defines an actor message.
 *
**/

public final class Message implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * Defines the possible message types.
   *
   */
  public enum Type
  {
    /**
     * Identifies a message that does not need a reply.
     *
    **/
    ONEWAY,
    /**
     * Identifies a message that needs a reply.
     *
    **/
    TWOWAY
  }

  /**
   * Constant that identifies messages that are not a reply to another message.
   *
  **/
  public static final int NOINREPLYTO = -1;

  // Message identifier.
  private final long identifier;
  //Sender reference.
  private final Reference sender;
  // List of receiver references.
  private final List<Reference> receivers;
  // Message content.
  private final Object content;
  // Delivery time.
  private final long time;
  // Message type.
  private final Type type;
  // Replied message identifier.
  private final long inreplyto;

  /**
   * Class constructor.
   *
   * @param i  the message identifier.
   * @param s  the sender reference.
   * @param l  the list of receiver references.
   * @param c  the message content.
   * @param t  the delivery time.
   * @param e  the message type.
   * @param m  the replied message identifier.
   *
  **/
  public Message(
      final long i, final Reference s, final List<Reference> l,
      final Object c, final long t, final Type e, final long m)
  {
    this.identifier = i;
    this.sender     = s;
    this.receivers  = l;
    this.content    = c;
    this.time       = t;
    this.type       = e;
    this.inreplyto  = m;
  }

  /**
   * Gets the identifier of the message.
   *
   * @return the identifier.
   *
  **/
  public long getIdentifier()
  {
    return this.identifier;
  }

  /**
   * Gets the reference of the sender actor.
   *
   * @return the reference.
   *
  **/
  public Reference getSender()
  {
    return this.sender;
  }

  /**
   * Gets the reference of the receiver actor.
   *
   * @return the reference.
   *
  **/
  public List<Reference> getReceivers()
  {
    return this.receivers;
  }

  /**
   * Gets the reference of the receiver actor.
   *
   * @return the reference.
   *
  **/
  public Reference getReceiver()
  {
    if (this.receivers != null)
    {
      return this.receivers.get(0);
    }

    return null;
  }

  /**
   * Gets the content of the message.
   *
   * @return the content.
   *
  **/
  public Object getContent()
  {
    return this.content;
  }

  /**
   * Gets the time when the message is delivered.
   *
   * @return the time.
  **/
  public long getTime()
  {
    return this.time;
  }

  /**
   * Gets the type of the message.
   *
   * @return one between {code ONEWAY} and {code TWOWAY}.
  **/
  public Type getType()
  {
    return this.type;
  }

  /**
   * Gets the identifier of the replied message.
   *
   * @return the identifier.
   *
  **/
  public long getInReplyTo()
  {
    return this.inreplyto;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean equals(final Object o)
  {
    if (o instanceof Message)
    {
      Message m = (Message) o;

      if (this.identifier == m.getIdentifier())
      {
        return this.sender.equals(m.getSender());
      }
    }

    return false;
  }

  /** {@inheritDoc} **/
  @Override
  public int hashCode()
  {
    return Objects.hash(this.identifier, this.sender.toString());
  }
}
