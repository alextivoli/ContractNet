package it.unipr.sowide.actodes.distribution.partitioner;

import java.util.Map;

import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.executor.Termination;

/**
 *
 * The {@code Master} class defines an actor executor for distributed cellular
 * automata simulations that starts the work of the actor executors of the
 * other actor spaces participating in the simulation.
 *
**/

public abstract class Master<L, P> extends Node<L, P>
{
  private static final long serialVersionUID = 1L;

  private L environment;
  private Map<AgentFactory<P>, Double> map;

  /**
   * Class constructor.
   *
   * @param d  the global cellular automata dimensions.
   * @param f  the cell behavior factory - probability map.
   * @param t  the simulation termination condition.
   *
  **/
  public Master(final PartitionBuilder<L, P> b, final Termination t, final L d, final Map<AgentFactory<P>, Double> f)
  {
    super(b, t);

    this.environment = d;
    this.map   = f;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    super.cases(c);
/*
    MessageHandler h = (m) -> {
      Set<Reference> s = SpaceInfo.INFO.getExecutors();

      Partitioner p = new Partitioner(environment, map, s.size() + 1);

      List<Partition<L, P>> l = p.define();

      int i = 0;

      for (Reference r : s)
      {
        send(r, l.get(i++));
      }

      send(getReference(), l.get(i));

      build();

      return null;
    };

    c.define(START, h);*/
  }
}
