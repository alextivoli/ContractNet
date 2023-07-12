package it.unipr.sowide.actodes.service.group.content;

/**
 *
 * The {@code Register} class defines a task request asking an actor
 * space service provider to register a specific group.
 *
 * The operation fails if the provider service
 * already managed a group with such a name.
 *
 * Its answer contains either the reference associated
 * with the group or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public class Register implements Group
{
  private static final long serialVersionUID = 1L;

  /**
   * Defines the possible group types.
   *
   */
  public enum Type
  {
    /**
     * Identifies an open group, i.e., each actor can join the group.
     *
    **/
    OPEN,
    /**
     * Identifies a closed group, i.e.  a group where the actor that
     * register the group chooses the actors participating in the group.
     *
    **/
    CLOSED;
  }

  private final String name;
  private final Type type;

  /**
   * Class constructor.
   *
   * @param n  the group name.
   *
   * Note that this constructor creates an open group.
   *
  **/
  public Register(final String n)
  {
    this.name = n;
    this.type = Type.OPEN;
  }

  /**
   * Class constructor.
   *
   * @param n  the group name.
   * @param t  the group type.
   *
  **/
  public Register(final String n, final Type t)
  {
    this.name = n;
    this.type = t;
  }

  /**
   * Gets the group name.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.name;
  }

  /**
   * Gets the group type.
   *
   * @return the type.
   *
  **/
  public Type getType()
  {
    return this.type;
  }
}
