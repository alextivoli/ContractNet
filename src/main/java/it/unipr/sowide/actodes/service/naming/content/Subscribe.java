package it.unipr.sowide.actodes.service.naming.content;

/**
 *
 * The {@code Subscribe} class defines a task request asking an actor
 * space service provider to deliver the reference of the actor bound
 * to a specified name.
 *
 * The receiving actor space service provider checks the presence of the
 * binding of the specific name. If it is present, then it sends a reply
 * containing the actor reference, else it waits for the binding and
 * then sends the reply.
 *
**/

public final class Subscribe implements Naming
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
  public Subscribe(final String n)
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
