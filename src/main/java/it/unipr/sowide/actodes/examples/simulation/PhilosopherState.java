package it.unipr.sowide.actodes.examples.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code PhilosopherState} class maintains the information about
 * the state of a dining philosopher.
 *
**/

public final class PhilosopherState implements Serializable
{
  private static final long serialVersionUID = 1L;

  private static final Random RANDOM = new Random();

  // forks.
  private List<Reference> forks;
  // Minimum thinking time.
  private int tMin;
  // Maximum thinking time.
  private int tMax;
  // Minimum eating time.
  private int eMin;
  // Maximum eating time.
  private int eMax;
  // Probability to try to catch the two forks.
  private double cFor;

  /**
   * Class constructor.
   *
  **/
  public PhilosopherState()
  {
    this.forks = null;

    this.tMin = 0;
    this.tMax = 0;
    this.eMin = 0;
    this.eMax = 0;

    this.cFor = 0.0;
  }

  /**
   * Gets the references to the two forks.
   *
   * @return the references.
   *
  **/
  public List<Reference> getForks()
  {
    return this.forks;
  }

  /**
   * Sets the references to the two forks.
   *
   * @param l the left fork references.
   * @param r the right fork references.
   *
  **/
  public void setForks(final Reference l, final Reference r)
  {
    this.forks = new ArrayList<>();

    this.forks.add(l);
    this.forks.add(r);
  }

  /**
   * Gets a random value for the thinking time.
   *
   * @return the thinking time.
   *
  **/
  public int getThinkingTime()
  {
    return RANDOM.nextInt(this.tMax - this.tMin) + this.tMin;
  }

  /**
   * Sets the minimum and maximum value for the thinking time.
   *
   * @param l the minimum value (inclusive).
   * @param h the maximum value (exclusive).
   *
  **/
  public void setThinkingTime(final int l, final int h)
  {
    this.tMin = l;
    this.tMax = h;
  }

  /**
   * Gets a random value for the eating time.
   *
   * @return the eating time.
   *
  **/
  public int getEatingTime()
  {
    return RANDOM.nextInt(this.eMax - this.eMin) + this.eMin;
  }

  /**
   * Sets the minimum and maximum value for the eating time.
   *
   * @param l the minimum value (inclusive).
   * @param h the maximum value (exclusive).
   *
  **/
  public void setEatingTime(final int l, final int h)
  {
    this.eMin = l;
    this.eMax = h;
  }

  /**
   * Checks if the philosopher can try to catch the two forks.
   *
   * @return <code>true</code> for enabling the catching.
   *
  **/
  public boolean getCatchForks()
  {
    return (RANDOM.nextDouble() < this.cFor);
  }

  /**
   * Sets the probability to try to catch the two forks.
   *
   * @param p the probability.
   *
  **/
  public void setCatchForks(final double p)
  {
    this.cFor = p;
  }
}
