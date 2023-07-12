
package it.unipr.sowide.actodes.service.mobile;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.controller.Controller;

/**
 *
 * The {@code MobileReference} class defines the references
 * used by mobile actors.
 *
**/

public final class MobileReference extends ActorReference
{
  private static final long serialVersionUID = 1L;

  private final boolean home;
  private MobileReference rRef;

  /**
   * Class constructor.
   *
   * @param i  the mobile actor initial address.
   * @param m  the mobile actor instance.
   *
  **/
  public MobileReference(final String i, final Actor m)
  {
    super(i, m);

    this.home = true;
    this.rRef = null;
  }

  /**
   * Class constructor.
   *
   * @param c  the mobile actor current address.
   * @param m  the mobile actor instance.
   * @param h  the mobile actor home reference.
   *
  **/
  public MobileReference(final String c, final Actor m, final MobileReference h)
  {
    super(c, m);

    this.home = false;
    this.rRef = h;
  }

  /**
   * Checks if it is the mobile actor home reference.
   *
   * @return <code>true</code> if it is the mobile actor home reference.
   *
  **/
  public boolean isHome()
  {
    return this.home;
  }

  /** {@inheritDoc} **/
  @Override
  public void push(final Message m)
  {
    if (this.actor != null)
    {
      this.actor.add(m);
    }
    else if (this.rRef != null)
    {
      this.rRef.push(m);
    }
    else
    {
      Controller.CONTROLLER.getDispatcher().deliver(m);
    }
  }

  /**
   * Gets the home reference of the mobile actor.
   *
   * @return the home reference.
   *
  **/
  public MobileReference getHome()
  {
    if (isHome())
    {
      return this;
    }
    else
    {
      return this.rRef;
    }
  }

  /**
   * Changes the destination reference in the home reference
   * when the actor moves to another actor space.
   *
   * @param c  the mobile actor current reference.
   * @param n  the mobile actor new reference.
   *
  **/
  public void redirect(final MobileReference c, final MobileReference n)
  {
    if (isHome())
    {
      if (((this.actor != null) && ((this.rRef == null) && this.equals(c)))
          || ((this.actor == null) && (this.rRef != null)
              && this.rRef.equals(c)))
      {
        this.rRef = n;
      }
    }
    else if (this.equals(c) && (this.rRef != null))
    {
      this.rRef.redirect(c, n);
    }
  }
}
