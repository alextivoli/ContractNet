package it.unipr.sowide.actodes.executor.passive;

import java.io.Serializable;

/**
 *
 * The {@code Selector} interface defines a selector for moving the actors
 * in the persistent storage based on both the number of its last idle
 * cycles and the number of scheduled actors.
 *
**/

public interface Selector extends Serializable
{
  /**
   * Evaluates if the current actor must be moved in the persistent storage.
   *
   * @param i  the number of idle cycles of the actor.
   * @param n  the number of scheduled actors.
   *
   * @return
   *
   * <code>true</code> if the actor must be moved in the persistent storage,
   * <code>false</code> otherwise.
   *
  **/
  boolean eval(int i, int n);
}
