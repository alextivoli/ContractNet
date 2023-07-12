package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Execution} class is used for informing about
 * the execution time and the number of actors created
 * during the execution of the actor space.
 *
 * For applications involving passive actors it provides
 * the number of scheduling cycles too.
 *
**/

public final class Execution extends Log
{
  private static final long serialVersionUID = 1L;

  private final Reference reference;
  private final long start;
  private final long end;
  private final long cycles;
  private final int created;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor space.
   * @param b  the beginning execution time of an actor space (in nanoseconds).
   * @param e  the end execution time of an actor space (in nanoseconds).
   * @param c  the number of execution cycles of the executor actor.
   * @param n  the number of created actors.
   *
  **/
  public Execution(final Reference r, final long b,
      final long e, final long c, final int n)
  {
    this.reference = r;
    this.start     = b;
    this.end       = e;
    this.cycles    = c;
    this.created  = n;
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
   * Gets the starting execution time of an actor space (in nanoseconds).
   *
   * @return the starting time.
   *
  **/
  public long getStart()
  {
    return this.start;
  }

  /**
   * Gets the end execution time of an actor space (in nanoseconds).
   *
   * @return the end time.
   *
  **/
  public long getEnd()
  {
    return this.end;
  }

  /**
   * Gets the number of execution cycles of the executor actor.
   *
   * @return the number of execution cycles.
   *
   * Note that this value is different from zero only
   * for the executor actors for passive actors.
   *
  **/
  public long getCycles()
  {
    return this.cycles;
  }

  /**
   * Gets the number of actors created in the actor space.
   *
   * @return the number.
   *
  **/
  public int getCreated()
  {
    return this.created;
  }
}
