package it.unipr.sowide.actodes.service.group.content;

/**
 *
 * The {@code Deregister} class defines a task request asking an actor space
 * service provider to remove a specific group.
 *
 * Note that this request can be considered only if the requester is the
 * actor that registered the group.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see it.unipr.sowide.actodes.interaction.Done
 * @see it.unipr.sowide.actodes.interaction.Error
 *
**/

public final class Deregister implements Group
{
  private static final long serialVersionUID = 1L;

  private final String fn;

  /**
   * Class constructor.
   *
   * @param n  the group name.
   *
  **/
  public Deregister(final String n)
  {
    this.fn = n;
  }

  /**
   * Gets the group name.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.fn;
  }
}
