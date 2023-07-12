package it.unipr.sowide.actodes.service.mobile.content;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.mobile.MobileState;

/**
 *
 * The {@code Move} class defines a task request asking the local actor
 * space service provider for moving the actor to another actor space.
 *
 * Its answer contains either an <code>Accepted</code> object
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Error
 * @see it.unipr.sowide.actodes.service.mobile.content.Accepted
 *
**/

public final class Move implements Mobility
{
  private static final long serialVersionUID = 1L;

  private final MobileState state;

  /**
   * Class constructor.
   *
   * @param s the state of the mobile actor.
   *
  **/
  public Move(final MobileState s)
  {
    this.state = s;
  }

  /**
   * Gets the actor space service provider reference.
   *
   * @return the reference.
   *
  **/
  public Reference getDestination()
  {
    return this.state.getDestination();
  }

  /**
   * Gets the behavior.
   *
   * @return the behavior.
   *
  **/
  public Behavior getBehavior()
  {
    return this.state.getBehavior();
  }
}
