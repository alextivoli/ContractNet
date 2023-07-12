package it.unipr.sowide.actodes.distribution.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Disconnected} class is used for informing about
 * the remotion of a connection with a remote actor space.
 *
**/

public final class Disconnected implements Network
{
  // Serialization identifier.
  private static final long serialVersionUID = 1L;

  // Source actor space service provider reference.
  private final Reference source;
  // Destination actor space service provider reference.
  private final Reference destination;

  /**
   * Class constructor.
   *
   * @param s  the source actor space service provider reference.
   * @param d  the destination actor space service provider reference.
   *
  **/
  public Disconnected(final Reference s, final Reference d)
  {
    this.source      = s;
    this.destination = d;
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

  /**
   * Gets the destination actor space service provider reference.
   *
   * @return the reference.
   *
  **/
  public Reference getDestination()
  {
    return this.destination;
  }
}
