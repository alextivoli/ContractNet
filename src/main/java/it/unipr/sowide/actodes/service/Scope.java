package it.unipr.sowide.actodes.service;

/**
 *
 * The {@code Scope} enumeration defines where a service acts.
 *
 * In particular, a service can act inside an actor space or in all the actor
 * spaces of an application. In the last case, the service providers of all
 * the actor spaces cooperate for providing the service.
 *
**/

public enum Scope
{
  /**
   * The service acts in the local actor space.
   *
  **/
  SPACE,
  /**
   * The service acts in all the actor spaces of the application.
   *
  **/
  APP;
}
