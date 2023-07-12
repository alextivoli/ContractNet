package it.unipr.sowide.actodes.examples.broadcasting;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.active.ThreadCoordinator;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.TextualWriter;

/**
 *
 * The {@code Sender} class defines a behavior that:
 *
 * If it acts as master actor, then it sends a broadcast message.
 *
 * After the receipt of its message, it sends another broadcast
 * message and then moves to the {@code Receiver} behavior.
 *
 * If it does not act as master actor of the actor space, then, after the
 * receipt of the message of the master actor, it sends a broadcast message
 * and then moves to the {@code Receiver} behavior.
 *
 * @see Receiver
 *
**/

public final class Sender extends Behavior
{
  private static final long serialVersionUID = 1L;

  private final int actors;

  /**
   * Class constructor.
   *
  **/
  public Sender()
  {
    this.actors = 0;
  }

  /**
   * Class constructor.
   *
   * @param n  the number of actors to be created.
   *
  **/
  public Sender(final int n)
  {
    this.actors = n;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if (this.actors > 0)
      {
        for (int i = 0; i < this.actors; i++)
        {
          actor(new Sender());
        }

        send(APP, Status.ALIVE);
      }

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      send(APP, Status.ALIVE);

      return new Receiver();
    };

    c.define(ACCEPTALL, a);
  }

  /**
   * Starts an actor space running the broadcasting example.
   *
   * Information about the execution are logged on standard output.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    Configuration c = SpaceInfo.INFO.getConfiguration();

    final int actors = 3;

    c.setFilter(Logger.ACTIONS | Logger.EXECUTION | Logger.DATASAVED);

    c.addWriter(new TextualWriter("core/broadcasting"));
    c.addWriter(new ConsoleWriter());

    c.setExecutor(new ThreadCoordinator(new Sender(actors)));

    c.start();
  }
}
