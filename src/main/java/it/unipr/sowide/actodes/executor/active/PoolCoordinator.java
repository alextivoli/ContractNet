package it.unipr.sowide.actodes.executor.active;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.active.ActiveActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code PoolCoordinator} class uses a {@code CachedThreadPool}
 * for the execution of active actors.
 *
 * @see java.util.concurrent.Executors#newCachedThreadPool
 * @see ActiveActor
 *
**/

public final class PoolCoordinator extends Coordinator
{
  private static final long serialVersionUID = 1L;

  // sleep time for waiting the shutdown.
  private static final long SLEEPTIME = 10;

  // Java thread pool.
  private final ThreadPoolExecutor pool;

  /**
   * Class constructor.
   *
  **/
  public PoolCoordinator()
  {
    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public PoolCoordinator(final Termination t)
  {
    super(t);

    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  public PoolCoordinator(final Behavior b)
  {
    super(b);

    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
  **/
  public PoolCoordinator(final Behavior b, final Termination t)
  {
    super(b, t);

    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
  **/
  public PoolCoordinator(final Builder b)
  {
    super(b);

    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actor of the application.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public PoolCoordinator(final Builder b, final Termination t)
  {
    super(b, t);

    this.pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Actor a)
  {
    this.pool.execute((ActiveActor) a);
  }

  /** {@inheritDoc} **/
  @Override
  protected void closeAll()
  {
    super.closeAll();

    while (this.pool.getActiveCount() > 0)
    {
      try
      {
        Thread.sleep(SLEEPTIME);
      }
      catch (InterruptedException e)
      {
        continue;
      }
    }

    this.pool.shutdown();
  }
}
