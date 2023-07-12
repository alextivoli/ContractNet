package it.unipr.sowide.actodes.actor;

/**
 *
 * The {@code Shutdown} class defines a behavior that performs the shutdown
 * of the actor.
 *
**/

public final class Shutdown extends Behavior
{
  private static final long serialVersionUID = 1L;

  /**
   * Shutdown instance.
   *
  **/
  public static final Shutdown SHUTDOWN = new Shutdown();

  // Class constructor.
  private Shutdown()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
  }
}
