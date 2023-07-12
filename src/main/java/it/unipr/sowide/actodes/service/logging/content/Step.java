package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Step} class defines an event that an actor
 * space uses for informing about the process of time and
 * the current number of running actors.
 *
 * It is used in applications involving passive actors and corresponds
 * to the number of scheduling cycles.
 *
**/

public final class Step extends Log
{
  private static final long serialVersionUID = 1L;

  private final Reference reference;
  private final long time;
  private final int running;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor space.
   * @param t  the time.
   * @param n  the number of actors created in the actor space.
   *
  **/
  public Step(final Reference r, final long t, final int n)
  {
    this.reference = r;
    this.time      = t;
    this.running   = n;
  }

  /**
   * Gets the reference to the actor.
   *
   * @return the reference.
   *
  **/
  public Reference getReference()
  {
    return this.reference;
  }

  /**
   * Gets the time.
   *
   * @return the time.
   *
  **/
  public long getTime()
  {
    return this.time;
  }

  /**
   * Gets the number of actors created in the actor space.
   *
   * @return the number.
   *
  **/
  public int getRunning()
  {
    return this.running;
  }
}
