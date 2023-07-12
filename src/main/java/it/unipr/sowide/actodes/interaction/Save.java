package it.unipr.sowide.actodes.interaction;

/**
 *
 * The {@code Save} enumeration defines a request  asking an actor
 * to make a persistent copy of its state.
 *
 * This enumeration has a single element: <code>SAVE</code> and it is used
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

public enum Save implements Request
{
  /**
   * The singleton instance.
   *
  **/
  SAVE;
}
