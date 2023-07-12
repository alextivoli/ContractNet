package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Changed} class is used for informing about the
 * change of state of a behavior of an actor.
 *
**/

public final class Changed extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final String behavior;
  private final String state;

  /**
   * Class constructor.
   *
   * @param r  the actor reference.
   * @param n  the behavior qualified class name.
   * @param s  the behavior state.
   *
  **/
  public Changed(final Reference r, final String n, final String s)
  {
    super(r, n);

    this.behavior = n;
    this.state    = s;
  }

  /**
   * Gets the behavior qualified class name.
   *
   * @return the qualified class name.
   *
  **/
  public String getBehavior()
  {
    return this.behavior;
  }

  /**
   * Gets the name of the state of the behavior.
   *
   * @return the state.
   *
  **/
  public String getState()
  {
    return this.state;
  }
}
