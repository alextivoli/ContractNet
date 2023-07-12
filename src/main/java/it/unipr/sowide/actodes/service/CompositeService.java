package it.unipr.sowide.actodes.service;

/**
 *
 * The {@code CompositeService} abstract class provides a partial implementation
 * of a composite service (i.e., a service what tasks are performed through the
 * cooperation of some different services) that an actor space can provide.

 *
**/

public abstract class CompositeService implements Service
{
  private static final long serialVersionUID = 1L;

  private final Service[] services;

  /**
   * Class constructor.
   *
   * @param s  the service.
   *
  **/
  public CompositeService(final Service... s)
  {
    this.services = s;

    link(s);
  }

  /**
   * Sets the links among the services belonging in this composite service.
   *
   * @param s  the services.
   *
   * It does nothing and must be overridden by the composite services
   * that need to link its services.
   *
  **/
  protected void link(final Service[] s)
  {
  }

  /**
   * Gets the services.
   *
   * @return the services.
   *
  **/
  public Service[] getServices()
  {
    return this.services;
  }
}
