package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.actor.CaseFactory;

/**
 *
 * The {@code FullBuffer} class defines a behavior that represents a full
 * buffer. This behavior can move to either the {@code EmptyBuffer} or the
 * {@code PartialBuffer} behavior.
 *
 * @see EmptyBuffer
 * @see PartialBuffer
 *
**/

public final class FullBuffer extends Buffer
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * @param q  the queue of the buffer.
   *
  **/
  public FullBuffer(final BufferQueue q)
  {
    super(q);
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    c.define(GETPATTERN, this.getCase);
    c.define(KILL, DESTROYER);
  }
}
