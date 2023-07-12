package it.unipr.sowide.actodes.examples.persistence;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.interaction.Status;

/**
 *
 * The {@code Worker} class defines a behavior that waits for messages
 * from a {@code Master} actor until it receives a {@code KILL} message.
 *
 * When it happens it kills itself.
 *
 * @see Master
 *
**/

public final class Worker extends Behavior
{
  private static final long serialVersionUID = 1L;


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    c.define(KILL, DESTROYER);

    MessageHandler a = (m) -> {
      send(m, Status.ALIVE);

      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
