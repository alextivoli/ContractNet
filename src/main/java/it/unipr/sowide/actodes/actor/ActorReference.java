package it.unipr.sowide.actodes.actor;

import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code ActorReference} class defines the references for accessing
 * to the actors of the actor space.
 *
**/

public class ActorReference extends Reference
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * @param a  the actor address.
   * @param i  the actor instance.
   *
  **/
  public ActorReference(final String a, final Actor i)
  {
    super(a);

    this.actor = i;
  }

  /**
   * Clears the reference when its actor kills itself.
   *
   * If the actor is still registered in the actor space,
   * then the method does not perform any work.
   *
  **/
  public void clear()
  {
    if (!Controller.CONTROLLER.getRegistry().exist(this))
    {
      this.actor = null;
    }
  }

  /**
   * Restores the reference for a revived actor.
   *
   * @param a  the actor.
   *
   * If the actor is registered, then the method does not perform any work.
   *
  **/
  public void restore(final Actor a)
  {
    if (!Controller.CONTROLLER.getRegistry().exist(this)
        && this.equals(a.getReference())
        && (this.actor == null))
    {
      this.actor = a;
    }
  }
}
