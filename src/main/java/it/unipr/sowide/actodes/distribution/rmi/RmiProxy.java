package it.unipr.sowide.actodes.distribution.rmi;

import java.rmi.Remote;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code RmiProxy} interface defines the remote methods used by
 * a connection.
 *
**/

public interface RmiProxy extends Remote
{
 /**
  * Gets the remote actor space address.
  *
  * @return the address.
  *
  * @throws Exception if the operation fails.
  *
 **/
  Reference getDestination() throws Exception;

 /**
  * Forwards a message to a remote actor.
  *
  * @param m  the message.
  *
  * @throws Exception if the operation fails.
  *
 **/
 void forward(Message m) throws Exception;
}
