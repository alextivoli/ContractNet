
package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.passive.CycleListActor.TimeoutMeasure;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.Length;
import it.unipr.sowide.actodes.executor.passive.CycleScheduler;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Initiator} class builds the initial set of actors and starts
 * a simulation of the classical dining philosophers concurrency problem.
 *
**/

public final class Initiator implements Builder
{
  private int nPhi;
  private int tMin;
  private int tMax;
  private int eMin;
  private int eMax;
  private double cFor;

  /**
   * Class constructor.
   *
   * @param n  the number of philosophers.
   * @param a  the minimum  thinking time.
   * @param b  the maximum thinking time.
   * @param c  the minimum eating time.
   * @param d  the maximum eating time.
   * @param p  the probability to try to catch the two forks.
   *
  **/
  public Initiator(final int n, final int a, final int b,
      final int c, final int d, final double p)
  {
    this.nPhi = n;
    this.tMin = a;
    this.tMax = b;
    this.eMin = c;
    this.eMax = d;

    this.cFor = p;
  }

  /** {@inheritDoc} **/
  @Override
  public void build(final Executor<? extends Actor> e)
  {
    Reference[] forks = new Reference[nPhi + 1];

    for (int i = 0; i < nPhi; i++)
    {
      forks[i] = e.actor(new FreeFork());
    }

    Reference left = forks[nPhi - 1];

    for (int i = 0; i < nPhi; i++)
    {
      PhilosopherState state = new PhilosopherState();

      state.setForks(left, forks[i]);

      state.setThinkingTime(tMin, tMax);
      state.setEatingTime(eMin, eMax);
      state.setCatchForks(cFor);

      e.actor(new ThinkingPhilosopher(state));

      left = forks[i];
    }
  }

  /**
   * Starts an actor space running the dining philosophers simulation.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    final int nPhi = 2;
    final int tMin = 4;
    final int tMax = 8;
    final int eMin = 1;
    final int eMax = 3;

    final double cFor = 0.33;

    final int length = 1000;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS);
    //c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new CycleScheduler(
        new Initiator(nPhi, tMin, tMax, eMin, eMax, cFor),
        new Length(length), TimeoutMeasure.CY));

    c.start();
  }
}
