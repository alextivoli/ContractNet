package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;

/**
 *
 * The {@code ThinkingPhilosopher} class defines a behavior representing a
 * thinking philosopher. This behavior moves to the {@code HungryPhilosopher}
 * behavior after a time between a predefined maximum and minimum value.
 *
**/

public class ThinkingPhilosopher extends Behavior
{
  private static final long serialVersionUID = 1L;

  // State.
  private PhilosopherState state;
  // Remaining eating time.
  private int remaining;

  /**
   * Class constructor.
   *
   * @param s  the philosopher state.
   *
  **/
  public ThinkingPhilosopher(final PhilosopherState s)
  {
    this.state = s;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      this.remaining = this.state.getThinkingTime();

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      if (this.remaining == 0)
      {
        return new HungryPhilosopher(this.state);
      }
      else
      {
        this.remaining--;
      }

      return null;
    };

    c.define(CYCLE, a);
  }
}
