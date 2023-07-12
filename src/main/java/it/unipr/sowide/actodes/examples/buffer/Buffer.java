package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;

/**
 *
 * The {@code Buffer} class provides a partial implementation
 * of a bounded buffer based on behavior change.
 *
 * @see EmptyBuffer
 * @see FullBuffer
 * @see PartialBuffer
 *
**/

public abstract class Buffer extends Behavior
{
  private static final long serialVersionUID = 1L;

  /**
   * Message pattern for capturing the messages whose content
   * is an instance of the {@code Get} enumeration.
   *
  **/
  protected static final MessagePattern GETPATTERN =
      MessagePattern.contentPattern(new IsInstance(Get.class));

  /**
   * Message pattern for capturing the messages whose content
   * is an instance of the {@code Put} class.
   *
  **/
  protected static final MessagePattern PUTPATTERN =
      MessagePattern.contentPattern(new IsInstance(Put.class));

  /**
   * Buffer queue.
   *
  **/
  protected BufferQueue queue;

  /**
   * Manages the reception of new new element.
   *
  **/
  protected MessageHandler getCase;

  /**
   * Manages the delivery of new new element.
   *
  **/
  protected MessageHandler putCase;

  /**
   * Class constructor.
   *
   * @param q  the queue of the buffer.
   *
  **/
  public Buffer(final BufferQueue q)
  {
    this.queue = q;

    this.getCase = (m) -> {
      send(m, this.queue.remove());

      if (this.queue.size() == 0)
      {
        return new EmptyBuffer(this.queue);
      }
      else if (getClass().getName().equals(FullBuffer.class.getName()))
      {
        return new PartialBuffer(this.queue);
      }

      return null;
    };

    this.putCase = (m) -> {
      this.queue.add(((Put) m.getContent()).getElement());
      send(m, Done.DONE);

      if (this.queue.size() == this.queue.getCapacity())
      {
        return new FullBuffer(this.queue);
      }
      else if (getClass().getName().equals(EmptyBuffer.class.getName()))
      {
        return new PartialBuffer(this.queue);
      }

      return null;
    };
  }
}
