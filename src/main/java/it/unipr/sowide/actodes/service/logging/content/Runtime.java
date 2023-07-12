package it.unipr.sowide.actodes.service.logging.content;


/**
 *
 * The {@code Runtime} class is used for informing about the execution
 * of the methods of the runtime components of the actor space.
 *
**/

public final class Runtime extends Log
{
  private static final long serialVersionUID = 1L;

  private final String runtime;
  private final String method;
  private final Object[] parameters;
  private final Object result;

  /**
   * Class constructor.
   *
   * @param c  the class qualified name of the runtime component.
   * @param n  the method name of the runtime component.
   * @param p  the parameters of the method.
   * @param o  the result of the method.
   *
  **/
  public Runtime(
      final String c, final String n, final Object[] p, final Object o)
  {
    this.runtime    = c;
    this.method     = n;
    this.parameters = p;
    this.result     = o;
  }

  /**
   * Gets the qualified class name.
   *
   * @return the qualified class name.
   *
  **/
  public String getRuntime()
  {
    return this.runtime;
  }

  /**
   * Gets the method name.
   *
   * @return the name.
   *
  **/
  public String getMethod()
  {
    return this.method;
  }

  /**
   * Gets the method   /**
   * Gets the method parameters.
   *
   * @return the parameters.
   *
  **/
  public Object[] getParameters()
  {
    return this.parameters;
  }

  /**
   * Gets the message result.
   *
   * @return the result.
   *
  **/
  public Object getResult()
  {
    return this.result;
  }
}
