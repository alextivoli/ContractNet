package it.unipr.sowide.actodes.filtering;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;

/**
 *
 * The {@code MessageMatcher} enumeration defines a pattern matcher
 * that filters messages.
 *
 * This enumeration has a single element: <code>MATCHER</code> an it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
**/

public enum MessageMatcher implements Matcher<Message, MessagePattern>
{
  /**
   * The singleton instance.
   *
  **/
  MATCHER;

  /** {@inheritDoc} **/
  @Override
  public boolean match(final Message o, final MessagePattern p)
  {
    if ((o == null) || (p == null))
    {
      return false;
    }

    if (p == Behavior.ACCEPTALL)
    {
      return true;
    }

    if (p.getContent() != null)
    {
      if (!p.getContent().eval(o.getContent()))
      {
        return false;
      }
    }

    if (p.getSender() != null)
    {
      if (!p.getSender().eval(o.getSender()))
      {
        return false;
      }
    }

    if (p.getReceivers() != null)
    {
      if (!p.getReceivers().eval(o.getReceivers()))
      {
        return false;
      }
    }

    if (p.getIdentifier() != null)
    {
      if (!p.getIdentifier().eval(o.getIdentifier()))
      {
        return false;
      }
    }

    if (p.getInReplyTo() != null)
    {
      if (!p.getInReplyTo().eval(o.getInReplyTo()))
      {
        return false;
      }
    }

    if (p.getTime() != null)
    {
      if (!p.getTime().eval(o.getTime()))
      {
        return false;
      }
    }

    if (p.getType() != null)
    {
      if (!p.getType().eval(o.getType()))
      {
        return false;
      }
    }

    return true;
  }
}
