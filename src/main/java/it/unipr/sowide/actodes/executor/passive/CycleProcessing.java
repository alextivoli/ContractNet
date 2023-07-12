package it.unipr.sowide.actodes.executor.passive;

import it.unipr.sowide.actodes.executor.passive.Scheduler.Cycle;
import it.unipr.sowide.actodes.service.logging.LogFilter;
import it.unipr.sowide.actodes.service.logging.content.Processed;

/**
 *
 * The {@code CycleProcessing} defines a logging object constraint
 * that allows to accept only all the logging of the processing of cycle
 * messages coming from the actor executor.
 *
**/

public class CycleProcessing extends LogFilter
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public CycleProcessing()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Object o)
  {
    if (o instanceof Processed)
    {
      Processed p = (Processed) o;

      if (p.getMessage().getContent() instanceof Cycle)
      {
        return true;
      }
    }

    return false;
  }
}
