package it.unipr.sowide.actodes.service.logging.content;

import java.io.Serializable;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Destroyed} class is used for informing about
 * the shutdown of an actor.
 *
**/

public final class Destroyed extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Serializable state;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param b  the qualified class name of the behavior.
   * @param s  the behavior state information.
   *
  **/
  public Destroyed(final Reference r, final String b, final Serializable s)
  {
    super(r, b);

    this.state = s;
  }

  /**
   * Gets the behavior state information.
   *
   * @return the state information.
   *
  **/
  public Serializable getState()
  {
    return this.state;
  }
}
