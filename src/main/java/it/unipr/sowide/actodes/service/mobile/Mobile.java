package it.unipr.sowide.actodes.service.mobile;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Mobile} class provide a partial implementation
 * of an actor behavior that provides a method for creating
 * mobile actors.
 *
 * @see #mobile
 *
**/

public abstract class Mobile extends Behavior
{
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new mobile actor.
   *
   * @param b  the actor behavior.
   *
   * @return
   *
   * the new mobile actor reference or <code>null</code>
   * if the creation of the actor failed.
   *
  **/
  public MobileReference mobile(final Behavior b)
  {
    try
    {
      Actor a = Controller.CONTROLLER.getExecutor().newActor(b);

      Reference r = Controller.CONTROLLER.getRegistry().add(a);

      if (r != null)
      {
        MobileReference m = new MobileReference(r.toString(), a);

        a.configure(Behavior.PROVIDER, m, b);
        b.configure(a);

        Controller.CONTROLLER.getExecutor().add(a);

        Logger.LOGGER.logActorCreation(Controller.CONTROLLER.getProvider(),
            m, b.getClass().getName());

        return m;
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    return null;
  }
}
