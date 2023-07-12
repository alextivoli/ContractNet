package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Initializing} class is used for informing about
 * the start of the initialization of a behavior of an actor
 * after its creation or when it changes its behavior.
 *
**/

public final class Initializing extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final String oBehavior;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param n  the qualified class name of the new behavior.
   *
  **/
  public Initializing(final Reference r, final String n)
  {
    super(r, n);

    this.oBehavior = null;
  }

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param o  the qualified class name of the old behavior.
   * @param n  the qualified class name of the new behavior.
   *
  **/
  public Initializing(final Reference r, final String o, final String n)
  {
    super(r, n);

    this.oBehavior = o;
  }

  /**
   * Gets the old behavior qualified class name.
   *
   * @return the qualified class name.
   *
  **/
  public String getOldBehavior()
  {
    return this.oBehavior;
  }
}
