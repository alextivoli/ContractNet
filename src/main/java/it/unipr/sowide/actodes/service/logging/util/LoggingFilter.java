package it.unipr.sowide.actodes.service.logging.util;

import it.unipr.sowide.actodes.executor.passive.CycleProcessing;
import it.unipr.sowide.actodes.service.logging.content.Configured;
import it.unipr.sowide.actodes.service.logging.content.Execution;
import it.unipr.sowide.actodes.service.logging.content.Step;

/**
 *
 * The {@code LoggingFilter} defines a logging object constraint
 * that allows to accept only all the logging useful for the simulation.
 *
 * In particular, it logs the processing of cycle messages coming from
 * the actor executor, the logging of the value of the current simulation
 * cycle and, finally, the logging of the execution results that is used
 * as notification of the end of the simulation.
 *
**/

public final class LoggingFilter extends CycleProcessing
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public LoggingFilter()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Object o)
  {
    if ((o instanceof Step)
        || (o instanceof Execution)
        || (o instanceof Configured))
    {
      return true;
    }

    return super.eval(o);
  }
}
