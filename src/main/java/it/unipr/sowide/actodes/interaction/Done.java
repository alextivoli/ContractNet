package it.unipr.sowide.actodes.interaction;

/**
 *
 * The {@code Done} enumeration is used for informing about the successful
 * execution of a request when the execution of such a task does not produce
 * data.
 *
 * Moreover, it is used for informing about the execution of a requested
 * action when the action does not produce data and the sender requires
 * an answer.
 *
 * This enumeration has a single element: <code>DONE</code> and it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
**/

public enum Done implements Response
{
  /**
   * The singleton instance.
   *
  **/
  DONE;
}
