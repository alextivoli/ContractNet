package it.unipr.sowide.actodes.examples.buffer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * The {@code BufferQueue} class define the storage support of the buffer.
 *
**/

public final class BufferQueue implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Queue maintaining the element of the buffer
  private final Queue<Integer> queue;
  // Queue maximum size.
  private int capacity;

  /**
   * Class constructor.
   *
  **/
  public BufferQueue()
  {
    this.queue = new LinkedList<Integer>();
  }

  /**
   * Class constructor.
   *
   * @param c  the buffer capacity.
   *
  **/
  public BufferQueue(final int c)
  {
    this.queue = new LinkedList<Integer>();

    this.capacity = c;
  }

  /**
   * Gets the maximum number of elements that can be stored in the buffer.
   *
   * @return the number of elements.
   *
  **/
  public int getCapacity()
  {
    return this.capacity;
  }

  /**
   * Sets the maximum number of elements that can be stored in the buffer.
   *
   * @param c the number of elements.
   *
  **/
  public void setCapacity(final int c)
  {
    this.capacity = c;
  }

  /**
   * Gets the current number of elements that are stored in the buffer.
   *
   * @return the number of elements.
   *
  **/
  public int size()
  {
    return this.queue.size();
  }

  /**
   * Adds an element to the buffer.
   *
   * @param e  the element.
   *
  **/
  public void add(final Integer e)
  {
    this.queue.add(e);
  }

  /**
   * Removes an element from the buffer.
   *
   * @return the element.
   *
  **/
  public int remove()
  {
    return this.queue.remove();
  }
}
