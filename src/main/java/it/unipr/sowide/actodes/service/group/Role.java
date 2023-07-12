package it.unipr.sowide.actodes.service.group;

/**
 *
 * The {@code Role} enumeration defines the possible roles of an actor
 * in a communication.
 *
**/

public enum Role
{
  /**
   * Identifies the subscriber role, i.e., the actor wants to receive the
   * messages exchanged in the communication group.
   *
  **/
  SUBSCRIBER,
  /**
   * Identifies the publisher role, i.e., the actor wants to send messages
   * to the communication group.
   *
  **/
  PUBLISHER,
  /**
   * Identifies and actor with both the subscriber and publisher roles.
   *
  **/
  EXCHANGER
}
