package it.unipr.sowide.actodes.examples.buffer;

import java.util.Random;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Producer} class defines a behavior that adds
 * elements to the buffer.
 *
**/

public final class Producer extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Buffer reference;
  private Reference buffer;
  // Random numbers generator
  private final Random random;
  // Producing handler.
  private MessageHandler produce;

  /**
   * Class constructor.
   *
   * @param r  the buffer reference.
   *
  **/
  public Producer(final Reference r)
  {
    this.buffer = r;

    this.random = new Random();
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    this.produce = (m) -> {
      future(this.buffer, new Put(this.random.nextInt()), this.produce);

      return null;
    };

    MessageHandler h = (m) -> {
      future(this.buffer, new Put(0), this.produce);

      return null;
    };

    c.define(START, h);

    c.define(KILL, DESTROYER);
  }
}
