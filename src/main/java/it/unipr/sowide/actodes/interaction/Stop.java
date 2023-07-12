package it.unipr.sowide.actodes.interaction;

/**
 *
 * The {@code Stop} enumeration defines an action used for asking an actor
 * for stopping itself.
 *
 * This enumeration has a single element: <code>STOP</code> and it is used
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

public enum Stop implements Request
{
  /**
   * The singleton instance.
   *
  **/
  STOP;
}
