package it.unipr.sowide.actodes.examples.buffer;

import java.util.HashSet;
import java.util.Scanner;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.active.PoolCoordinator;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

/**
 *
 * The {@code Initiator} class defines a behavior that creates
 * a buffer actor and a set of {@code Producer} and {@code Consumer} actors.
 *
 * After a fixed period of time it asks all the actors to kill themselves.
 *
 * Note that the buffer actor can use two types of behavior:
 * one based on behavior change and the other based on state change.
 *
 * This class allows to a user to select the type of behavior,
 * the size of the buffer, the number of producers and consumers
 * and the length of the execution.
 *
**/

public final class Initiator extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Default configuration values.
  private static final long DURATION = 1000;
  private static final int CAPACITY  = 100;
  private static final int PRODUCERS = 10;
  private static final int CONSUMERS = 10;

  private final long duration;
  private final int capacity;
  private final int nProducers;
  private final int nConsumers;
  private final Behavior behavior;

  private HashSet<Reference> rProducers;
  private HashSet<Reference> rConsumers;

  /**
   * Class constructor.
   *
   * @param f
   *
   * the implementation flag: <code>true</code> for the behavior
   * change implementation" and <code>false</code> for the state
   *  change implementation.
   *
   * @param t  the execution time (in milliseconds),
   * @param s  the size of the buffer.
   * @param p  the number of producers.
   * @param c  the number of consumers.
   *
  **/
  public Initiator(final boolean f, final long t,
      final int s, final int p, final int c)
  {
    this.duration = t;
    this.capacity = c;

    this.nProducers = p;
    this.nConsumers = c;

    this.rProducers = new HashSet<>();
    this.rConsumers = new HashSet<>();

    if (f)
    {
      this.behavior = new EmptyBuffer(new BufferQueue(c));
    }
    else
    {
      this.behavior = new StateBuffer(this.capacity);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      if ((this.duration > 0) && (this.capacity > 0))
      {
        MessageHandler t = (n) -> {
          this.rProducers.forEach(r -> send(r, Kill.KILL));
          this.rConsumers.forEach(r -> send(r, Kill.KILL));
          send(this.behavior.getReference(), Kill.KILL);

          return Shutdown.SHUTDOWN;
        };

        Reference r = actor(this.behavior);

        for (int i = 0; i < this.nProducers; i++)
        {
          this.rProducers.add(actor(new Producer(r)));
        }

        for (int i = 0; i < this.nConsumers; i++)
        {
          this.rConsumers.add(actor(new Consumer(r)));
        }

        onReceive(ACCEPTALL, this.duration, t);
      }

      return null;
    };

    c.define(START, h);
  }

  /**
   * Starts an actor space running the buffer example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    Configuration c =  SpaceInfo.INFO.getConfiguration();

    boolean f = c.load(v);

    final long duration = f ? c.getLong("duration") : DURATION;
    final int capacity  = f ? c.getInt("size")      : CAPACITY;
    final int producers = f ? c.getInt("producers") : PRODUCERS;
    final int consumers = f ? c.getInt("consumers") : CONSUMERS;

    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    //c.addWriter(new BinaryWriter("examples/buffer"));

    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter:");
    System.out.println(" b for the behavior change implementation");
    System.out.println(" s for the state change implementation");

    String s = scanner.next();

    scanner.close();

    switch (s)
    {
      case "b":
        c.setExecutor(new OldScheduler(new Initiator(true, duration,
            capacity, producers, consumers)));
        break;
      case "s":
        c.setExecutor(new PoolCoordinator(new Initiator(false, duration,
            capacity, producers, consumers)));
        break;
      default:
        return;
    }

    c.start();
  }
}
