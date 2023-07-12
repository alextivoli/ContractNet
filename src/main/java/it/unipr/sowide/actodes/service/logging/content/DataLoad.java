package it.unipr.sowide.actodes.service.logging.content;

/**
 *
 * The {@code DataLoad} class defines an event that an actor
 * space uses for informing about the loading of data from a file.
 *
**/

public final class DataLoad extends Log
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final String file;

  /**
   * Class constructor.
   *
   * @param n  the name of the class that loads data.
   * @param f  the input file path.
   *
  **/
  public DataLoad(final String n, final String f)
  {
    this.name = n;
    this.file = f;
  }

  /**
   * Gets the name of the class that loads data.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.name;
  }

  /**
   * Gets the input file path.
   *
   * @return the path.
   *
  **/
  public String getFile()
  {
    return this.file;
  }
}
