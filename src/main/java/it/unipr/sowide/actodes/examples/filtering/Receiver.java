package it.unipr.sowide.actodes.examples.filtering;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.RegexMatcher;
import it.unipr.sowide.actodes.filtering.RegexPattern;
import it.unipr.sowide.actodes.filtering.constraint.Matches;

/**
 *
 * The {@code Receiver} class defines a behavior that waits for a sequence
 * of words and prints on console the words that match a predefined pattern.
 *
 * It kills itself when it receives a "kill" message.
 *
**/

public final class Receiver extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final String PATTERN = "string|cat";

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessagePattern mp = MessagePattern.contentPattern(
        new Matches<Object, RegexPattern>(new RegexPattern(PATTERN), RegexMatcher.INSTANCE));

    MessageHandler a = (m) -> {
      System.out.println("message " + m.getContent() + " matches pattern " + PATTERN);

      return null;
    };

    c.define(mp, a);

    c.define(KILL, DESTROYER);
  }
}
