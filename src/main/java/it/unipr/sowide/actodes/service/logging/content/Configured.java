package it.unipr.sowide.actodes.service.logging.content;

import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Configured} class is used for informing about
 * the configuration of the actor space.
 *
**/

public final class Configured extends Log
{
  private static final long serialVersionUID = 1L;

  private final Reference reference;
  private final Configuration configuration;

  /**
   * Class constructor.
   *
   * @param r  the reference to the actor space.
   * @param c  the configuration of the actor space.
   *
  **/
  public Configured(final Reference r, final Configuration c)
  {
    this.reference     = r;
    this.configuration = c;
  }

  /**
   * Gets the reference to the actor space.
   *
   * @return the reference.
   *
  **/
  public Reference getReference()
  {
    return this.reference;
  }

  /**
   * Gets the configuration.
   *
   * @return the configuration.
   *
  **/
  public Configuration getConfiguration()
  {
    return this.configuration;
  }
}
