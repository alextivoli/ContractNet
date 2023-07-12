package it.unipr.sowide.actodes.executor;

import it.unipr.sowide.actodes.executor.passive.Scheduler;

/**
 *
 * The {@code Length} class defines a termination condition that becomes true
 * when the simulation arrives to a certain number of execution cycles.
 *
**/

public class Length implements Termination
{
  private static final long serialVersionUID = 1L;

  private long length;

  /**
   * Class constructor.
   *
   * @param l  the number of execution cycles to run.
   *
  **/
  public Length(final long l)
  {
    this.length = l;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Executor<?> s)
  {
    if (s instanceof Scheduler<?>)
    {
      return !(((Scheduler<?>) s).cycles() < this.length);
    }

    return false;
  }
}
