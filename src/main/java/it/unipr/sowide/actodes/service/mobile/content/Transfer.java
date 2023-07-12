package it.unipr.sowide.actodes.service.mobile.content;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.service.mobile.MobileReference;

/**
 *
 * The {@code Transfer} class defines a task request sent by an actor space
 * service to another service provider for transferring a mobile actor.
 *
 * Its answer contains either a <code>Accepted</code> object
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Error
 * @see it.unipr.sowide.actodes.service.mobile.content.Accepted
 *
**/

public final class Transfer implements Mobility
{
  private static final long serialVersionUID = 1L;

  private final MobileReference current;
  private final Behavior behavior;

  /**
   * Class constructor.
   *
   * @param c  the mobile actor current reference.
   * @param b  the actor initial behavior.
   *
  **/
  public Transfer(final MobileReference c, final Behavior b)
  {
    this.current  = c;
    this.behavior = b;
  }

  /**
   * Gets the mobile actor current reference.
   *
   * @return the reference.
   *
  **/
  public MobileReference getCurrent()
  {
    return this.current;
  }

  /**
   * Gets the mobile actor initial behavior.
   *
   * @return the behavior.
   *
  **/
  public Behavior getBehavior()
  {
    return this.behavior;
  }
}
