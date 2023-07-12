package it.unipr.sowide.actodes.examples.pubsub;

import java.util.Scanner;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.distribution.activemq.ActiveMqConnector;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Create;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.service.creation.Creator;
import it.unipr.sowide.actodes.service.group.Grouper;
import it.unipr.sowide.actodes.service.group.content.Register;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

/**
 *
 * The {@code Initiator} class defines a behavior that checks if the
 * application is based on either one or more actor spaces.
 *
 * If the application involves only the local actor space then it creates a
 * set of {@code Sensor} actors and a set of {@code Processor} actors.
 *
 * Then, after a specific time, it asks all the actors to kill themselves and,
 * finally, it kills itself.
 *
 * If the application involves more than an actor space then it creates
 * a set of {@code Sensor} actors in all the other actor spaces and a set
 * of {@code Processor} actors in the local actor space.
 *
 * Then, after a specific time, it asks all the actors to kill themselves
 * and, finally, it kills itself.
 *
 * Moreover, this class allows to a user to select the type of execution,
 * the number of processors and sensors, the lifetime of the application
 * and the number of nodes of the distributed application.
 *
 * When the execution is distributed on a set of actors spaces, then
 * the first must be an broker actor space, then there can be zero or
 * more node actor space, and finally the last one must be the
 * initiator actor space.
 *
 * @see Processor
 * @see Sensor
 *
**/

public final class Initiator extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final String TOPIC = "temperature";

  private int actors;
  private int killed;
  private int processors;
  private int sensors;
  private long duration;

  /**
   * Class constructor.
   *
   * @param p  the number of processors to be created.
   * @param s  the number of sensors to be created.
   *
   * @param t
   *
   * the time to wait before asking all the other actors to kill themselves.
   *
  **/
  public Initiator(final int p, final int s, final long t)
  {
    this.processors = p;
    this.sensors    = s;
    this.duration   = t;

    this.killed = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      send(PROVIDER, new Register(TOPIC));

      if ((this.sensors > 0) && (this.processors > 0))
      {
        if (SpaceInfo.INFO.getProviders().size() > 0)
        {
          for (int i = 0; i < this.processors; i++)
          {
            System.out.println("Start Pr");
            actor(new Processor());
          }

          Create cs = new Create(new Sensor());

          for (int i = 0; i < this.sensors; i++)
          {
            System.out.println("Start Sr");
            SpaceInfo.INFO.getProviders().forEach(r -> send(r, cs));
          }

          this.actors = this.processors + this.sensors
              * SpaceInfo.INFO.getProviders().size();
        }
        else
        {
          for (int i = 0; i < this.processors; i++)
          {
            System.out.println("Start Pr");
            actor(new Processor());
          }

          for (int i = 0; i < this.sensors; i++)
          {
            System.out.println("Start Sr");
            actor(new Sensor());
          }

          this.actors = this.processors + this.sensors;
        }
      }

      MessageHandler c1 = (n) -> {
        send(APP, Kill.KILL);

        return null;
      };

      onReceive(TIMEOUT, duration, c1);

      return null;
    };

    c.define(START, h);

    MessagePattern mp = MessagePattern.contentPattern(
        new IsInstance(Done.class));

    h = (m) -> {
      this.killed++;

      if (this.killed == this.actors)
      {
        send(APP, Kill.KILL);
        send(SpaceInfo.INFO.getBroker(), Kill.KILL);

        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(mp, h);
  }

  /**
   * Starts both the standalone and the distributed version of
   * the topic-based publish subscribe system example.
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
    final int processors = 10;
    final int sensors    = 10;
    final long duration  = 1000;

    Configuration c = SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.addService(new Grouper());

    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter:");
    System.out.println(" b for starting the broker of the application");
    System.out.println(" n for starting a node of the application");
    System.out.println(" i for starting the initiator of the application");
    System.out.println(" any other character for a standalone execution");

    String s = scanner.next();

    scanner.close();

    switch (s)
    {
      case "b":
        c.setExecutor(new OldScheduler());
        c.setConnector(new ActiveMqConnector(true));
        c.addService(new Creator());
        break;
      case "n":
        c.setExecutor(new OldScheduler());
        c.setConnector(new ActiveMqConnector());
        c.addService(new Creator());
        break;
      case "i":
        c.setExecutor(new OldScheduler(
            new Initiator(processors, sensors, duration)));
        c.setConnector(new ActiveMqConnector());
        break;
      default:
        c.setExecutor(new OldScheduler(
            new Initiator(processors, sensors, duration)));
    }

    c.start();
  }
}
