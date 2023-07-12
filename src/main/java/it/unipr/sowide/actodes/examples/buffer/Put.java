package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.interaction.Request;

/**
 *
 * The {@code Put} class defines a request that a {@code Producer} actor
 * sends to the actor managing the buffer for adding a new element to it.
 *
**/

public final class Put implements Request
{
  private static final long serialVersionUID = 1L;

  // Element for the buffer.
  private final int element;

  /**
   * Class constructor.
   *
   * @param e  the element for the buffer.
   *
  **/
  public Put(final int e)
  {
    this.element = e;
  }

  /**
   * Gets the element.
   *
   * @return the element.
   *
  **/
  public int getElement()
  {
    return this.element;
  }
}
