package it.unipr.sowide.actodes.distribution.partitioner;

import java.util.Map;
import java.util.Set;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.distribution.partitioner.content.Lookup;
import it.unipr.sowide.actodes.distribution.partitioner.content.Partition;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code PartitionBuilder} class that builds the initial set of actors
 * of a partition of a distributed cellular automata.
 *
**/

public abstract class PartitionBuilder<L, P> implements Builder
{
  public abstract Lookup<L> findRemoteContacts(final Partition<L, P> p);

  public abstract Set<L> findLocalContacts(final Message m);

  public abstract void findAgentContacts();

  public abstract Map<L, Reference> updateMap(final Message m);
}
