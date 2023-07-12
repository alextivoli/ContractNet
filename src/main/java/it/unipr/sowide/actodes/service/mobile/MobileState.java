package it.unipr.sowide.actodes.service.mobile;

import java.io.Serializable;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code MobileState} class defines the state of a mobile actor that
 * contains all the information for moving it in a new actor space.
 *
**/

public final class MobileState implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final Reference destination;
  private final Behavior behavior;

  /**
   * Class constructor.
   *
   * @param d
   *
   * the reference of the service provider of the destination actor space.
   *
   * @param b
   *
   * the qualified class name of the initial behavior in the destination
   * actor space.
   *
  **/
  public MobileState(final Reference d, final Behavior b)
  {
    this.destination = d;
    this.behavior    = b;
  }

  /**
   * Gets the actor space service provider reference.
   *
   * @return the reference.
   *
  **/
  public Reference getDestination()
  {
    return this.destination;
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
