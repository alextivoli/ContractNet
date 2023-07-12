package it.unipr.sowide.actodes.examples.persistence;

import java.util.Random;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.passive.PersistentScheduler;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.persistence.FileStorer;

/**
 *
 * The {@code Master} class defines a behavior that cyclically selects
 * randomly a {@code Worker} actor until the number of selected actor
 * becomes equal to the number of workers. Moreover, after the selection
 * of the worker, the master cyclically sends it a message and waits
 * for the answer until the number of sent messages becomes equal to
 * the a predefined value.
 *
 * @see Worker
 *
**/

public final class Master extends Behavior
{
  private static final long serialVersionUID = 1L;

  private int workers;
  private int messages;
  private int attempts;
  private int sent;
  private Reference[] references;
  private Reference current;
  private final Random random;

  // Alive processing handler.
  private MessageHandler process;

  /**
   * Class constructor.
   *
   * @param w  the number of workers.
   * @param m  the number of messages.
   *
  **/
  public Master(final int w, final int m)
  {
    this.workers  = w;
    this.messages = m;
    this.attempts = 0;
    this.sent     = 0;

    this.references = null;
    this.current    = null;

    this.random = new Random();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (k) -> {
      if ((this.workers > 0) && (this.messages > 0))
      {
        this.references = new Reference[this.workers];

        for (int i = 0; i < this.workers; i++)
        {
          this.references[i] = actor(new Worker());
        }

        this.current = this.references[this.random.nextInt(this.workers)];

        this.process = (n) -> {
          this.sent++;
          if (this.sent < this.messages)
          {
            future(this.current, Status.ALIVE, this.process);
          }
          else if (this.attempts < this.workers)
          {
            this.attempts++;

            this.sent = 0;

            this.current = this.references[this.random.nextInt(this.workers)];

            future(this.current, Status.ALIVE, this.process);
          }
          else
          {
            send(APP, Kill.KILL);

            return Shutdown.SHUTDOWN;
          }

          return null;
        };

        future(this.current, Status.ALIVE, this.process);
      }

      return null;
    };

    c.define(START, h);
  }

  /**
   * Starts an actor space running the master - workers example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    final int receivers = 10;
    final int messages  = 1000;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.addService(new FileStorer());

    c.setFilter(Logger.ACTIONS | Logger.STORER);

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new PersistentScheduler(new Master(receivers, messages)));

    c.start();
  }
}
