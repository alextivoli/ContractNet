package it.unipr.sowide.actodes.examples.messaging;

import java.util.Scanner;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.distribution.activemq.ActiveMqConnector;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.interaction.Create;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.creation.Creator;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

/**
 *
 * The {@code Initiator} class defines a behavior that checks if the
 * application is based on either one or more actor spaces.
 *
 * If the application involves only the local actor space then it creates a
 * set of {@code Receiver} actors and a {@code Sender} actor. Finally, it
 * kills itself.
 *
 * If the application involves more than an actor space then it creates a set
 * of {@code Receiver} actors in all the actor space and a {@code Sender}
 * actor in the local actor space. Finally, it kills itself.
 *
 * Moreover, this class allows to a user to select the type of execution,
 * the number to be sent, and the number of nodes of the distributed
 * application.
 *
 * When the execution is distributed on a set of actors spaces, then the first
 * must be an broker actor space, then there can be zero or more node actor
 * space, and finally the last one must be the initiator actor space.
 *
 * @see Receiver
 * @see Sender
 *
**/

public final class Initiator extends Behavior
{
  private static final long serialVersionUID = 1L;

  private Reference[] references;

  private int receivers;
  private int messages;

  private int index;

  /**
   * Class constructor.
   *
   * @param r  the number of receivers.
   * @param m  the number of messages.
   *
  **/
  public Initiator(final int r, final int m)
  {
    this.receivers = r;
    this.messages  = m;
    this.index     = 0;
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (k) -> {
      if (this.receivers > 0)
      {
        if (SpaceInfo.INFO.getProviders().size() > 0)
        {
          int size = this.receivers * SpaceInfo.INFO.getProviders().size();
          this.references = new Reference[size];

          Create o = new Create(new Receiver(this.messages));

          MessageHandler c1 = (n) -> {
            this.references[this.index++] = (Reference) n.getContent();

            if (this.index == size)
            {
              actor(new Sender(this.references, this.messages));

              return Shutdown.SHUTDOWN;
            }

            return null;
          };

          for (int i = 0; i < this.receivers; i++)
          {
            SpaceInfo.INFO.getProviders().forEach(e -> future(e, o, c1));
          }

        }
        else
        {
          this.references = new Reference[this.receivers];

          for (int i = 0; i < this.receivers; i++)
          {
            this.references[i] = actor(new Receiver(this.messages));
          }

          actor(new Sender(this.references, this.messages));
        }
      }

      return null;
    };

    c.define(START, h);
  }

  /**
   * Starts both the standalone and the distributed version of
   * the messaging example.
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
    final int receivers = 10;
    final int messages  = 100;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

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
        c.setConnector(new ActiveMqConnector(false));
        c.addService(new Creator());
        break;
      case "i":
        c.setExecutor(new OldScheduler(new Initiator(receivers, messages)));
        c.setConnector(new ActiveMqConnector(false));
        break;
      default:
        c.setExecutor(new OldScheduler(new Initiator(receivers, messages)));
    }

    c.start();
  }
}
