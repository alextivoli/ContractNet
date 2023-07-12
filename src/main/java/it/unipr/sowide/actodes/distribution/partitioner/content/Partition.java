package it.unipr.sowide.actodes.distribution.partitioner.content;

import java.util.Map;

import it.unipr.sowide.actodes.distribution.partitioner.AgentFactory;
import it.unipr.sowide.actodes.interaction.Request;


/**
 *
 * The {@code Partition} class provides the information about the dimensions
 * of a partition of the environment where the agents act and the coordinates
 * of the left-bottom corner of the partition.
 *
 * The environment id defined by a rectangle area
 * and a partition is a rectangle area of the environment
 *
**/

public final class Partition<L, P> implements Request
{
  private static final long serialVersionUID = 1L;

  private final L environment;
  private final L partition;
  private final L vertex;

  private final Map<AgentFactory<P>, Double> map;

  /**
   * Class constructor.
   *
   * @param w  the dimensions of the rectangle defining the global environment.
   * @param d  the dimensions of the rectangle defining the partition.
   * @param v  the vertex of the rectangle defining the partition.

   * @param m  the behavior factory - probability pairs.
   *
  **/
  public Partition(final L w, final L d,
      final L v, final Map<AgentFactory<P>, Double> m)
  {
    this.environment = w;
    this.partition   = d;
    this.vertex      = v;
    this.map         = m;
  }

  /**
   * Gets the width and height of the rectangle
   * defining the global environment.
   *
   * @return the dimensions.
   *
  **/
  public L geEnvironment()
  {
    return this.environment;
  }

  /**
   * Gets the width and height of the rectangle defining a partition
   * of the environment managed by an actor space.
   *
   * @return the dimensions.
   *
  **/
  public L geRectangle()
  {
    return this.partition;
  }

  /**
   * Gets the left-bottom vertex of the rectangle defining
   * a partition of the environment managed by an actor space.
   *
   * @return the vertex.
   *
  **/
  public L geVertex()
  {
    return this.vertex;
  }

  /**
   * Gets the behavior factory - probability pairs.
   *
   * @return the pairs.
   *
  **/
  public Map<AgentFactory<P>, Double> geMap()
  {
    return this.map;
  }
}
