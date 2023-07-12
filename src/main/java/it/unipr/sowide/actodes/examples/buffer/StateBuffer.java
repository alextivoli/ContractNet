package it.unipr.sowide.actodes.examples.buffer;

import java.util.LinkedList;
import java.util.Queue;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.BehaviorState;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;

/**
 *
 * The {@code StateBuffer} class provides a partial implementation
 * of a bounded buffer based on state change.
 *
**/

public final class StateBuffer extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern GETPATTERN =
      MessagePattern.contentPattern(new IsInstance(Get.class));

  private static final MessagePattern PUTPATTERN =
      MessagePattern.contentPattern(new IsInstance(Put.class));

  // Queue maintaining the element of the buffer
  private final Queue<Integer> queue;
  // Queue maximum size.
  private int capacity;

  private enum BufferState implements BehaviorState
  {
    EMPTY,
    PARTIAL,
    FULL;
  }

  /**
   * Class constructor.
   *
   * @param c  the buffer capacity.
   *
  **/
  public StateBuffer(final int c)
  {
    super(BufferState.EMPTY, BufferState.values());

    this.queue    = new LinkedList<Integer>();
    this.capacity = c;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      send(m, this.queue.remove());

      if (this.queue.size() == 0)
      {
        setState(BufferState.EMPTY);
      }
      else if (getState() == BufferState.FULL)
      {
        setState(BufferState.PARTIAL);
      }

      return null;
    };

    c.define(GETPATTERN, h, BufferState.FULL, BufferState.PARTIAL);

    h = (m) -> {
      this.queue.add(((Put) m.getContent()).getElement());
      send(m, Done.DONE);

      if (this.queue.size() == this.capacity)
      {
        setState(BufferState.FULL);
      }
      else if (getState() == BufferState.EMPTY)
      {
        setState(BufferState.PARTIAL);
      }

      return null;
    };

    c.define(PUTPATTERN, h, BufferState.EMPTY, BufferState.PARTIAL);

    c.define(KILL, DESTROYER);
  }
}
