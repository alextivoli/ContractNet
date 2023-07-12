package it.unipr.sowide.actodes.interaction;


/**
 *
 * The {@code Error} enumeration defines the most common errors that
 * can happen during the interaction between actors and that are used
 * for informing about the failure of the delivery of messages.
 *
**/

public enum Error implements Response
{
  /**
   * Identifies the inability of an actor to process the message content
   * because it does not know how to manage it.
   *
  **/
  UNKNOWNCONTENT,
  /**
   * Identifies the inability of delivering a message to an actor.
   *
  **/
  UNREACHABLEACTOR,
  /**
   * Identifies the inability of an actor to perform a requested task
   * because it does not receive the correct task arguments.
   *
  **/
  WRONGARGUMENTS,
  /**
   * Identifies the unavailability of an actor to process the message content
   * because it is waiting for another kind of message content.
   *
  **/
  UNEXPECTEDCONTENT,
  /**
   * Identifies the unavailability of an actor to perform a requested task.
   *
  **/
  REFUSEDREQUEST,
  /**
   * Identifies the unavailability of an actor to process an unexpected reply
   * to an its message.
   *
  **/
  UNEXPECTEDREPLY,
  /**
   * Identifies the failure of an actor to perform a requested task.
   *
  **/
  FAILEDEXECUTION,
  /**
   * Identifies the firing of a timeout.
   *
  **/
  TIMEOUT
}
