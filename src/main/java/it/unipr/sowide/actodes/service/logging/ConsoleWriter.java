package it.unipr.sowide.actodes.service.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;

/**
 *
 * The {@code ConsoleWriter} defines a textual writer performing
 * its output on the console.
 *
 * Its implementation is based on the Java Logging API.
 *
**/

public final class ConsoleWriter extends ConsoleHandler implements Writer
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * This writer uses the default textual formatter.
   *
   * @see TextualFormatter
   *
  **/
  public ConsoleWriter()
  {
    setFormatter(new TextualFormatter());
  }

  /**
   * Class constructor.
   *
   * @param f  the logging data formatter.
   *
  **/
  public ConsoleWriter(final Formatter f)
  {
    setFormatter(f);
  }
}
