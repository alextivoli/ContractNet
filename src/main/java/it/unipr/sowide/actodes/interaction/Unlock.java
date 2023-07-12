package it.unipr.sowide.actodes.interaction;

import java.io.Serializable;

/**
 *
 * The {@code Unlock} enumeration defines an action used for asking
 * an actor to end its exclusive service.
 *
 * This enumeration has a single element: <code>UNLOCK</code> and it is used
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

public enum Unlock implements Serializable
{
  /**
   * The singleton instance.
   *
  **/
  UNLOCK;
}
