package it.unipr.sowide.actodes.distribution.partitioner.content;

import java.util.Map;

import it.unipr.sowide.actodes.interaction.Response;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Found} class provides the information about the agents
 * of another actor space that should interact with the local agents.
 *
**/

public final class Found<L> implements Response
{
  private static final long serialVersionUID = 1L;

  private final Map<L, Reference> map;

  /**
   * Class constructor.
   *
   * @param m  the agent location - reference pairs.
   *
  **/
  public Found(final Map<L, Reference> m)
  {
    this.map = m;
  }

  /**
   * Gets the the agent location - reference pairs.
   *
   * @return the pairs.
   *
  **/
  public Map<L, Reference> getMap()
  {
    return this.map;
  }
}
