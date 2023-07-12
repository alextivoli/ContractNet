package it.unipr.sowide.actodes.actor;

import java.util.List;

import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.filtering.Field;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.Pattern;
import it.unipr.sowide.actodes.filtering.constraint.UnaryConstraint;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code MessagePattern} class defines a pattern for filtering messages.
 *
**/

public final class MessagePattern implements Pattern
{
  private static final long serialVersionUID = 1L;

  /**
   * Builds a content-based message pattern.
   *
   * @param c  the message content constraint.
   *
   * @return the pattern.
   *
  **/
  public static MessagePattern contentPattern(final UnaryConstraint<Object> c)
  {
    return new MessagePattern(new MessagePatternField(
        MessageField.CONTENT, c));
  }

  /**
   * Defines the fields of a message pattern.
   *
  **/
  public enum MessageField implements Field
  {
    /**
     * Message identifier field.
     *
    **/
    IDENTIFIER,
    /**
     * Sender reference field.
     *
    **/
    SENDER,
    /**
     * Receiver references field.
     *
    **/
    RECEIVERS,
    /**
     * Message content field.
     *
    **/
    CONTENT,
    /**
     * Delivery time field.
     *
    **/
    TIME,
    /**
     * Message type field.
     *
    **/
    TYPE,
    /**
     * Replied message identifier field.
     *
    **/
    INREPLYTO
  }

  // Message identifier constraint.
  private final UnaryConstraint<Long> identifier;
  //Sender reference constraint.
  private final UnaryConstraint<Reference> sender;
  // Receiver reference constraint.
  private final UnaryConstraint<List<Reference>> receivers;
  // Message content constraint.
  private final UnaryConstraint<Object> content;
  // Delivery time constraint.
  private final UnaryConstraint<Long> time;
  // Message type constraint.
  private final UnaryConstraint<Type> type;
  // Replied message identifier constraint.
  private final UnaryConstraint<Long> inreplyto;

  /**
   * Class constructor.
   *
   * @param i  the message identifier constraint.
   * @param s  the sender reference constraint.
   * @param l  the receiver references constraint.
   * @param c  the message content constraint.
   * @param t  the delivery time constraint.
   * @param n  the message type constraint.
   * @param r  the replied message identifier constraint.
   *
  **/
  public MessagePattern(
      final UnaryConstraint<Long> i, final UnaryConstraint<Reference> s,
      final UnaryConstraint<List<Reference>> l, final UnaryConstraint<Object> c,
      final UnaryConstraint<Long> t, final UnaryConstraint<Type> n,
      final UnaryConstraint<Long> r)
  {
    this.identifier = i;
    this.sender     = s;
    this.receivers  = l;
    this.content    = c;
    this.time       = t;
    this.type       = n;
    this.inreplyto  = r;
  }

  /**
   * Class constructor.
   *
   * @param p  a message pattern.
   * @param f  a set of message pattern field objects.
   *
   * Note that the value of this message pattern are built by using
   * the value of the <code>p</code> message pattern and updated through
   * the <code>f</code> message pattern field objects.
   *
  **/
  @SuppressWarnings("unchecked")
  public MessagePattern(final MessagePattern p,
      final MessagePatternField... f)
  {
    UnaryConstraint<Long> i            = p.getIdentifier();
    UnaryConstraint<Reference> s       = p.getSender();
    UnaryConstraint<List<Reference>> l = p.getReceivers();
    UnaryConstraint<Object> c          = p.getContent();
    UnaryConstraint<Long> t            = p.getTime();
    UnaryConstraint<Type> n            = p.getType();
    UnaryConstraint<Long> r            = p.getInReplyTo();

    for (MessagePatternField e : f)
    {
      switch (e.getField())
      {
        case IDENTIFIER:
          i = (UnaryConstraint<Long>) e.getValue();
          break;
        case SENDER:
          s = (UnaryConstraint<Reference>) e.getValue();
          break;
        case RECEIVERS:
          l = (UnaryConstraint<List<Reference>>) e.getValue();
          break;
        case CONTENT:
          c = (UnaryConstraint<Object>) e.getValue();
          break;
        case TIME:
          t = (UnaryConstraint<Long>) e.getValue();
          break;
        case TYPE:
          n = (UnaryConstraint<Type>) e.getValue();
          break;
        case INREPLYTO:
          r = (UnaryConstraint<Long>) e.getValue();
          break;
        default:
          break;
      }
    }

    this.identifier = i;
    this.sender     = s;
    this.receivers  = l;
    this.content    = c;
    this.time       = t;
    this.type       = n;
    this.inreplyto  = r;
  }

  /**
   * Class constructor.
   *
   * @param f  the fields initialization information.
   *
  **/
  @SuppressWarnings("unchecked")
  public MessagePattern(final MessagePatternField... f)
  {
    UnaryConstraint<Long> i            = null;
    UnaryConstraint<Reference> s       = null;
    UnaryConstraint<List<Reference>> l = null;
    UnaryConstraint<Object> c          = null;
    UnaryConstraint<Long> t            = null;
    UnaryConstraint<Type> n            = null;
    UnaryConstraint<Long> r            = null;

    for (MessagePatternField e : f)
    {
      switch (e.getField())
      {
        case IDENTIFIER:
          i = (UnaryConstraint<Long>) e.getValue();
          break;
        case SENDER:
          s = (UnaryConstraint<Reference>) e.getValue();
          break;
        case RECEIVERS:
          l = (UnaryConstraint<List<Reference>>) e.getValue();
          break;
        case CONTENT:
          c = (UnaryConstraint<Object>) e.getValue();
          break;
        case TIME:
          t = (UnaryConstraint<Long>) e.getValue();
          break;
        case TYPE:
          n = (UnaryConstraint<Type>) e.getValue();
          break;
        case INREPLYTO:
          r = (UnaryConstraint<Long>) e.getValue();
          break;
        default:
          break;
      }
    }

    this.identifier = i;
    this.sender     = s;
    this.receivers  = l;
    this.content    = c;
    this.time       = t;
    this.type       = n;
    this.inreplyto  = r;
  }

  /**
   * Gets the message identifier constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Long> getIdentifier()
  {
    return this.identifier;
  }

  /**
   * Gets the message sender reference constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Reference> getSender()
  {
    return this.sender;
  }

  /**
   * Gets the message receiver reference constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<List<Reference>> getReceivers()
  {
    return this.receivers;
  }

  /**
   * Gets the the message content constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Object> getContent()
  {
    return this.content;
  }

  /**
   * Returns the message delivery time constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Long> getTime()
  {
    return this.time;
  }

  /**
   * Returns the message type constraint.
   *
   * @return the constraint.
  **/
  public UnaryConstraint<Type> getType()
  {
    return this.type;
  }

  /**
   * Returns the replied message identifier constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Long> getInReplyTo()
  {
    return this.inreplyto;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean equals(final Object o)
  {
    if (o instanceof MessagePattern)
    {
      if (this == o)
      {
        return true;
      }
    }

    return false;
  }


  /** {@inheritDoc} **/
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
}
