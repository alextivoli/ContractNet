package it.unipr.sowide.actodes.examples.broadcasting;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.interaction.Status;

/**
 *
 * The {@code Receiver} class defines a behavior that waits for a broadcast
 * messages from all the actors of the actor space and then kills itself.
 *
**/

public final class Receiver extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Actor number.
  private int actors;

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      send(APP, Status.ALIVE);

      this.actors = SpaceInfo.INFO.getPopulation();

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      this.actors--;

      if (this.actors == 0)
      {
        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
