package it.unipr.sowide.actodes.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.Service;

/**
 *
 * The {@code SpaceInfo} class maintains the information
 * about the current state of the actor space.
 *
**/

public final class SpaceInfo
{
  private final Configuration configuration;

  /**
   * The SpaceInfo instance.
   *
  **/
  public static final SpaceInfo INFO = new SpaceInfo();

  // Class constructor.
  private SpaceInfo()
  {
    this.configuration = new Configuration();
  }

  // Actor space services.
  private Set<String> services = null;

  /**
   * Gets the actor space configuration.
   *
   * @return the instance.
   *
  **/
  public Configuration getConfiguration()
  {
    return this.configuration;
  }

  /**
   * Gets the references of the service providers of the connected
   * actor spaces.
   *
   * @return the references.
   *
   * The references are contained in an immutable set.
   *
  **/
  public Set<Reference> getProviders()
  {
    return Collections.unmodifiableSet(
        Controller.CONTROLLER.getDispatcher().providers());
  }

  /**
   * Gets the reference of the service provider of the actor space
   * giving the reference of an actor of such an actor space.
   *
   * @param r  the actor reference.
   *
   * @return the service provider reference.
   *
   * Note that the return value is <code>null</code> if such an actor is
   * no more connected with the current actor space.
   *
  **/
  public Reference getProvider(final Reference r)
  {
    for (Reference e : Controller.CONTROLLER.getDispatcher().providers())
    {
      if (r.getLocation().equals(e.getLocation()))
      {
        return e;
      }
    }

    return null;
  }

  /**
   * Gets the references of the actor executors of the connected actor spaces.
   *
   * @return the references.
   *
  **/
  public Set<Reference> getExecutors()
  {
    return Collections.unmodifiableSet(
        Controller.CONTROLLER.getDispatcher().executors());
  }

  /**
   * Gets the reference of the actor executor of the
   * actor space giving the reference of an its actor.
   *
   * @param r  the actor reference.
   *
   * @return the actor executor reference.
   *
   * Note that the return value is <code>null</code> if such an actor is
   * no more connected with the current actor space.
   *
  **/
  public Reference getExecutor(final Reference r)
  {
    for (Reference e : Controller.CONTROLLER.getDispatcher().executors())
    {
      if (r.getLocation().equals(e.getLocation()))
      {
        return e;
      }
    }

    return null;
  }

  /**
   * Gets the local broadcast references of the connected actor spaces.
   *
   * @return the references.
   *
   * The references are contained in an immutable set.
   *
  **/
  public Set<Reference> getBroadcasts()
  {
    return Collections.unmodifiableSet(
        Controller.CONTROLLER.getDispatcher().lbroadcasts());
  }

  /**
   * Gets the local broadcast reference of the
   * actor space giving the reference of an its actor.
   *
   * @param r  the actor reference.
   *
   * @return the local broadcast reference.
   *
   * Note that the return value is <code>null</code> if such an actor is
   * no more connected with the current actor space.
   *
  **/
  public Reference getBroadcast(final Reference r)
  {
    for (Reference e : Controller.CONTROLLER.getDispatcher().lbroadcasts())
    {
      if (r.getLocation().equals(e.getLocation()))
      {
        return e;
      }
    }

    return null;
  }

  /**
   * Gets the reference of the service provider of the actor space
   * acting as communication broker.
   *
   * @return the reference.
   *
   * Note that it returns the local service provider when
   * the actor space is not connected with other actor spaces.
   *
  **/
  public Reference getBroker()
  {
    if (Controller.CONTROLLER.getDispatcher().getBroker() != null)
    {
      return Controller.CONTROLLER.getDispatcher().getBroker();
    }

    return Behavior.PROVIDER;
  }

  /**
   * Gets the number of actors running in the actor space.
   *
   * @return  the number of actors.
   *
  **/
  public int getPopulation()
  {
    return Controller.CONTROLLER.getRegistry().size();
  }

  /**
   * Gets the class qualified names of the services provided
   * by the actor space.
   *
   * @return the class qualified names.
   *
   * The class qualified names are contained in an immutable set.
   *
  **/
  public Set<String> getServices()
  {
    if (this.services == null)
    {
      this.services = new HashSet<>();

      for (Service s : this.configuration.getServices())
      {
        this.services.add(s.getClass().getName());
      }

      this.services = Collections.unmodifiableSet(this.services);
    }

    return this.services;
  }
}
