package it.unipr.sowide.actodes.examples.simulation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Lock;
import it.unipr.sowide.actodes.interaction.Unlock;

/**
 *
 * The {@code HungryPhilosopher} class defines a behavior representing a
 * hungry philosopher. This behavior moves to the {@code EatingPhilosopher}
 * behavior when it gets both the two forks.
 *
**/

public class HungryPhilosopher extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern DONE =
      MessagePattern.contentPattern(new IsInstance(Done.class));

  // Messages from forks.
  private Message[] lf;
  // State.
  private PhilosopherState state;
  // Bumber of messages from forks.
  private int answers;

  /**
   * Class constructor.
   *
   * @param s  the philosopher state.
   *
  **/
  public HungryPhilosopher(final PhilosopherState s)
  {
    this.state = s;

    this.lf = new Message[2];

    this.answers = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      this.state.getForks().forEach(r -> send(r, Lock.LOCK));

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      if (this.answers ==  2)
      {
        return new EatingPhilosopher(this.state, this.lf);
      }
      else if (this.answers ==  1)
      {
        send(this.lf[0], Unlock.UNLOCK);
      }
      else
      {
        if (this.state.getCatchForks())
        {
          this.state.getForks().forEach(r -> send(r, Lock.LOCK));
        }
      }

      this.answers = 0;

      return null;
    };

    c.define(CYCLE, a);

    a = (m) -> {
      this.lf[this.answers] = m;

      this.answers++;

      return null;
    };

    c.define(DONE, a);
  }
}
