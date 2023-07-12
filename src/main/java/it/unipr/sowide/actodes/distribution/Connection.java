package it.unipr.sowide.actodes.distribution;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Connection} interface defines a connection.
 *
 * A connection allows the sending of messages to the actors
 * of a remote actor space.
 *
**/

public interface Connection
{
  /**
   * Gets the actor space service provider reference
   * identifying the remote actor space.
   *
   * @return the reference.
   *
  **/
  Reference getDestination();

  /**
   * Forwards a message to a remote actor.
   *
   * Of course, the actor should be reachable
   * through such a connection.
   *
   * @param m  the message.
   *
   * @return <code>true</code> if the operation is successful.
   *
  **/
  boolean forward(Message m);
}
