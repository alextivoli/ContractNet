package it.unipr.sowide.actodes.service.naming.content;

/**
 *
 * The {@code Unsubscribe} class defines a task request asking an actor space
 * service provider to cancel the subscription to the binding of an actor
 * reference to a specified name.
 *
**/

public final class Unsubscribe implements Naming
{
  private static final long serialVersionUID = 1L;

  // Name associated with the actor.
  private final String name;

  /**
   * Class constructor.
   *
   * @param n  the binding name.
   *
  **/
  public Unsubscribe(final String n)
  {
    this.name = n;
  }

  /**
   * Gets the name.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.name;
  }
}
