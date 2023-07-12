package it.unipr.sowide.actodes.service.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code BinaryReader} defines a reader for binary logging files.
 *
**/

public final class BinaryReader
{
  private final File file;
  private ObjectInputStream is;

  /**
   * Class constructor.
   *
   * @param f  the absolute path of the binary logging file.
   *
  **/
  public BinaryReader(final String f)
  {
    this.file = new File(f);

    try
    {
      this.is = new ObjectInputStream(new FileInputStream(this.file));

      Logger.LOGGER.logDataLoading(BinaryReader.class.getName(),
          this.file.getAbsolutePath());
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Gets a logging event.
   *
   * @return  the event.
   *
  **/
  public Object get()
  {
    try
    {
      return this.is.readObject();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Rewinds the logging file.
   *
  **/
  public void rewind()
  {
    try
    {
      this.is.close();

      this.is = new ObjectInputStream(new FileInputStream(this.file));
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Closes the logging file.
   *
  **/
  public void close()
  {
    try
    {
      this.is.close();

      this.is = new ObjectInputStream(new FileInputStream(this.file));
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }
}
