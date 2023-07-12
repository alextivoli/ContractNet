package it.unipr.sowide.actodes.actor;

import java.io.Serializable;

/**
 *
 * The {@code MessageHandler} class defines a handler
 * for processing incoming messages.
 *
 * In particular, a handler processes the messages that match the message
 * pattern associated with such a handler.
 *
**/

public interface MessageHandler extends Serializable
{
  /**
   * Processes an incoming message.
   *
   * @param m  the message.
   *
   * @return
   *
   * either the new behavior of the actor or <code>null</code>
   * if the actor maintains the current behavior.
   *
   * Note that if the return value is the
   * {@link it.unipr.sowide.actodes.actor.Shutdown Shutdown} behavior,
   * then the actor kills itself.
   *
  **/
  Behavior process(Message m);
}
