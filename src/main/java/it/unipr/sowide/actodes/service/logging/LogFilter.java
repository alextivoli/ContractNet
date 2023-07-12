package it.unipr.sowide.actodes.service.logging;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import it.unipr.sowide.actodes.filtering.constraint.UnaryConstraint;

/**
 *
 * The {@code CycleProcessing} interface identifies some logging record filters
 * that refines the filtering on the basis of the {@code Log} objects
 * contained in the records.
 *
 * @see it.unipr.sowide.actodes.service.logging.content.Log
 *
**/

public abstract class LogFilter implements Filter, UnaryConstraint<Object>
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public LogFilter()
  {
  }

  /**
   * Checks if a logging record must be stored.
   *
   * @param r  the record to be filtered.
   *
   * @return <code>true</code> if the record satisfies the filter.
   */
  @Override
  public final boolean isLoggable(final LogRecord r)
  {
    return eval(r.getParameters()[0]);
  }
}
