package it.unipr.sowide.actodes.examples.buffer;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Consumer} class defines a behavior that removes elements
 * from the buffer.
 *
**/

public final class Consumer extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Buffer reference.
  private Reference buffer;
  // Consuming handler.
  private MessageHandler consume;

  /**
   * Class constructor.
   *
   * @param r  the buffer reference.
   *
  **/
  public Consumer(final Reference r)
  {
    this.buffer = r;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    this.consume = (m) -> {
      future(this.buffer, Get.GET, this.consume);

      return null;
    };

    MessageHandler h = (m) -> {
      future(this.buffer, Get.GET, this.consume);

      return null;
    };

    c.define(START, h);

    c.define(KILL, DESTROYER);
  }
}
