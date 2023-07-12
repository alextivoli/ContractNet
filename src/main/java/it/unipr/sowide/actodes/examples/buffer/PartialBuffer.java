package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.actor.CaseFactory;

/**
 *
 * The {@code PartialBuffer} class defines a behavior that manages a
 * buffer with a partial occupation. This behavior can move to either
 * the {@code EmptyBuffer} or the {@code FullBuffer} behavior.
 *
 * @see EmptyBuffer
 * @see FullBuffer
 *
**/

public final class PartialBuffer extends Buffer
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * @param q  the queue of the buffer.
   *
  **/
  public PartialBuffer(final BufferQueue q)
  {
    super(q);
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    c.define(GETPATTERN, this.getCase);
    c.define(PUTPATTERN, this.putCase);
    c.define(KILL, DESTROYER);
  }
}
