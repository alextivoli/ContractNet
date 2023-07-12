package it.unipr.sowide.actodes.service.mobile.content;

import it.unipr.sowide.actodes.service.mobile.MobileReference;

/**
 *
 * The {@code ActiveBehavior} class defines a task request asking
 * an actor space service provider for starting the mobile actor
 * just arrived in its actor space.
 *
**/

public final class Active implements Mobility
{
  private static final long serialVersionUID = 1L;

  private final MobileReference reference;


  /**
   * Class constructor.
   *
   * @param r
   *
   * the reference of the mobile actor to be started in its
   * new actor space.
   *
  **/
  public Active(final MobileReference r)
  {
    this.reference = r;
  }

  /**
   * Gets the reference of the mobile actor to be started in its
   * new actor space.
   *
   * @return the reference.
   *
  **/
  public MobileReference getReference()
  {
    return this.reference;
  }
}
