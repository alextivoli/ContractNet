package it.unipr.sowide.actodes.examples.fibonacci;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.passive.OldScheduler;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.service.logging.ConsoleWriter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.util.NoCycleProcessing;

/**
 *
 * The {@code Fibonacci} class defines a behavior whose scope is to generate
 * the value of one of the Fibonacci sequence numbers thanks to the
 * cooperation of some actors.
 *
**/

public final class Fibonacci extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern PATTERN =
      MessagePattern.contentPattern(new IsInstance(Integer.class));

  private static final String FS = "The Fibonacci number of %s is %s!\n";

  // Fibonacci sequence element.
  private int element;
  // Current result.
  private int result;

  /**
   * Class constructor.
   *
   * @param n  the seed number.
   *
  **/
  public Fibonacci(final int n)
  {
    this.element = n;
    this.result  = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if (this.element <= 1)
      {
        if (!getParent().equals(EXECUTOR))
        {
          send(getParent(), this.element);
        }
        else
        {
          System.out.format(FS, this.element, this.element);
        }

        return Shutdown.SHUTDOWN;
      }
      else
      {
        int i = this.element - 1;

        actor(new Fibonacci(i--));
        actor(new Fibonacci(i));
      }

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      int p = (int) m.getContent();

      if (this.result == 0)
      {
        this.result = p;
      }
      else
      {
        if (!getParent().equals(EXECUTOR))
        {
          send(getParent(), this.result + p);
        }
        else
        {
          System.out.format(FS, this.element, this.result + p);
        }

        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(PATTERN, a);
  }

  /**
   * Starts an actor space running the Fibonacci example.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    final int number = 7;

    Configuration c =  SpaceInfo.INFO.getConfiguration();

    c.setFilter(Logger.ACTIONS | Logger.EXECUTION);
    c.setLogFilter(new NoCycleProcessing());

    c.addWriter(new ConsoleWriter());

    c.setExecutor(new OldScheduler(new Fibonacci(number)));

    c.start();
  }
}
