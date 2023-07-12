package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code BusyFork} class defines a behavior representing a fork
 * in a hand of a philosopher.
 *
**/

public class BusyFork extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Locking philosopher.
  private Reference reference;
  //Unlocking flag.
  private boolean unlock;

  /**
   * Class constructor.
   *
   * @param r  the philosopher reference.
   *
  **/
  public BusyFork(final Reference r)
  {
    this.reference = r;

    this.unlock = false;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      this.unlock = true;

      return null;
    };

    MessageHandler h = (m) -> {
      future(this.reference, Done.DONE, a);

      return null;
    };

    c.define(START, h);

    h = (m) -> {
      if (this.unlock)
      {
        return new FreeFork();
      }

      return null;
    };

    c.define(CYCLE, h);

    h = (m) -> {
      return null;
    };

    c.define(ACCEPTALL, h);
  }
}
