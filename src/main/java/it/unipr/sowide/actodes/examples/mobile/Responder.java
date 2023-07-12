package it.unipr.sowide.actodes.examples.mobile;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.interaction.Done;

/**
 *
 * The {@code Responder} class defines a behavior that waits for messages:
 * if it receives a kill message, then it kills itself, else it answers it.
 *
**/

public final class Responder extends Behavior
{
  private static final long serialVersionUID = 1L;


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    c.define(KILL, DESTROYER);

    MessageHandler a = (m) -> {
      send(m, Done.DONE);

      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
