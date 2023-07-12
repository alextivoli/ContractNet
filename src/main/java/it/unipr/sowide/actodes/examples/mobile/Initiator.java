package it.unipr.sowide.actodes.examples.mobile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.distribution.activemq.ActiveMqConnector;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;
import it.unipr.sowide.actodes.service.mobile.Mobile;
import it.unipr.sowide.actodes.service.mobile.Mobiler;
/**
 *
 * The {@code Initiator} class defines a behavior that create an
 * {@code Explorer} actor and then periodically sends a message to it.
 *
 * Moreover, the {@code Explorer} actor creates some actors and moves
 * to a new actor space.
 *
 * The execution ends when the {@code Explorer} actor visited all the
 * actor spaces of the application.
 *
 * This class allows to a user to select the number of nodes
 * of the distributed application.
 *
 * When the execution is distributed on a set of actors spaces, then the first
 * must be an broker actor space, then there can be zero or more node actor
 * space, and finally the last one must be the initiator actor space.
 *
 * @see Explorer
 *
**/

public final class Initiator extends Mobile
{
  private static final long serialVersionUID = 1L;

  /**
   * Timeout length (in milliseconds).
   *
  **/
  protected static final long TIMEOUT = 100;

  /**
   * Mobile actor reference.
   *
  **/
  private Reference explorer;

  private MessageHandler c1;


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      ArrayList<Reference> p = new ArrayList<>();
      HashSet<Reference> r   = new HashSet<>();

      p.add(PROVIDER);

      this.explorer = mobile(new Explorer(p, r));

      if (this.explorer != null)
      {
        this.c1 = (n) -> {
          future(this.explorer, Status.ALIVE, TIMEOUT,  this.c1);

          return null;
        };
      }

      future(this.explorer, Status.ALIVE, TIMEOUT,  this.c1);

      return null;
    };

    c.define(START, h);

    MessagePattern.contentPattern(new IsInstance(Error.class));
    c.define(KILL, DESTROYER);
  }

  /**
   * Starts both the mobile actor example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
   * Note that this actor space must be started after all the other
   * actor spaces of the application.
   *
  **/
  public static void main(final String[] v)
  {
    Configuration c = SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.addService(new Mobiler());

    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter:");
    System.out.println(" b for starting the broker of the application");
    System.out.println(" n for starting a node of the application");
    System.out.println(" i for starting the initiator of the application");

    String s = scanner.next();

    scanner.close();

    switch (s)
    {
      case "b":
        c.setExecutor(new OldScheduler());
        c.setConnector(new ActiveMqConnector(true));
        break;
      case "n":

        c.setExecutor(new OldScheduler());
        c.setConnector(new ActiveMqConnector());
        break;
      case "i":

        c.setExecutor(new OldScheduler(new Initiator()));
        c.setConnector(new ActiveMqConnector());
        break;
      default:
        return;
    }

    c.start();
  }
}
