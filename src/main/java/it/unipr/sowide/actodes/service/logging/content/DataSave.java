package it.unipr.sowide.actodes.service.logging.content;

/**
 *
 * The {@code DataSave} class defines an event that an actor
 * space uses for informing about the saving of data into a file.
 *
**/

public final class DataSave extends Log
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final String file;

  /**
   * Class constructor.
   *
   * @param n  the name of the class that saves data.
   * @param f  the output file path.
   *
  **/
  public DataSave(final String n, final String f)
  {
    this.name = n;
    this.file = f;
  }

  /**
   * Gets the name of the class that saves data.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.name;
  }

  /**
   * Gets the output file path.
   *
   * @return the path.
   *
  **/
  public String getFile()
  {
    return this.file;
  }
}
