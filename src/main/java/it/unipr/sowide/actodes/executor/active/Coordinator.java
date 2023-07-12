package it.unipr.sowide.actodes.executor.active;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.active.ActiveActor;
import it.unipr.sowide.actodes.configuration.Builder;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.Termination;
import it.unipr.sowide.actodes.interaction.Kill;

/**
 *
 * The {@code Coordinator} class provides a partial implementation
 * of the behavior of the executor actor used for creating and executing
 * active actors.
 *
**/

public abstract class Coordinator extends Executor<ActiveActor>
                                  implements Runnable
{
  private static final long serialVersionUID = 1L;

  // Default period between successive termination condition checking.
  private static final long PERIOD = 10;

  private Termination termination;
  private long time;
  private long period;

  /**
   * Class constructor.
   *
  **/
  protected Coordinator()
  {
    this.termination = null;

    this.period = PERIOD;
  }

  /**
   * Class constructor.
   *
   * @param t  the simulation termination condition.
   *
  **/
  protected Coordinator(final Termination t)
  {
    this.termination = t;

    this.period = PERIOD;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   *
  **/
  protected Coordinator(final Behavior b)
  {
    super(b);

    termination = null;
  }

  /**
   * Class constructor.
   *
   * @param b  the behavior of the actor starting the application.
   * @param t  the simulation termination condition.
   *
  **/
  protected Coordinator(final Behavior b, final Termination t)
  {
    super(b);

    this.termination = t;

    this.period = PERIOD;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actors of the application.
   *
  **/
  protected Coordinator(final Builder b)
  {
    super(b);

    this.termination = null;
  }

  /**
   * Class constructor.
   *
   * @param b
   *
   * the builder creating the initial set of actors of the application.
   *
   * @param t  the simulation termination condition.
   *
  **/
  protected Coordinator(final Builder b, final Termination t)
  {
    super(b);

    this.termination = t;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      build();
      manage();

      this.time = System.nanoTime();

      return null;
    };

    c.define(Behavior.START, a);

    c.define(Behavior.KILL, KILLER);
  }

  /**
   * Runs the termination checker.
   *
  **/
  public void run()
  {
    while (!this.termination.eval(this))
    {
      try
      {
        Thread.sleep(this.period);
      }
      catch (InterruptedException e)
      {
        continue;
      }
    }

    closeAll();
    send(PROVIDER, Kill.KILL);
  }

  /**
   * Gets the period between successive termination condition checking.
   *
   * @return the period.
   *
   * Note that the time unit corresponds to a millisecond.
   *
  **/
  public long getPeriod()
  {
    return this.period;
  }

  /**
   * Sets the period between successive termination condition checking.
   *
   * @param p  the period.
   *
   * Note that the time unit corresponds to a millisecond.
   *
  **/
  public void setPeriod(final long p)
  {
    this.period = p;
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final Actor a)
  {
  }

  /** {@inheritDoc} **/
  @Override
  protected void stopAll()
  {
    for (Actor a : Controller.CONTROLLER.getRegistry().actors())
    {
      ((ActiveActor) a).stopAll();
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void manage()
  {
    if ((this.termination != null) && !isEmpty())
    {
      new Thread(this).start();
    }
  }

  /** {@inheritDoc} **/
  @Override
  protected void startAll()
  {
    for (Actor a : Controller.CONTROLLER.getRegistry().actors())
    {
      add(a);
    }
  }


  /** {@inheritDoc} **/
  @Override
  protected void closeAll()
  {
    stopAll();

    for (Actor a : Controller.CONTROLLER.getRegistry().actors())
    {
      a.shutdown();
    }

  }

  /** {@inheritDoc} **/
  @Override
  public ActiveActor newActor(final Behavior b) throws Exception
  {
    return new ActiveActor();
  }

  /** {@inheritDoc} **/
  @Override
  public boolean isEmpty()
  {
    return Controller.CONTROLLER.getRegistry().actors().isEmpty();
  }

  /** {@inheritDoc} **/
  @Override
  public long time()
  {
    return System.nanoTime() - this.time;
  }
}
