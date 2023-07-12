package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.examples.buffer.*;
import it.unipr.sowide.actodes.executor.active.PoolCoordinator;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

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
  private ArrayList<Reference> nWorker = new ArrayList<Reference>();

  private Reference master;

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
  public Initiator()
  {

  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {

        this.master = actor(new Master());

        for (int i = 0; i < 5; i++)
        {
          this.nWorker.add(actor(new Worker()));
        }



//        onReceive(ACCEPTALL, this.duration, t);

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


    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    //c.addWriter(new BinaryWriter("examples/buffer"));



    c.setExecutor(new PoolCoordinator(new Initiator()));

    c.start();


  }
}
