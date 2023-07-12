package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Log} class provides a partial implementation of a logging event.
 *
**/

public abstract class ActionLog extends Log
{
  private static final long serialVersionUID = 1L;

  private final Reference reference;
  private final String behavior;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param b  the behavior qualified class name of the actor.
   *
  **/
  public ActionLog(final Reference r, final String b)
  {
    this.reference = r;
    this.behavior  = b;
  }

  /**
   * Gets the reference to the parent actor.
   *
   * @return the reference.
   *
  **/
  public Reference getReference()
  {
    return this.reference;
  }

  /**
   * Gets the behavior qualified class name of the actor.
   *
   * @return the qualified class name.
   *
  **/
  public String getBehavior()
  {
    return this.behavior;
  }
}
