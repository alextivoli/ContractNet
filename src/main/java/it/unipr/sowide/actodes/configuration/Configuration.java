package it.unipr.sowide.actodes.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Handler;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.distribution.Connector;
import it.unipr.sowide.actodes.distribution.Dispatcher;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.ExecutorActor;
import it.unipr.sowide.actodes.provider.ProviderActor;
import it.unipr.sowide.actodes.registry.Registry;
import it.unipr.sowide.actodes.service.CompositeService;
import it.unipr.sowide.actodes.service.Service;
import it.unipr.sowide.actodes.service.SimpleService;
import it.unipr.sowide.actodes.service.logging.LogFilter;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.logging.Writer;
import it.unipr.sowide.actodes.service.persistence.Storer;

/**
 *
 * The {@code Configuration} class manages the information about
 * the configuration of an actor space.
 *
**/

public final class Configuration extends PropertiesGroup
{
  private static final long serialVersionUID = 1L;

  private static final String HOME    = ".";
  private static final Locale LOCALE  = Locale.ENGLISH;

  // Project home directory path.
  private final String home;
  // Actor address location.
  private String location;
  // Registry
  private transient Registry registry;
  // Executor actor.
  private transient ExecutorActor executor;
  // Provider actor.
  private transient ProviderActor provider;
  // Storage service.
  private transient Storer storer;
  // Provider services.
  private final Set<SimpleService> services;
  // Logging filtering value.
  private int filter;
  // Logging special filter.
  private transient LogFilter logFilter;
  // Logging writers.
  private final transient Set<Writer> writers;
  // Dispatcher.
  private transient Dispatcher dispatcher;
  // Connector.
  private transient Connector connector;

  // Current snapshot file path.
  private String snapshot;
  // Specific application properties.
  private final Map<String, PropertiesGroup> managers;

  /**
   * Class constructor.
   *
  **/
  public Configuration()
  {
    this.home = HOME;

    try
    {
      String vm = ManagementFactory.getRuntimeMXBean().getName();

      if (vm == null)
      {
        throw new NullPointerException();
      }

      if (vm.indexOf("@") > 0)
      {
        vm = vm.substring(0, vm.indexOf("@"));
      }

      String ip = InetAddress.getLocalHost().getHostAddress();

      this.location = vm + "@" + ip;
    }
    catch (UnknownHostException e)
    {
      ErrorManager.kill(e);
    }
    catch (NullPointerException e)
    {
      ErrorManager.kill(e);
    }

    this.registry   = null;
    this.executor   = null;
    this.provider   = new ProviderActor();
    this.services   = new HashSet<>();
    this.filter     = 0;
    this.logFilter  = null;
    this.writers    = new HashSet<>();
    this.dispatcher = null;
    this.connector  = null;
    this.storer     = null;
    this.snapshot   = null;
    this.managers   = new HashMap<>();
  }

  /**
   * Loads a set of properties from the command-line arguments.
   *
   * @param v  the command-line arguments.
   *
   * @return <code>true</code> if a set of properties is loaded.
   *
  **/
  public boolean load(final String[] v)
  {
    if ((v.length > 1) && (v[0].equals("-cfg")))
    {
      return loadProperties(v[1]);
    }

    return false;
  }

  /**
   * Gets the project home directory path.
   *
   * @return the absolute path.
   *
  **/
  public String getHome()
  {
    return this.home;
  }



