package it.unipr.sowide.actodes.distribution.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Closed} class is used for informing that
 * a remote actor space is closing all the connections.
 *
**/

public final class Closed implements Network
{
  // Serialization identifier.
  private static final long serialVersionUID = 1L;

  // Source Actor space.
  private final Reference source;

  /**
   * Class constructor.
   *
   * @param s  the source actor space service provider reference.
   *
  **/
  public Closed(final Reference s)
  {
    this.source = s;
  }

  /**
   * Gets the source actor space service provider reference.
   *
   * @return the reference.
   *
  **/
  public Reference getSource()
  {
    return this.source;
  }
}
