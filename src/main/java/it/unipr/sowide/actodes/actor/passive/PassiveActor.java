package it.unipr.sowide.actodes.actor.passive;

import it.unipr.sowide.actodes.actor.Actor;

/**
 *
 * The {@code PassiveActor} class provides a partial implementation of
 * an actor using the passive threading solution.
 *
**/

public abstract class PassiveActor extends Actor
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  protected PassiveActor()
  {
    this.phase = Phase.CREATED;
  }

  /**
   * Performs an actor execution step.
   *
  **/
  public abstract void step();
}
