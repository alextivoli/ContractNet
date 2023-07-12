package it.unipr.sowide.actodes.controller;

import java.io.Serializable;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.distribution.DefaultDispatcher;
import it.unipr.sowide.actodes.distribution.Dispatcher;
import it.unipr.sowide.actodes.error.ErrorInfo;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.provider.Provider;
import it.unipr.sowide.actodes.provider.ProviderActor;
import it.unipr.sowide.actodes.registry.Registry;
import it.unipr.sowide.actodes.service.Service;
import it.unipr.sowide.actodes.service.logging.DispatcherWrapper;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.RegistryWrapper;
import it.unipr.sowide.actodes.service.logging.StorerWrapper;
import it.unipr.sowide.actodes.service.persistence.FileStorer;
import it.unipr.sowide.actodes.service.persistence.Storer;

/**
 *
 * The {@code Configuration} class manages the information about
 * the configuration of an actor space.
 *
**/

public final class Configurator implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final Configuration conf;

  /**
   * Class constructor.
   *
   * @param c  the configuration instance.
   *
  **/
  public Configurator(final Configuration c)
  {
    this.conf = c;
  }

  /**
   * Configures the application.
   *
   * @return <code>true</code> if the configuration is successful.
   *
  **/
  public boolean build()
  {
    if (!Controller.CONTROLLER.isRunning())
    {
      if (this.conf.getExecutor() == null)
      {
        ErrorManager.notify(ErrorInfo.NOEXECUTOR);

        return false;
      }

      storer();

      if (!registry())
      {
        ErrorManager.notify(ErrorInfo.INCOMPATIBLEREGISTRY);

        return false;
      }

      if (!services())
      {
        ErrorManager.notify(ErrorInfo.HIDESERVICE);

        return false;
      }

      this.conf.buildLogger();

      provider();
      dispatcher();
    }

    return true;
  }

  // Which are the its users?
  private void storer()
  {
    Storer s = this.conf.getStorer();

    if (s == null)
    {
      s = new FileStorer();
    }

    if ((this.conf.getFilter() & Logger.STORER) > 0)
    {
      s = new StorerWrapper(s);
    }

    this.conf.addService(s);
  }

  private boolean registry()
  {
    if (!Controller.CONTROLLER.isRunning())
    {
      Registry r =
          ((Executor<?>) this.conf.getExecutor().getBehavior()).getRegistry();

      if (this.conf.getRegistry() != null)
      {
        if (!this.conf.getRegistry().getClass().isAssignableFrom(r.getClass()))
        {
          return false;
        }

        r = this.conf.getRegistry();
      }

      if ((this.conf.getFilter() & Logger.REGISTRY) > 0)
      {
        r = new RegistryWrapper(r);
      }

      this.conf.setRegistry(r);

      return true;
    }

    return false;
  }

  private boolean services()
  {
    for (Service s : this.conf.getServices())
    {
      for (Service c : this.conf.getServices())
      {
        if (s != c)
        {
          Class<?> sc = s.getClass();
          Class<?> cc = c.getClass();

          if ((sc.isAssignableFrom(cc)) || (cc.isAssignableFrom(sc)))
          {
            return false;
          }
        }
      }
    }

    return true;
  }

  private void provider()
  {
    Provider p = new Provider(this.conf.getServices());

    ProviderActor a = this.conf.getProvider();

    a.configure(Behavior.PROVIDER, Behavior.PROVIDER, p);
    p.configure(a);
  }

  private void dispatcher()
  {
    Dispatcher d = this.conf.getDispatcher();

    if (d == null)
    {
      d = new DefaultDispatcher();
    }

    if ((this.conf.getFilter() & Logger.DISPATCHER) > 0)
    {
      d = new DispatcherWrapper(d);
    }

    this.conf.setDispatcher(d);
  }
}
