package it.unipr.sowide.actodes.configuration;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.executor.Executor;

/**
 *
 * The {@code Builder} interface identifies a method used by
 * an executor actor for building an initial set of actors.
 *
**/

public interface Builder
{
  /**
   * Builds the initial set of actors.
   *
   * @param e  the executor actor.
   *
  **/
  void build(Executor<? extends Actor> e);
}
