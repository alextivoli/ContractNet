package it.unipr.sowide.actodes.interaction;

import java.io.Serializable;

/**
 *
 * The {@code Lock} enumeration defines a request asking
 * an actor to start its exclusive service.
 *
 * This enumeration has a single element: <code>LOCK</code> and it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
 * Of course, the receiver can refuse to satisfy the request.
 *
 * Its answer contains either a <code>Done</code>
 * or an <code>Error</code> object.
 *
 * @see Done
 * @see Error
 *
**/

public enum Lock implements Serializable
{
  /**
   * The singleton instance.
   *
  **/
  LOCK;
}
