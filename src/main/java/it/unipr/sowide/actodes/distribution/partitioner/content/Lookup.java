package it.unipr.sowide.actodes.distribution.partitioner.content;

import java.util.Set;

import it.unipr.sowide.actodes.interaction.Request;

/**
 *
 * The {@code Lookup} class defines a task request sent by an actor scheduler
 * to a remote scheduler for asking the information about its agents
 * that should interact with its local agents.
 *
**/

public final class Lookup<L> implements Request
{
  private static final long serialVersionUID = 1L;

  private final Set<L> locations;

  /**
   * Class constructor.
   *
   * @param s
   *
   * the locations identifying the agents managed by remote schedulers.
   *
  **/
  public Lookup(final Set<L> s)
  {
    this.locations = s;
  }

  /**
   * Gets the locations identifying the agents managed by remote schedulers.
   *
   * @return the locations.
   *
  **/
  public Set<L> getLocations()
  {
    return this.locations;
  }
}
