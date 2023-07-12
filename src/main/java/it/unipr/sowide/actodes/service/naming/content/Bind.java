package it.unipr.sowide.actodes.service.naming.content;

/**
 *
 * The {@code Bind} class defines a task request asking an actor space
 * service provider to bind the sender reference to the specified name.
 *
 * Its answer, if requested, contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Done
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Bind implements Naming
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
  public Bind(final String n)
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
