package it.unipr.sowide.actodes.service.logging;

import java.util.Collection;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.registry.Registry;

/**
 *
 * The {@code RegistryWrapper} class wraps a registry to support
 * the logging of its activities.
 *
**/

public final class RegistryWrapper extends Registry implements Wrapper<Registry>
{
  private static final long serialVersionUID = 1L;

  // Wrapped registry.
  private final Registry wrapped;

  /**
   * Class constructor.
   *
   * @param w  the wrapped registry.
   *
  **/
  public RegistryWrapper(final Registry w)
  {
    this.wrapped = w;
  }

  /** {@inheritDoc} **/
  @Override
  public Registry getWrapped()
  {
    return this.wrapped;
  }

  /** {@inheritDoc} **/
  @Override
  public Reference add(final Actor a)
  {
    Reference r = this.wrapped.add(a);

    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "add", r, a.toString());

    return r;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean exist(final Reference r)
  {
    boolean b = this.wrapped.exist(r);

    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "exist", b, r);

    return b;
  }

  /** {@inheritDoc} **/
  @Override
  public Actor get(final Reference r)
  {
    Actor a = this.wrapped.get(r);

    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "get", a, r);

    return a;
  }

  /** {@inheritDoc} **/
  @Override
  public Collection<Actor> actors()
  {
    return this.wrapped.actors();
  }

  /** {@inheritDoc} **/
  @Override
  public Collection<ActorReference> references()
  {
    return this.wrapped.references();
  }

  /** {@inheritDoc} **/
  @Override
  public int size()
  {
    return this.wrapped.size();
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final Reference r)
  {
    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "remove", null, r, null);

    this.wrapped.remove(r);
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "start", null);

    this.wrapped.start();
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
    Logger.LOGGER.logRuntime(Logger.REGISTRY,
        this.wrapped.getClass().getName(), "shutdown", null);

    this.wrapped.shutdown();
  }
}
