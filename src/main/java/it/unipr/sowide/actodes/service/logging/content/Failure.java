package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Failure} class is used for informing about
 * the failure of both actor spaces and their actors.
 *
**/

public final class Failure extends Log
{
  private static final long serialVersionUID = 1L;

  private final Reference reference;
  private final Exception exception;

  /**
   * Class constructor.
   *
   * @param r  the reference to either an actor or an actor space.
   * @param e  the exception.
   *
  **/
  public Failure(final Reference r, final Exception e)
  {
    this.reference = r;
    this.exception = e;
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
   * Gets the exception.
   *
   * @return the exception.
   *
  **/
  public Exception getException()
  {
    return this.exception;
  }
}
