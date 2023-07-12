package it.unipr.sowide.actodes.examples.filtering;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.active.ThreadCoordinator;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Sender} class defines a behavior that sends a sequence
 * of words to a "receiver" actor.
 *
 * It kills itself when it completes the delivery of the words.
 *
 * @see Receiver
 *
**/

public final class Sender extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final String WORDS = "cat,white,dog,string,leon,string";

  /**
   * Class constructor.
   *
  **/
  public Sender()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {

      Reference r = actor(new Receiver());

      String[] ws = WORDS.split(",");

      for (String s : ws)
      {
        send(r, s);
      }

      send(r, Kill.KILL);

      return Shutdown.SHUTDOWN;
    };

    c.define(START, a);
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

    c.setFilter(Logger.ACTIONS | Logger.EXECUTION | Logger.DATASAVED);

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new ThreadCoordinator(new Sender()));

    c.start();
  }
}
