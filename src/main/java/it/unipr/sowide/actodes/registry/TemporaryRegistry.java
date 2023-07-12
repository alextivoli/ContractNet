package it.unipr.sowide.actodes.registry;

import java.util.concurrent.ConcurrentMap;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.controller.Controller;

/**
 *
 * The {@code TemporaryRegistry} class defines an implementation of an actors
 * registry for passive actors that removes from its list the idle actors
 * and adds again them when they receive a new message.
 *
 * Idle actors are maintained in the memory.
 *
 * This implementation does not reuses the addresses of the killed actors.
 *
 * Therefore, the registry life is constrained by the number of created actors.
 *
**/

public final class TemporaryRegistry extends Registry
{
  private static final long serialVersionUID = 1L;

  /**
   * Reference - idle actor map.
   *
  **/
  protected ConcurrentMap<ActorReference, Actor> idles;

  /**
   * Class constructor.
   *
   * It sets the maximum number of created actors to the
   * default value.
   *
   * @see it.unipr.sowide.actodes.registry.Registry#MAX
   *
  **/
  public TemporaryRegistry()
  {
  }

  /**
   * Class constructor.
   *
   * @param m  the maximum number of created actors.
   *
  **/
  public TemporaryRegistry(final int m)
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

      if ((this.actors.size() == 0)  && (this.idles.size() == 0))
      {
        Controller.CONTROLLER.notifyEmpty();
      }
    }
  }

  /**
   * Removes a reference - actor pair and moves
   * the actor in the idle list.
   *
   * @param r  the reference.
   *
  **/
  public void move(final Reference r)
  {
    Actor i = this.actors.get(r);

    if (i != null)
    {
      this.actors.remove(r);
      ((ActorReference) r).clear();

      this.idles.put((ActorReference) r, i);
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
   * in the scheduling list nor in the idle list.
   *
  **/
  @Override
  public Actor get(final Reference r)
  {
    Actor a = this.actors.get(r);

    if (a == null)
    {
      a = this.idles.get(r);

      if (a != null)
      {
        ((ActorReference) a.getReference()).restore(a);
        this.actors.put((ActorReference) a.getReference(), a);
        this.idles.remove(r);
        Controller.CONTROLLER.getExecutor().add(a);
      }
    }

    return a;
  }

  /**
   * Gets all the idle actors.
   *
  **/
  public void getAll()
  {
    this.idles.forEach((r, a) -> {
      if (a != null)
      {
        r.restore(a);
        this.actors.put((ActorReference) a.getReference(), a);
        Controller.CONTROLLER.getExecutor().add(a);
      }
    });

    this.idles.clear();
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
  }
}