  /**
   * Gets the actors address location.
   *
   * @return the location.
   *
   * @see it.unipr.sowide.actodes.registry.Reference#getLocation()
   *
  **/
  public String getLocation()
  {
    return this.location;
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
   * Sets the registry.
   *
   * @param r  the registry.
   *
  **/
  public void setRegistry(final Registry r)
  {
    this.registry = r;
  }

  /**
   * Gets the executor actor.
   *
   * @return actor.
   *
  **/
  public ExecutorActor getExecutor()
  {
    return this.executor;
  }

  /**
   * Sets the executor behavior.
   *
   * @param e  the behavior.
   *
  **/
  public void setExecutor(final Executor<? extends Actor> e)
  {
    this.executor = new ExecutorActor();

    this.executor.configure(Behavior.PROVIDER, Behavior.EXECUTOR, e);
    e.configure(this.executor);
  }

  /**
   * Gets the provider actor.
   *
   * @return the actor.
   *
  **/
  public ProviderActor getProvider()
  {
    return this.provider;
  }

  /**
   * Gets the actor space services.
   *
   * @return the services.
   *
  **/
  public Set<SimpleService> getServices()
  {
    return this.services;
  }

  /**
   * Adds a service to an actor space service.
   *
   * @param s  the service.
   *
  **/
  public void addService(final Service s)
  {
    if (s instanceof SimpleService)
    {
      this.services.add((SimpleService) s);
    }
    else if (s instanceof CompositeService)
    {
      for (Service p : ((CompositeService) s).getServices())
      {
        addService(p);
      }
    }
    else if (s instanceof Logger)
    {
      return;
    }
    else if (s instanceof Storer)
    {
      this.storer = (Storer) s;
    }
  }

  /**
   * Checks the presence of an actor space service.
   *
   * @param n  the service class qualified name.
   *
   * @return <code>true</code> if the service is present.
   *
  **/
  public boolean containsService(final String n)
  {
    for (Service s : this.services)
    {
      if (n.equals(s.getClass().getName()))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets the persistent storage service.
   *
   * @return the service.
   *
  **/
  public Storer getStorer()
  {
    return this.storer;
  }

  /**
   * sets the persistent storage service.
   *
   * @param s  the service.
   *
  **/
  public void setStorer(final Storer s)
  {
    this.storer = s;
  }

  /**
   * Gets the logging filtering value.
   *
   * @return  the value.
   *
  **/
  public int getFilter()
  {
    return this.filter;
  }

  /**
   * Sets the logging filtering value.
   *
   * @param v  the value.
   *
  **/
  public void setFilter(final int v)
  {
    this.filter = v;
  }

  /**
   * Gets the logging special filter.
   *
   * @return  special filter.
   *
  **/
  public LogFilter getLogFilter()
  {
    return this.logFilter;
  }

  /**
   * Sets the logging special filter.
   *
   * @param f  special filter.
   *
  **/
  public void setLogFilter(final LogFilter f)
  {
    this.logFilter = f;
  }

  /**
   * Gets the configurations of the logging writers.
   *
   * @return the configurations.
   *
  **/
  public Set<Writer> getWriters()
  {
    return this.writers;
  }

  /**
   * Adds ta logging writer.
   *
   * @param w  the writer.
   *
  **/
  public void addWriter(final Writer w)
  {
    this.writers.add(w);
  }

  /**
   * Builds the logging service.
   *
  **/
  public void buildLogger()
  {
    if (this.filter > 0)
    {
      Logger.LOGGER.setFilter(this.filter);

      for (Writer w : this.writers)
      {
        if (this.logFilter != null)
        {
          ((Handler) w).setFilter(this.logFilter);
        }

        Logger.LOGGER.addWriter(w);
      }
    }
  }

  /**
   * Gets the dispatcher.
   *
   * @return the dispatcher
   *
  **/
  public Dispatcher getDispatcher()
  {
    return this.dispatcher;
  }

  /**
   * Sets the dispatcher.
   *
   * @param d  the dispatcher
   *
  **/
  public void setDispatcher(final Dispatcher d)
  {
    this.dispatcher = d;
  }

  /**
   * Gets the connector.
   *
   * @return the connector.
   *
  **/
  public Connector getConnector()
  {
    return this.connector;
  }

  /**
   * Sets the connector.
   *
   * @param c  the connector.
   *
  **/
  public void setConnector(final Connector c)
  {
    this.connector = c;
  }

  /**
   * Sets the file path of the snapshot containing the state of
   * an actor space from which continues its execution.
   *
   * @param s  the file absolute path.
   *
  **/
  public void setSnapshot(final String s)
  {
    this.snapshot = s;
  }

  /**
   * Gets the file path of the snapshot containing the state of
   * an actor space from which continues its execution.
   *
   * @return the file absolute path.
   *
  **/
  public String getSnapshot()
  {
    return this.snapshot;
  }

  /**
   * Gets a properties group from the application configuration.
   *
   * @param k  the key identifying properties group.
   *
   * @return <code>true</code> if the operation is successful.
   *
  **/
  public PropertiesGroup getPropertiesGroup(final String k)
  {
    PropertiesGroup p = this.managers.get(k);

    if (p != null)
    {
      return p;
    }

    String f = getString(k);

    if (f != null)
    {
      try
      {
        File e = new File(Paths.get(f).toString());

        Properties c = new Properties();

        Map<String, Serializable> map = new HashMap<>();

        c.load(new FileInputStream(e));

        for (String s : c.stringPropertyNames())
        {
          map.put(s, (Serializable) c.get(s));
        }

        PropertiesGroup g = new PropertiesGroup(map);

        this.managers.put(k, g);

        return g;
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }
    }

    return null;
  }

  /**
   * Loads a set of properties from a properties file
   * in the application configuration.
   *
   * @param f  the properties file path.
   *
   * @return <code>true</code> if the operation is successful.
   *
   * Note that an application can contain specific properties groups
   * maintaining the information for configuring a specific object
   * (e.g., a behavior). Each properties group is identified in the
   * properties file with a property whose value it the path of the
   * properties file maintaining the information of such a properties
   * group.
   *
   * Moreover, the properties file, defining the application
   * configuration, can contain a property named "properties.groups
   * and whose value of this property contains the list of the property
   * keys (separated by commas) identifying a set of properties groups.
   *
   * If this property is present, the loading operation also loads the
   * declared properties groups. If it is not present, each properties
   * group can be loaded in the application configuration by using the
   * related key.
   *
   * @see PropertiesGroup
   *
  **/
  @Override
  public boolean loadProperties(final String f)
  {
    if (super.loadProperties(f))
    {
      String s = getString("properties.groups");

      if (s != null)
      {
        for (String k : s.split(","))
        {
          if (getPropertiesGroup(k) == null)
          {
            return false;
          }
        }
      }

      return true;
    }

    return false;
  }

  /**
   * Configures the actor space and starts its execution.
   *
  **/
  public void start()
  {
    Controller.CONTROLLER.run();
  }
}
