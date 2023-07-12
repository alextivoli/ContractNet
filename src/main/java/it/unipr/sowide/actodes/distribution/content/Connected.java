package it.unipr.sowide.actodes.distribution.content;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Like} class is used for informing about
 * the creation of a connection with a remote actor space.
 *
**/

public final class Connected implements Network
{
  // Serialization identifier.
  private static final long serialVersionUID = 1L;

  // Source actor space service provider reference.
  private final Reference source;
  // Destination actor space service provider reference.
  private final Reference destination;
  // source actor space broker flag.
  private final boolean broker;

  /**
   * Class constructor.
   *
   * @param s  the source actor space service provider reference.
   * @param d  the destination actor space service provider reference.
   * @param f  the source actor space broker flag.
   *
  **/
  public Connected(final Reference s, final Reference d, final boolean f)
  {
    this.source      = s;
    this.destination = d;
    this.broker      = f;
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

  /**
   * Checks if the source actor space acts as broker.
   *
   * @return <code>true</code> if the source actor space acts as broker.
   *
  **/
  public boolean isBroker()
  {
    return this.broker;
  }
}
