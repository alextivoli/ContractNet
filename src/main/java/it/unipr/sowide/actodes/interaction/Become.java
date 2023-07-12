package it.unipr.sowide.actodes.interaction;

import it.unipr.sowide.actodes.actor.Behavior;

/**
 *
 * The {@code Become} class defines a request asking
 * an actor to move to another behavior.
 *
 * Of course, the receiver can refuse to satisfy the request.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see Done
 * @see Error
 *
**/

public class Become implements Request
{
  private static final long serialVersionUID = 1L;

  private Behavior behavior;

  /**
   * Class constructor.
   *
   * @param b  the behavior.
   *
  **/
  public Become(final Behavior b)
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
