package it.unipr.sowide.actodes.service.mobile.content;

import it.unipr.sowide.actodes.service.mobile.MobileReference;

/**
 *
 * The {@code Accepted} class defines a task request sent by an actor space
 * service in response to a {@code Transfer} request if it accepted the mobile
 * actor.
 *
 * This request is used for asking the execution of the operations that
 * conclude the transfer of the mobile actor to the other actor space.
 *
**/

public final class Accepted implements Mobility
{
  private static final long serialVersionUID = 1L;

  private final MobileReference previous;
  private final MobileReference current;

  /**
   * Class constructor.
   *
   * @param p  the mobile actor previous reference.
   * @param c  the mobile actor current reference.
   *
  **/
  public Accepted(final MobileReference p, final MobileReference c)
  {
    this.previous = p;
    this.current  = c;
  }

  /**
   * Gets the previous reference of the mobile actor.
   *
   * @return the reference.
   *
  **/
  public MobileReference getPrevious()
  {
    return this.previous;
  }

  /**
   * Gets the current reference of the mobile actor.
   *
   * @return the reference.
   *
  **/
  public MobileReference getCurrent()
  {
    return this.current;
  }
}
