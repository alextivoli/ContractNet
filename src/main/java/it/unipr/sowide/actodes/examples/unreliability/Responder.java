package it.unipr.sowide.actodes.examples.unreliability;

import java.util.Random;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Status;

/**
 *
 * The {@code Responder} class defines a behavior that waits for messages:
 * if it receives a kill message, then it kills itself, else it flips a coin to
 * decide to answer it.
 *
**/

public final class Responder extends Behavior
{
  private static final long serialVersionUID = 1L;

  protected static final MessagePattern ALIVEPATTERN =
      MessagePattern.contentPattern(new IsInstance(Status.class));

  // Random generator.
  private final Random random;

  /**
   * Class constructor.
   *
  **/
  public Responder()
  {
    this.random = new Random();
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if (this.random.nextInt(2) == 1)
      {
        send(m, Done.DONE);
      }

      return null;
    };

    c.define(ALIVEPATTERN, a);

    c.define(KILL, DESTROYER);
  }
}
