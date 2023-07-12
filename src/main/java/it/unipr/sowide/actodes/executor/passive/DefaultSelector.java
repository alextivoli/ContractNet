package it.unipr.sowide.actodes.executor.passive;

/**
 *
 * The {@code DefaultSelector} class defines the default implementation of
 * a selector for actors to move in the persistent storage.
 *
**/

public final class DefaultSelector implements Selector
{
  private static final long serialVersionUID = 1L;

  private static final int MAX1 = 1000;
  private static final int MAX2 = 10000;

  /**
   * {@inheritDoc}
   *
   * Note that the evaluation is <code>true</code> when either there are more
   * than a thousand of scheduled actors or when the product between the number
   * of scheduled actors and the number of inactivity cycles of the current
   * actor is greater than a ten of thousands.
   *
  **/
  @Override
  public boolean eval(final int i, final int n)
  {
    if ((i > MAX1) || ((i * n) > MAX2))
    {
      return true;
    }

    return false;
  }
}
