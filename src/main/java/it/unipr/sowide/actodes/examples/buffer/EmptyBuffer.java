package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.actor.CaseFactory;

/**
 *
 * The {@code EmptyBuffer} class defines a behavior that represents an empty
 * buffer. This behavior can move to either the {@code FullBuffer} or the
 * {@code PartialBuffer} behavior.
 *
 * @see FullBuffer
 * @see PartialBuffer
 *
**/

public final class EmptyBuffer extends Buffer
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * @param q  the queue of the buffer.
   *
  **/
  public EmptyBuffer(final BufferQueue q)
  {
    super(q);
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    c.define(PUTPATTERN, this.putCase);
    c.define(KILL, DESTROYER);
  }
}
