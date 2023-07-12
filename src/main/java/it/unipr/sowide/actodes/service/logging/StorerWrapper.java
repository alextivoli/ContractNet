package it.unipr.sowide.actodes.service.logging;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.service.persistence.Storer;

/**
 *
 * The {@code StorerWrapper} class wraps a storer to support
 * the logging of its activities.
 *
**/

public final class StorerWrapper implements Storer
{
  private static final long serialVersionUID = 1L;

  // Wrapped storer.
  private final Storer wrapped;

  /**
   * Class constructor.
   *
   * @param w  the wrapped storer.
   *
  **/
  public StorerWrapper(final Storer w)
  {
    this.wrapped = w;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean save(final Configuration c, final String i)
  {
    boolean s = this.wrapped.save(c, i);

    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "save", s, c, i);

    return s;
  }

  /** {@inheritDoc} **/
  @Override
  public Configuration retrieve(final String i)
  {
    Configuration c = this.wrapped.retrieve(i);

    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "retrieve", c, i);

    return c;
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final String i)
  {
    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "remove", null, i);

    this.wrapped.remove(i);
  }

  /** {@inheritDoc} **/
  @Override
  public Actor load(final String i)
  {
    Actor a = this.wrapped.load(i);

    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "load", a, i);

    return a;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean store(final Actor a, final String i)
  {
    boolean b = this.wrapped.store(a, i);

    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "store", b, a, i);

    return b;
  }

  /** {@inheritDoc} **/
  @Override
  public void delete(final String i)
  {
    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "delete", null, i);

    this.wrapped.delete(i);
  }

  /** {@inheritDoc} **/
  @Override

  public String getId()
  {
    return this.wrapped.getId();
  }

  /** {@inheritDoc} **/
  @Override
  public Set<String> list()
  {
    Set<String> s = this.wrapped.list();

    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "list", s);

    return s;
  }

  /** {@inheritDoc} **/
  @Override
  public int size()
  {
    return this.wrapped.size();
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "start", null);

    this.wrapped.start();
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
    Logger.LOGGER.logRuntime(Logger.STORER,
        this.wrapped.getClass().getName(), "shutdown", null);

    this.wrapped.shutdown();
  }
}
