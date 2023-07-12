package it.unipr.sowide.actodes.interaction;

/**
 *
 * The {@code Kill} enumeration defines a request asking an actor
 * to kill itself.
 *
 * This enumeration has a single element: <code>KILL</code> and it is used
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

public enum Kill implements Request
{
  /**
   * The singleton instance.
   *
  **/
  KILL;
}
