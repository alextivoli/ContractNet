package it.unipr.sowide.actodes.error;

import java.util.Locale;
import java.util.ResourceBundle;

import it.unipr.sowide.actodes.configuration.Configuration;

/**
 *
 * The {@code ConfigurationInfo} enumeration allows the retrieval
 * of the information associated with the possible types of configuration
 * exceptions thrown by an ActoDeS application.
 *
**/

public enum ErrorInfo
{
  /**
   * Notifies the failure during the configuration of the actor space
   * because the absence of the executor actor.
   *
  **/
  NOEXECUTOR,

  /**
   * Notifies the failure during the configuration of the actor space
   * because the registry is incompatible with the executor actor.
   *
  **/
  INCOMPATIBLEREGISTRY,

  /**
   * Notifies the failure during the configuration of the actor space
   * because a service hides another service.
   *
  **/
  HIDESERVICE,

  NOSERVICE;

  private static final ResourceBundle ERRORS;

  static
  {
    ERRORS = ResourceBundle.getBundle("it.unipr.sowide.actodes.error.errors",
        Locale.ENGLISH);
  }

  /**
   * Gets the errors bundle.
   *
   * @return the bundle.
   *
  **/
  public ResourceBundle getErrors()
  {
    return ERRORS;
  }
}
