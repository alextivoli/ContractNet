package it.unipr.sowide.actodes.service;

import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;

/**
 *
 * The {@code SimpleService} abstract class provides a partial implementation
 * of a service that an actor space can provide.
 *
 * In particular, it defines the method that implements the service
 * and the message pattern identifying the messages that
 * must be processed by such a service.
 *
**/

public abstract class SimpleService implements Service, MessageHandler
{
  private static final long serialVersionUID = 1L;

  private final MessagePattern[] patterns;

  /**
   * Class constructor.
   *
   * @param p  the message patterns associated with the service.
   *
   * These patterns allow to trigger the same service
   * for a set of different types of message.
   *
  **/
  public SimpleService(final MessagePattern... p)
  {
    this.patterns =  p;
  }

  /**
   * Gets the service message patterns.
   *
   * @return the patterns.
   *
  **/
  public MessagePattern[] getPatterns()
  {
    return this.patterns;
  }
}
