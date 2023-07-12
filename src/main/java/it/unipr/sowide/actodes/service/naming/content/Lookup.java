package it.unipr.sowide.actodes.service.naming.content;

/**
 *
 * The {@code Lookup} class defines a task request asking an actor space
 * service provider to provide the reference of the actor bound to the
 * specified name.
 *
 * Its answer contains either the actor reference
 *  or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Lookup implements Naming
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
  public Lookup(final String n)
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
