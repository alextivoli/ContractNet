package it.unipr.sowide.actodes.service.group.content;

import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.group.MulticastReference;

/**
 *
 * The {@code Unlink} class defines a task request, sent from an actor
 * space service provider, for asking a remote actor space service provider
 * to remove the local group reference from the destinations of the remote
 * group, because there are not more subscribers to the local group.
 *
 * Note that the operation is performed only if the requester is
 * an actor space provider.
 *
 * There is not an answer for such a kind of request.
 *
**/

public final class Unlink implements Group
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final MulticastReference reference;

  /**
   * Class constructor.
   *
   * @param g  the group name.
   * @param r  the multicast reference.
   *
  **/
  public Unlink(final String g, final MulticastReference r)
  {
    this.name     = g;
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
   * Gets the multicast reference.
   *
   * @return the reference.
   *
  **/
  public Reference getReference()
  {
    return this.reference;
  }
}
