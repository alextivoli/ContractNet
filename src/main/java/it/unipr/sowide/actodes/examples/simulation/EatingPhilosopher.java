package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.interaction.Unlock;

/**
 *
 * The {@code EatingPhilosopher} class defines a behavior representing an
 * eating philosopher. This behavior moves to the {@code ThinkingPhilosopher}
 * behavior after a time between a predefined maximum and minimum value.
 *
**/

public class EatingPhilosopher extends Behavior
{
  private static final long serialVersionUID = 1L;

  // Messages from forks.
  private Message[] lf;
  // State.
  private PhilosopherState state;
  // Remaining eating time.
  private int remaining;

  /**
   * Class constructor.
   *
   * @param s  the philosopher state.
   * @param f  the messages received from the two forks.
   *
  **/
  public EatingPhilosopher(final PhilosopherState s, final Message[] f)
  {
    this.state = s;
    this.lf    = f;
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
      if (this.remaining ==  0)
      {
        send(this.lf[0], Unlock.UNLOCK);
        send(this.lf[1], Unlock.UNLOCK);

        return new ThinkingPhilosopher(this.state);
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
