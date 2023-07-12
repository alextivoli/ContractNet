package it.unipr.sowide.actodes.controller;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.distribution.Connector;
import it.unipr.sowide.actodes.distribution.Dispatcher;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.ExecutorActor;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Start;
import it.unipr.sowide.actodes.interaction.Stop;
import it.unipr.sowide.actodes.provider.Provider;
import it.unipr.sowide.actodes.provider.ProviderActor;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.registry.Registry;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.StorerWrapper;
import it.unipr.sowide.actodes.service.persistence.Storer;

/**
 *
 * The {@code Controller} class manages the execution of an actor space.
 *
**/

public final class Controller implements Runnable
{
  private static final String EXNAME = "EXECUTOR";
  private static final String PRNAME = "PROVIDER";
  private static final String SPNAME = "SPACE";
  private static final String APNAME = "APP";

  /**
   * Controller instance.
   *
  ^*/
  public static final Controller CONTROLLER = new Controller();

  // Registry instance.
  private Registry registry;
  // Dispatcher instance.
  private Dispatcher dispatcher;
  // Persistent storage manager instance.
  private Storer storer;
  // Connector instance.
  private Connector connector;
  // Executor behavior Instance.
  private ExecutorActor executor;
  // Provider behavior instance.
  private Provider pBehavior;
  // Actor provider instance.
  private ProviderActor pActor;
  // Runtime running flag.
  private boolean running;
  // Shutdown enabled.
  private boolean shutdown;

  // Class constructor.
  private Controller()
  {
    this.registry   = null;
    this.dispatcher = null;
    this.storer     = null;
    this.connector  = null;
    this.executor   = null;
    this.pBehavior  = null;
    this.pActor     = null;
    this.running    = false;
    this.shutdown   = false;
  }

  /**
   * Runs the controller.
   *
  **/
  @Override
  public void run()
  {
    try
    {
      Configuration c = SpaceInfo.INFO.getConfiguration();

      if (c.getSnapshot() == null)
      {
        if (!new Configurator(c).build())
        {
          return;
        }

        configure(c);
      }
      else
      {
        load();
      }

      String l = c.getLocation();

      ((SpecialReference) Behavior.PROVIDER).configure(
          Reference.address(PRNAME, l), this.pActor);
      ((SpecialReference) Behavior.EXECUTOR).configure(
          Reference.address(EXNAME, l), this.executor);
      ((SpecialReference) Behavior.SPACE).configure(
          Reference.address(SPNAME, l), this.pActor);
      ((SpecialReference) Behavior.APP).configure(
          Reference.address(APNAME, l), this.pActor);

      this.registry.start();

      if (this.storer != null)
      {
        this.storer.start();
      }

      this.pActor.init();

      this.dispatcher.start();

      this.running = true;

      long t = System.nanoTime();

      Logger.LOGGER.logConfiguration();

      this.executor.run();

      Logger.LOGGER.logEndExecution(t);
      Logger.LOGGER.shutdown();

      this.dispatcher.shutdown();
      this.registry.shutdown();
      this.storer.shutdown();
    }
    catch (Exception f)
    {
      ErrorManager.notify(f);
    }
  }

  // Configures the actor space.
  private void configure(final Configuration c)
  {
    this.storer     = c.getStorer();
    this.registry   = c.getRegistry();
    this.dispatcher = c.getDispatcher();
    this.connector  = c.getConnector();
    this.executor   = c.getExecutor();
    this.pActor     = c.getProvider();
    this.pBehavior  = (Provider) this.pActor.getBehavior();
  }

  // Loads the state of the actor space from a persistent storage.
  private void load()
  {
    Configuration c = SpaceInfo.INFO.getConfiguration();

    this.storer = c.getStorer();

    Configuration rc = this.storer.retrieve(c.getSnapshot());

    if ((Logger.LOGGER.getFilter() & Logger.STORER) > 0)
    {
      this.storer = new StorerWrapper(this.storer);
    }

    configure(rc);

    ((SpecialReference) this.executor.getReference()).restore(this.executor);
    ((SpecialReference) this.pActor.getReference()).restore(this.pActor);
  }

  /**
   * Asks the executor to stop the execution of the actor space.
   *
   * This task is done by delegating the sending of the request to
   * the service provider.
   *
   * Note that an executor may not be able to manage this request.
   *
  **/
  public void stop()
  {
    this.pBehavior.send(Behavior.EXECUTOR, Stop.STOP);
  }

  /**
   * Asks the executor to start the execution of the actor space after
   * its stop.
   *
   * This task is done by delegating the sending of the request to
   * the service provider.
   *
   * Note that an executor may not be able to manage this request.
   *
  **/
  public void start()
  {
    this.pBehavior.send(Behavior.EXECUTOR, Start.START);
  }

  /**
   * Saves the state of the actor space in a persistent storage when
   * its execution is stopped.
   *
   * @param i  the actor space persistent storage identifier.
   *
   * @return <code>true</code> for a successful operation.
   *
  **/
  public boolean save(final String i)
  {
    if (getExecutor().isStopped() && (this.storer != null))
    {
      return this.storer.save(SpaceInfo.INFO.getConfiguration(), i);
    }

    return false;
  }

  /**
   * Gets the registry.
   *
   * @return  the registry.
   *
  **/
  public Registry getRegistry()
  {
    return this.registry;
  }

  /**
   * Gets the dispatcher.
   *
   * @return  the dispatcher.
   *
  **/
  public Dispatcher getDispatcher()
  {
    return this.dispatcher;
  }

  /**
   * Gets the persistent storage manager.
   *
   * @return the persistent storage manager.
   *
  **/
  public Storer getStorer()
  {
    return this.storer;
  }

  /**
   * Gets the connector.
   *
   * @return  the connector.
   *
  **/
  public Connector getConnector()
  {
    return this.connector;
  }

  /**
   * Gets the behavior of the service provider.
   *
   * @return  the behavior.
   *
  **/
  public Behavior getProvider()
  {
    return this.pBehavior;
  }

  /**
   * Gets the current behavior of the actor executor.
   *
   * @return  the current behavior.
   *
  **/
  public Executor<?> getExecutor()
  {
    return (Executor<?>) this.executor.getBehavior();
  }

  /**
   * Checks if the runtime is running.
   *
   * @return <code>true</code> if the runtime is running.
   */
  public boolean isRunning()
  {
    return this.running;
  }

  /**
   * Notifies that the actor space is empty.
   *
  **/
  public void notifyEmpty()
  {
    if ((this.dispatcher != null)
        && ((this.dispatcher.providers().size() == 0) || this.shutdown))
    {
      this.shutdown = true;

      this.pBehavior.send(Behavior.PROVIDER, Kill.KILL);
    }
  }

  /**
   * Notifies the arrival of a request to shutdown the actor space.
   *
  **/
  public void shutdown()
  {
    this.shutdown = true;

    if (this.registry.size() == 0)
    {
      this.pBehavior.send(Behavior.PROVIDER, Kill.KILL);
    }
  }

  /**
   * Checks if the shutdown of the actor space is under execution.
   *
   * @return <code>true</code> if the shutdown is under execution.
   */
  public boolean getRequest()
  {
    return this.shutdown;
  }
}
