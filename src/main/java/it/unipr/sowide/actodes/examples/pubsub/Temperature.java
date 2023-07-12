package it.unipr.sowide.actodes.examples.pubsub;

import java.io.Serializable;

/**
 *
 * The {@code Temperature} class is used for informing about the the value
 * of the current temperature.
 *
**/

public final class Temperature implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Temperature value.
  private final float value;


  /**
   * Class constructor.
   *
   * @param t  the temperature value.
   *
  **/
  public Temperature(final float t)
  {
    this.value = t;
  }

  /**
   * Gets the temperature value.
   *
   * @return the value.
   *
  **/
  public float getValue()
  {
    return this.value;
  }
}
