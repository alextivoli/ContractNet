package it.unipr.bottitivoli.contractnet;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.active.PoolCoordinator;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

import java.util.Scanner;

/**
 *
 * The {@code Initiator} class defines a behavior that creates
 * a Master actor and a list of workers actor.
 *
 * @author Filippo Botti, Alex Tivoli
**/

public final class Initiator extends Behavior
{
  private static final long serialVersionUID = 1L;

  private Reference master;
  private Reference[] workers;

  private final boolean isStorageEnable;
  private final int nWorkers;

 /**
   * Constructor for the Initiator class.
   *
   * @param nWorkers The number of workers to be initialized in the distributed system.
   * @param isStorageEnable A boolean indicating whether storage is enabled in the system.
   *                        If true, storage is enabled; if false, storage is disabled.
   */
  public Initiator( final int nWorkers , final boolean isStorageEnable)
  {
    this.nWorkers = nWorkers;
    this.isStorageEnable = isStorageEnable;
    this.workers = new Reference[this.nWorkers];
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler startHandler = (m) -> {
      for (int i = 0; i < nWorkers; i++)
      {
        this.workers[i] = (actor(new Worker(this.isStorageEnable)));
      }
      this.master = actor(new Master(this.workers, this.isStorageEnable));
      return null;
    };

    MessageHandler terminateApp = (m) -> {
      send(m.getSender(), Kill.KILL);
      send(APP, Kill.KILL);
      send(SpaceInfo.INFO.getBroker(), Kill.KILL);
				
      return Shutdown.SHUTDOWN;
    };

    c.define(START, startHandler);
    c.define(KILL,terminateApp);
  }

  public static void main(final String[] v)
  {
    Configuration c =  SpaceInfo.INFO.getConfiguration();


    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());


    Scanner scanner = new Scanner(System.in);

    System.out.println("How many Workers? ");

    int n = Integer.parseInt(scanner.next());

    System.out.println("Is storage Enabled? (y/n) ");

    String s = scanner.next();

    boolean isStorageEnable = false;

    if(s.equals("y") || s.equals(("Y"))){
      isStorageEnable = true;
    }

    scanner.close();

    c.setExecutor(new PoolCoordinator(new Initiator(n, isStorageEnable)));

    c.start();


  }
}
