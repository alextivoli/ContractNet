package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Lock;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code FreeFork} class defines a behavior representing a free fork.
 *
**/

public class FreeFork extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern LOCK =
      MessagePattern.contentPattern(new IsInstance(Lock.class));

  // Number of requests.
  private int requests;
  // Locking philosopher.
  private Reference reference;

  /**
   * Class constructor.
   *
  **/
  public FreeFork()
  {
    this.requests = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if (this.requests == 1)
      {
        return new BusyFork(this.reference);
      }

      this.requests = 0;

      return null;
    };

    c.define(CYCLE, a);

    a = (m) -> {
      this.requests++;

      this.reference = m.getSender();

      return null;
    };

    c.define(LOCK, a);

    a = (m) -> {
      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
