package it.unipr.sowide.actodes.service.logging;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.distribution.Dispatcher;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code DispatcherWrapper} class wraps a dispatcher to support
 * the logging of its activities.
 *
**/

public final class DispatcherWrapper extends Dispatcher
{
  private static final long serialVersionUID = 1L;

  // Wrapped dispatcher.
  private final Dispatcher wrapped;

  /**
   * Class constructor.
   *
   * @param w  the wrapped dispatcher.
   *
  **/
  public DispatcherWrapper(final Dispatcher w)
  {
    this.wrapped = w;
  }

  /** {@inheritDoc} **/
  @Override
  public Set<Reference> providers()
  {
    return this.wrapped.providers();
  }

  /** {@inheritDoc} **/
  @Override
  public Set<Reference> executors()
  {
    return this.wrapped.executors();
  }

  /** {@inheritDoc} **/
  @Override
  public Set<Reference> lbroadcasts()
  {
    return this.wrapped.lbroadcasts();
  }

  /** {@inheritDoc} **/
  @Override
  public Reference getBroker()
  {
    return this.wrapped.getBroker();
  }

  /** {@inheritDoc} **/
  @Override
  public void setBroker(final Reference r)
  {
    Logger.LOGGER.logRuntime(
        Logger.DISPATCHER, this.wrapped.getClass().getName(),
        "setBroker", null, r);

    this.wrapped.setBroker(r);
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Reference d)
  {
    Logger.LOGGER.logRuntime(
        Logger.DISPATCHER, this.wrapped.getClass().getName(),
        "add", null, d);

    this.wrapped.add(d);
  }

  /** {@inheritDoc} **/
  @Override
  public void delete(final Reference d)
  {
    Logger.LOGGER.logRuntime(
        Logger.DISPATCHER, this.wrapped.getClass().getName(),
        "delete", null, d);

    this.wrapped.delete(d);
  }

  @Override
  public void deliver(final Message m)
  {
    Logger.LOGGER.logRuntime(Logger.DISPATCHER,
        this.wrapped.getClass().getName(), "deliver", null, m);

    this.wrapped.deliver(m);
  }

  /** {@inheritDoc} **/
  @Override
  public void receive(final Message m)
  {
    Logger.LOGGER.logRuntime(Logger.DISPATCHER,
        this.wrapped.getClass().getName(), "receive", null, m);

    this.wrapped.receive(m);
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
    Logger.LOGGER.logRuntime(Logger.DISPATCHER,
        this.wrapped.getClass().getName(), "start", null);

    this.wrapped.start();
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
    Logger.LOGGER.logRuntime(Logger.DISPATCHER,
        this.wrapped.getClass().getName(), "shutdown", null);

    this.wrapped.shutdown();
  }
}
