package it.unipr.sowide.actodes.distribution.partitioner;

import it.unipr.sowide.actodes.actor.Behavior;

/**
 *
 * The {@code ActorFactory} class defines
 * a factory for creating agents.
 *
**/

public abstract class AgentFactory<P>
{
  /**
   * Creates the behavior of a cell actor.
   *
   * @param b  the qualified name of the initial behavior.
   * @param p  the configuration parameters of the agent.
   *
   * @return the behavior.
   *
  **/
  abstract Behavior create(final String b, final P p);
}
