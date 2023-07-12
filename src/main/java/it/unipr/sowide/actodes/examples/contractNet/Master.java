package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.executor.passive.PersistentScheduler;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.persistence.FileStorer;
import java.util.Scanner;

import java.util.Random;

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

  // Alive processing handler.
  private MessageHandler process;

  /**
   * Class constructor.
   *
   * @param w  the number of workers.
   * @param m  the number of messages.
   *
  **/
  public Master()
  {

  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c) {

    System.out.print("SONO IL MASTER");
  }

}
