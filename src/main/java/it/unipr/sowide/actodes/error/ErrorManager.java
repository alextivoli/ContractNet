package it.unipr.sowide.actodes.error;

/**
 *
 * The {@code ErrorManager} class defines a set of methods
 * for notifying and getting information about configuration
 * and execution errors.
 *
**/

public final class ErrorManager
{
  // Class constructor.
  private ErrorManager()
  {
  }

  /**
   * Prints some information about an error to the standard error stream.
   *
   * @param e  the error information object.
   *
  **/
  public static void notify(final ErrorInfo e)
  {
    System.err.println(e.getErrors().getString(e.toString()));
  }

  /**
   * Prints the exception and its stack-trace to the standard error stream.
   *
   * @param e  the exception.
   *
  **/
  public static void notify(final Exception e)
  {
    e.printStackTrace();
  }

  /**
   * Prints the some error information to the standard error stream.
   *
   * @param e  the error information object.
   * @param s  the specific information.
   *
  **/
  public static void notify(final ErrorInfo e, final String s)
  {
    System.err.format(e.getErrors().getString(e.toString()), s);
  }

  /**
   * Prints the exception and its stack-trace to the standard error stream.
   *
   * Then it kills the actor space.
   *
   * @param e  the exception.
   *
  **/
  public static void kill(final Exception e)
  {
    e.printStackTrace();
    System.exit(-1);
  }

  /**
   * Prints the some error information to the standard error stream.
   *
   * Then it kills the actor space.
   *
   * @param e  the error information object.
   * @param s  the specific information.
   *
  **/
  public static void kill(final ErrorInfo e, final String s)
  {
    System.err.format(e.getErrors().getString(e.toString()), s);
    System.exit(-1);
  }
}
