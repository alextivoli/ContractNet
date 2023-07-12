package it.unipr.sowide.actodes.distribution.rmi;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.distribution.Connection;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code RmiConnection} class supports the communication
 * towards a remote actor space taking advantage of RMI software.
 *
**/

public final class RmiConnection implements Connection
{
  // Destination service provider reference.
  private final Reference destination;
  // RMI proxy.
  private final RmiProxy proxy;

  /**
   * Class constructor.
   *
   * @param r
   *
   * the actor space service provider reference of the destination actor space.
   *
   * @param p  the RMI remote proxy.
   *
  **/
  public RmiConnection(final Reference r, final RmiProxy p)
  {
    this.destination = r;
    this.proxy       = p;
  }

  /** {@inheritDoc} **/
  @Override
  public Reference getDestination()
  {
    return this.destination;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean forward(final Message m)
  {
    try
    {
      this.proxy.forward(m);

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }
}
