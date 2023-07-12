package it.unipr.sowide.actodes.service.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Formatter;
import java.util.logging.StreamHandler;

import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code TextualWriter} defines a textual writer performing
 * its output on a file.
 *
 * Its implementation is based on the Java Logging API.
 *
**/

public final class TextualWriter extends StreamHandler implements Writer
{
  private static final long serialVersionUID = 1L;

  private String file;

  /**
   * Class constructor.
   *
   * @param p  the logging directory path.
   *
   * If the path is relative, then the directory path has as
   * initial part the path of the logging repository.
   *
   * This writer uses the default textual formatter.
   *
   * @see TextualFormatter
   *
  **/
  public TextualWriter(final String p)
  {
    try
    {
      File f = Logger.file(p, Logger.TEXT);

      setOutputStream(new FileOutputStream(f));

      setFormatter(new TextualFormatter());

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
   * @param p  the logging directory path.
   *
   * if the path is relative, then the directory path has as
   * initial part the path of the logging repository.
   *
   * @param f  the logging data formatter.
   *
  **/
  public TextualWriter(final String p, final Formatter f)
  {
    try
    {
      File w = Logger.file(p, "txt");

      setOutputStream(new FileOutputStream(w));

      setFormatter(f);

      this.file = w.getAbsolutePath();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  @Override
  public void close()
  {
    super.close();

    Logger.LOGGER.logDataSaving(TextualWriter.class.getName(), this.file);
  }
}
