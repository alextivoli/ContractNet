package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.interaction.Request;

/**
 *
 * The {@code Get} enumeration defines a request that a {@code Consumer}
 * actor sends to the actor managing the buffer for retrieving an its element.
 *
 * This enumeration has a single element: <code>GET</code> and it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
**/

public enum Get implements Request
{
  /**
   * The singleton instance.
   *
  **/
  GET;
}
