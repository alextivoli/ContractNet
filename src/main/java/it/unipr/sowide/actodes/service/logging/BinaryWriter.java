package it.unipr.sowide.actodes.service.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code BinaryWriter} defines a binary writer performing
 * its output on a file.
 *
 * Its implementation is based on the Java Logging API.
 *
**/

public class BinaryWriter extends Handler implements Writer
{
  private static final long serialVersionUID = 1L;

  private ObjectOutputStream os;

  private String file;

  /**
   * Class constructor.
   *
   * @param f  the file.
   *
  **/
  public BinaryWriter(final File f)
  {
    try
    {
      this.os = new ObjectOutputStream(new FileOutputStream(f));

      this.file = f.getAbsolutePath();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Class constructor.
   *
   * @param p  the logging directory path,
   *
   * if the path is relative, then the directory path has as
   * initial part the path of the logging repository.
   *
  **/
  public BinaryWriter(final String p)
  {
    try
    {
      File w = Logger.file(p, Logger.BINARY);

      this.os = new ObjectOutputStream(new FileOutputStream(w));

      this.file = w.getAbsolutePath();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Class constructor.
   *
   * @param p  the logging directory path.
   * @param e  the file extension.
   *
   * if the path is relative, then the directory path has as
   * initial part the path of the logging repository.
   *
  **/
  public BinaryWriter(final String p, final String e)
  {
    try
    {
      File w = Logger.file(p, e);

      this.os = new ObjectOutputStream(new FileOutputStream(w));

      this.file = w.getAbsolutePath();

    }
    catch (Exception i)
    {
      ErrorManager.notify(i);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized void publish(final LogRecord record)
  {
    if (isLoggable(record))
    {
      try
      {
        this.os.writeObject(record.getParameters()[0]);
      }
      catch (Exception e)
      {
        return;
      }
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized void flush()
  {
    try
    {
      this.os.flush();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized void close()
  {
    try
    {
      this.os.flush();
      this.os.close();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    Logger.LOGGER.logDataSaving(TextualWriter.class.getName(), this.file);
  }
}
