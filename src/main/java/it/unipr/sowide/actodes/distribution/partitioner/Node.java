package it.unipr.sowide.actodes.distribution.partitioner;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.actor.passive.CycleListActor.TimeoutMeasure;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.distribution.partitioner.content.Found;
import it.unipr.sowide.actodes.distribution.partitioner.content.Lookup;
import it.unipr.sowide.actodes.distribution.partitioner.content.Partition;
import it.unipr.sowide.actodes.executor.Termination;
import it.unipr.sowide.actodes.executor.passive.CycleScheduler;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Node} class defines an actor executor participating
 * in the  distributed cellular automata simulations.
 *
**/

public abstract class Node<L, P> extends CycleScheduler
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern PARTITIONPATTERN =
      MessagePattern.contentPattern(new IsInstance(Partition.class));
  private static final MessagePattern LOOKUPPATTERN =
      MessagePattern.contentPattern(new IsInstance(Lookup.class));
  private static final MessagePattern FOUNDPATTERN =
      MessagePattern.contentPattern(new IsInstance(Found.class));
  private static final MessagePattern DONEPATTERN =
      MessagePattern.contentPattern(new IsInstance(Done.class));

  private PartitionBuilder<L, P> builder;

  private Set<Reference> nodes;
  private int size;
  private int received;

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  public Node(final PartitionBuilder<L, P> b, final Termination t)
  {
    super(t, TimeoutMeasure.CY);

    this.received = 0;
    this.builder = b;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    super.cases(c);

    MessageHandler h = (m) -> {
      this.received++;

      if (this.received == this.size)
      {
        this.received = 0;

        cycle();

        Logger.LOGGER.logStep(this.step);

        this.step++;

        if (isTerminated())
        {
          closeAll();
          send(PROVIDER, Kill.KILL);
        }

        this.nodes.forEach(n -> send(n, Done.DONE));
      }
      return null;
    };

    c.define(DONEPATTERN, h);

    h = (m) -> {
      Partition<L, P> p = (Partition) m.getContent();

      this.nodes = SpaceInfo.INFO.getExecutors();
      this.size = this.nodes.size();

      Lookup<L> l = builder.findRemoteContacts(p);

      SpaceInfo.INFO.getExecutors().forEach(e -> send(e, l));

      return null;
    };

    c.define(PARTITIONPATTERN, h);

    h = (m) -> {
      send(m.getSender(), builder.updateMap(m));

      return null;
    };

    c.define(LOOKUPPATTERN, h);

    h = (m) -> {
      this.builder.findLocalContacts(m);

      this.received++;

      if (this.received == this.size)
      {
        this.received = 0;

        this.builder.findAgentContacts();

        this.nodes.forEach(n -> send(n, Done.DONE));
      }
      return null;
    };

    c.define(FOUNDPATTERN, h);

    h = (m) -> {
      if (m.getSender().equals(Behavior.PROVIDER))
      {
        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(KILL, h);
  }  
}
