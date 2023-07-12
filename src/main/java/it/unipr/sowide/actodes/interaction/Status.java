package it.unipr.sowide.actodes.interaction;

/**
 *
 * The {@code Status} enumeration defines action for informing the receiver
 * that the sender is alive or informing that either is stopping itself,
 * or is killing itself.
 *
**/

public enum Status implements Inform
{
  /**
   * Identifies an alive actor.
   *
  **/
  ALIVE,
  /**
   * Identifies a stopped actor that is stopping itself.
   *
  **/
  STOPPED,
  /**
   * Identifies an actor that is killing itself.
   *
  **/
  KILLED;
}
