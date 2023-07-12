package it.unipr.sowide.actodes.interaction;

import it.unipr.sowide.actodes.actor.Behavior;

/**
 *
 * The {@code Create} class defines a request asking an actor
 * or an actor space service provider to create a new actor.
 *
 * Actors can directly create new actors in the local actor space. Therefore,
 * this service is necessary only for the creation of actors in a remote
 * actor space.
 *
 * Of course, the receiver can refuse to satisfy the request.
 *
 * Its answer contains either the reference of the new actor or an
 * <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Create implements Request
{
  private static final long serialVersionUID = 1L;

  // Behavior.
  private final Behavior behavior;

  /**
   * Class constructor.
   *
   * @param b  the behavior qualified class name.
   *
  **/
  public Create(final Behavior b)
  {
    this.behavior = b;
  }

  /**
   * Gets the behavior.
   *
   * @return the behavior.
   *
  **/
  public Behavior getBehavior()
  {
    return this.behavior;
  }
}
