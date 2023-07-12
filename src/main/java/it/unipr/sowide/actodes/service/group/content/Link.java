package it.unipr.sowide.actodes.service.group.content;

import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.group.MulticastReference;
import it.unipr.sowide.actodes.service.group.content.Register.Type;

/**
 *
 * The {@code Link} class defines a task request, sent from an actor space
 * service provider, for asking a remote actor space service provider to
 * add the reference associated with the local group to the references of
 * the remote group.
 *
 * Note that the operation is performed only if the requester is
 * an actor space provider.
 *
 * There is not an answer for such a kind of request.
 *
**/

public final class Link implements Group
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final Reference owner;
  private final Type type;
  private final MulticastReference reference;

  /**
   * Class constructor.
   *
   * @param n  the group name.
   * @param o  the owner reference.
   * @param t  the group type.
   * @param r  the group reference.
   *
  **/
  public Link(final String n, final Reference o,
      final Type t, final MulticastReference r)
  {
    this.name      = n;
    this.owner     = o;
    this.type      = t;
    this.reference = r;
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
   * Gets the owner reference.
   *
   * @return the reference.
   *
  **/
  public Reference getOwner()
  {
    return this.owner;
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

  /**
   * Gets the group reference.
   *
   * @return the reference.
   *
  **/
  public MulticastReference getReference()
  {
    return this.reference;
  }
}
