package it.unipr.sowide.actodes.actor;

/**
 *
 * The {@code CaseFactory} interface defines some methods for
 * binding a message pattern with a message handler.
 *
 * Note that the execution of these methods is successful only before
 * the beginning of the execution of the current actor behavior.
 *
**/

public interface CaseFactory
{
  /**
   * Defines a message pattern - message handler pair for
   * all the set of behavior states.
   *
   * @param p  the message pattern.
   * @param h  the message handler.
   *
  **/
  void define(MessagePattern p, MessageHandler h);

  /**
   * Defines a message pattern - message handler pair for
   * a subset of behavior states.
   *
   * @param p  the message pattern.
   * @param h  the message handler.
   * @param s  the subset of behavior states.
   *
  **/
  void define(MessagePattern p, MessageHandler h, BehaviorState... s);
}
