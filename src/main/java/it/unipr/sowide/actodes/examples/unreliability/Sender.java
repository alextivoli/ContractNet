package it.unipr.sowide.actodes.examples.unreliability;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

/**
 *
 * The {@code Sender} class defines a behavior that sends a certain number
 * of messages to a {@code Responder} actor and then kills itself.
 *
 * @see Responder
 *
**/

public final class Sender extends Behavior
{
  private static final long serialVersionUID = 1L;

  protected static final MessagePattern DONEPATTERN =
      MessagePattern.contentPattern(new IsInstance(Done.class));

  // Number of messages.
  private int messages;
  // Timeout (in milliseconds).
  private long timeout;
  // Alive processing handler.
  private MessageHandler pAlive;
  //
  private Reference responder;

  /**
   * Class constructor.
   *
   * @param n  the number of messages.
   *
   * @param t
   *
   * the time (in milliseconds) to wait before sending a new copy of a message.
   *
  **/
  public Sender(final int n, final long t)
  {
    this.messages  = n;
    this.timeout   = t;
    this.responder = null;
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if (this.messages > 0)
      {
        this.responder = actor(new Responder());

        this.pAlive = (k) -> {
          if (k.getContent().equals(Done.DONE))
          {
            this.messages--;

            if (this.messages > 0)
            {
              future(this.responder, Status.ALIVE, this.timeout, this.pAlive);
            }
            else
            {
              send(k.getSender(), Kill.KILL);

              return Shutdown.SHUTDOWN;
            }
          }
          else
          {
            future(responder, Status.ALIVE, this.timeout, this.pAlive);
          }

          return null;
        };

        future(responder, Status.ALIVE, this.timeout, this.pAlive);
      }

      return null;
    };

    c.define(START, a);
  }

  /**
   * Starts an actor space running the unreliable communication example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    final int messages = 10;
    final long timeout = 100;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new OldScheduler(new Sender(messages, timeout)));

    c.start();
  }
}
