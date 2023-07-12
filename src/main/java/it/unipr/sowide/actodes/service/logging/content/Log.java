package it.unipr.sowide.actodes.service.logging.content;

import java.io.Serializable;

/**
 *
 * The {@code Log} class provides a partial implementation of a logging event.
 *
**/

public abstract class Log implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final long timestamp;

  /**
   * Class constructor.
   *
  **/
  public Log()
  {
    this.timestamp = System.nanoTime();
  }

  /**
   * Gets the timestamp associated with the logging event.
   *
   * @return the timestamp.
   *
  **/
  public long getTimestamp()
  {
    return this.timestamp;
  }
}
