package it.unipr.sowide.actodes.distribution.activemq;

import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.distribution.Connection;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code JmsConnection} class supports the communication
 * towards a remote actor space taking advantage of JMS technology.
 *
**/

public final class JmsConnection implements Connection
{
  // Destination service provider reference.
  private final Reference destination;
  // JMS queue session.
  private final QueueSession session;
  // JMS queue sender.
  private final QueueSender sender;

  /**
   * Class constructor.
   *
   * @param r
   *
   * the actor space service provider reference of the destination actor space.
   *
   * @param s  the JMS queue session.
   * @param e  the JMS queue sender.
   *
  **/
  public JmsConnection(
      final Reference r, final QueueSession s, final QueueSender e)
  {
    this.destination = r;
    this.session     = s;
    this.sender      = e;
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
      ObjectMessage objMessage = this.session.createObjectMessage();

      objMessage.setObject(m);
      this.sender.send(objMessage);

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }
}
