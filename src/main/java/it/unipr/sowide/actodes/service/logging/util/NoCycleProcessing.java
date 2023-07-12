package it.unipr.sowide.actodes.service.logging.util;

import it.unipr.sowide.actodes.executor.passive.Scheduler.Cycle;
import it.unipr.sowide.actodes.service.logging.LogFilter;
import it.unipr.sowide.actodes.service.logging.content.Processed;
import it.unipr.sowide.actodes.service.logging.content.Processing;

/**
 *
 * The {@code NoCycleProcessing} defines a logging object constraint
 * that allows to avoid the logging of the processing of cycle
 * messages coming from the actor executor.
 *
**/

public final class NoCycleProcessing extends LogFilter
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public NoCycleProcessing()
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
         return false;
       }
     }

     if (o instanceof Processing)
     {
       Processing p = (Processing) o;

       if (p.getMessage().getContent() instanceof Cycle)
       {
         return false;
       }
     }

     return true;
  }
}
