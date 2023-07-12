package it.unipr.sowide.actodes.registry;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.controller.Controller;

/**
 *
 * The {@code PersistentRegistry} class defines an implementation of an
 * actors registry for passive actors that removes from its list the idle
 * actors and adds again them when they receive a new message.
 *
 * Idle actors are moved in a persistent storage.
 *
 * This implementation does not reuses the addresses of the killed actors.
 *
 * Therefore, the registry life is constrained by the number of created actors.
 *
**/

public class PersistentRegistry extends Registry
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * It sets the maximum number of created actors to the
   * default value.
   *
   * @see it.unipr.sowide.actodes.registry.Registry#MAX
   *
  **/
  public PersistentRegistry()
  {
  }

  /**
   * Class constructor.
   *
   * @param m  the maximum number of created actors.
   *
  **/
  public PersistentRegistry(final int m)
  {
    super(m);
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final Reference r)
  {
    Actor i = this.actors.get(r);

    if (i != null)
    {
      this.actors.remove(r);
      ((ActorReference) r).clear();

      if ((this.actors.size() == 0)
          && (Controller.CONTROLLER.getStorer().size() == 0))
      {
        Controller.CONTROLLER.notifyEmpty();
      }
    }
  }

  /**
   * Removes a reference - actor pair and stores
   * the actor in the persistent storage.
   *
   * @param r  the reference.
   *
  **/
  public void store(final Reference r)
  {
    Actor i = this.actors.get(r);

    if (i != null)
    {
      this.actors.remove(r);
      ((ActorReference) r).clear();

      Controller.CONTROLLER.getStorer().store(i, r.toString());
    }
  }

  /**
   * Gets the actor associated with a reference.
   *
   * @param r  the reference.
   *
   * @return the actor.
   *
   * Note that the return value is <code>null</code> if there
   * is not an actor associated with such a reference neither
   * in the scheduling list nor in the persistence storage.
   *
  **/
  @Override
  public Actor get(final Reference r)
  {
    Actor a = this.actors.get(r);

    if (a == null)
    {
      a = load(r.toString());

      if (a != null)
      {
        Controller.CONTROLLER.getExecutor().add(a);
      }
    }

    return a;
  }

  /**
   * Gets all the idle actors.
   *
   *
   *
  **/
  public void getAll()
  {
    for (String r : Controller.CONTROLLER.getStorer().list())
    {
      Actor a = load(r);

      if (a != null)
      {
        this.actors.put((ActorReference) a.getReference(), a);
        Controller.CONTROLLER.getExecutor().add(a);
      }
    }
  }

  // Loads an actor from the persistent storage.
  private Actor load(final String r)
  {
    Actor a = Controller.CONTROLLER.getStorer().load(r);

    if ((a != null) && (r.equals(a.getReference().toString())))
    {
      ((ActorReference) a.getReference()).restore(a);

      this.actors.put((ActorReference) a.getReference(), a);

      Controller.CONTROLLER.getStorer().delete(r.toString());

      a.getBehavior().configure(a);

      return a;
    }

    return null;
  }
}

