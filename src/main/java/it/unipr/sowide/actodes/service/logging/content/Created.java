package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Created} class is used for informing about
 * the creation of an actor.
 *
**/

public final class Created extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Reference child;
  private final String cBehavior;

  /**
   * Class constructor.
   *
   * @param r  the reference to the parent actor.
   * @param b  the behavior qualified class name of the parent actor.
   * @param n  the reference to the child actor.
   * @param c  the behavior qualified class name of the child actor.
   *
  **/
  public Created(
      final Reference r, final String b, final Reference n, final String c)
  {
    super(r, b);

    this.child     = n;
    this.cBehavior = c;
  }

  /**
   * Gets the reference to the child actor.
   *
   * @return the reference.
   *
  **/
  public Reference getChild()
  {
    return this.child;
  }

  /**
   * Gets the behavior qualified class name of the child actor.
   *
   * @return the qualified class name.
   *
  **/
  public String getChildBehavior()
  {
    return this.cBehavior;
  }
}
