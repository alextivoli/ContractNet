package it.unipr.sowide.actodes.service.group.content;

import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.group.Role;

/**
 *
 * The {@code Signup} class defines a task request asking an actor space
 * service provider to add one or more actors to a group.
 *
 * Note that the actor that registered the group can add one or more
 * actors to the group; the other actors can only add themselves.
 *
 * Moreover, the operation also fails if the provider service
 * does not manage a group with such a name.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Done
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Signup implements Group
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final Reference[] actors;
  private final Role role;

  /**
   * Class constructor.
   *
   * @param n  the group name.
   * @param r  the role of the actor.
   *
   * Note that this constructor creates objects
   * that actors can use for joining themselves to a group.
   *
   * However, in the case of a closed group, only the actor that
   * creates the group can use the object for joining it.
   *
  **/
  public Signup(final String n, final Role r)
  {
    this.name   = n;
    this.actors = null;
    this.role   = r;
  }

  /**
   * Class constructor.
   *
   * @param n  the group name.
   * @param r  the role of the actors.
   * @param a  the references of the actors.
   *
   * Note that this constructor creates objects that the actor
   * that creates the group can use for adding to it some actors.
   *
  **/
  public Signup(final String n, final Role r, final Reference... a)
  {
    this.name   = n;
    this.actors = a;
    this.role   = r;
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
   * Gets the actor role.
   *
   * @return the role.
   *
  **/
  public Role getRole()
  {
    return this.role;
  }

  /**
   * Gets the references of the actors.
   *
   * @return the references.
   *
  **/
  public Reference[] getReferences()
  {
    return this.actors;
  }
}
