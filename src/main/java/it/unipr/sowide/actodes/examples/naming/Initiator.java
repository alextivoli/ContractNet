package it.unipr.sowide.actodes.examples.naming;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.naming.Namer;

/**
 *
 * The {@code Initiator} class defines an application that describes
 * the interaction between a client and a server.
 *
 * This interaction is synchronized by the naming service.
 *
 * @see Client
 * @see Server
 *
**/

public final class Initiator implements Builder
{
  private long time;

  /**
   * Class constructor.
   *
   * @param t  the server bootstrap time.
   *
  **/
  public Initiator(final long t)
  {
    this.time = t;
  }

  /** {@inheritDoc} **/
  @Override
  public void build(final Executor<? extends Actor> e)
  {
    e.actor(new Client());
    e.actor(new Server(this.time));
  }

  /**
   * Starts an actor space running the virtual actors example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    final long t = 1;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.addService(new Namer());

    c.setFilter(Logger.ACTIONS);
    //c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new OldScheduler(new Initiator(t)));

    c.start();
  }
}
