package it.unipr.sowide.actodes.controller;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code SpecialReference} class defines the references for accessing
 * to the special actors of the actor space.
 *
**/

public class SpecialReference extends Reference
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public SpecialReference()
  {
    super();

    this.actor = null;
  }

  /**
   * Class constructor.
   *
   * @param s  the special address.
   *
  **/
  public SpecialReference(final String s)
  {
    super(s);

    this.actor = null;
  }

  /**
   * Configures the reference.
   *
   * @param s  the special address.
   *
   * @param a
   *
   * either the service provider actor or the scheduling actor.
   *
   * If the reference is already configured,
   * then the method does not perform any work.
   *
  **/
  public void configure(final String s, final Actor a)
  {
    if (!Controller.CONTROLLER.isRunning())
    {
      this.address  = s;
      this.location = s.substring(s.indexOf(".") + 1);
      this.actor    = a;
    }
  }

  /**
   * Restores the reference.
   *
   * @param a  the special actor.
   *
   * If the reference is already restored, then the method does nothing.
   *
  **/
  public void restore(final Actor a)
  {
    if (this.equals(a.getReference()) && (this.actor == null))
    {
      this.actor = a;
    }
  }
}
