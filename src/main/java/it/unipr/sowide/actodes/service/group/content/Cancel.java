package it.unipr.sowide.actodes.service.group.content;

import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.group.Role;

/**
 *
 * The {@code Cancel} class defines a task request asking an actor space
 * service provider to cancel a member from a group.
 *
 * Note that the actor that registered the group can remove one or more
 * actors from the group; the other actors can only remove themselves.
 *
 * Moreover, the operation also fails if the provider service does not manage
 * a group with such a name.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Done
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Cancel implements Group
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
   * Creates an object that an actor can use for removing itself or
   * an its role from a group (i.e., if an actor acts with both
   * the publisher and the subscriber roles, then it can decide
   * to maintain one of the two roles in the group).
   *
  **/
  public Cancel(final String n, final Role r)
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
   * Creates an object that the actor that creates the group can use for
   * removing a set of actors or a their role from a group (i.e., if some of
   * such actors act with both the publisher and the subscriber roles,
   * then they can maintain one of the two roles in the group).
   *
  **/
  public Cancel(final String n, final Role r, final Reference... a)
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
