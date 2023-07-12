package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Initialized} class is used for informing about the
 * the end of the initialization of a behavior of an actor after
 * its creation or when it changes its behavior.
 *
**/

public final class Initialized extends ActionLog
{
  private static final long serialVersionUID = 1L;

  private final Object state;
  private final long time;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor.
   * @param n  the qualified class name of the new behavior.
   * @param s  the behavior state information.
   * @param t  the behavior initialization time.
   *
  **/
  public Initialized(final Reference r,
      final String n, final Object s, final long t)
  {
    super(r, n);

    this.state = s;
    this.time  = t;
  }

  /**
   * Gets the behavior state information.
   *
   * @return the state information.
   *
  **/
  public Object getState()
  {
    return this.state;
  }

  /**
   * Gets the initialization time.
   *
   * @return the time.
   *
  **/
  public long getTime()
  {
    return this.time;
  }
}
