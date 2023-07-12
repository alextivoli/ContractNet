package it.unipr.sowide.actodes.registry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpaceInfo;

/**
 *
 * The {@code Registry} class provides the default implementation of an actors
 * registry.
 *
 * A registry maintains the bindings between the references
 * and the actors of an actor space.
 *
**/

public class Registry implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * The default value for the maximum number of actors
   * that the registry can be managed.
   *
  **/
  public static final int MAX = 1000000000;

  // Actor numeric name.
  private final AtomicInteger id;
  // Actor numeric name string format.
  private String format;
  // Maximum number of created actors.
  private int max;

  /**
   * Reference - actor map.
   *
  **/
  protected ConcurrentMap<ActorReference, Actor> actors;

  /**
   * name - reference map.
   *
  **/
  private ConcurrentMap<String, ActorReference> names;

  /**
   * name - reference map.
   *
  **/
  private ConcurrentMap<Message, String> subscriptions;

  /**
   * Class constructor.
   *
   * It sets the maximum number of created actors to the
   * default value.
   *
   * @see it.unipr.sowide.actodes.registry.Registry#MAX
   *
  **/
  public Registry()
  {
    this.actors        = new ConcurrentHashMap<>();
    this.names         = new ConcurrentHashMap<>();
    this.subscriptions = new ConcurrentHashMap<>();

    this.id  = new AtomicInteger(-1);
    this.max = MAX;

    this.format = "%0" + (String.valueOf(this.max).length() - 1)
        + "d." +  SpaceInfo.INFO.getConfiguration().getLocation();
  }

  /**
   * Class constructor.
   *
   * @param m  the maximum number of actors that can be managed.
   *
  **/
  public Registry(final int m)
  {
    this.actors        = new ConcurrentHashMap<>();
    this.names         = new ConcurrentHashMap<>();
    this.subscriptions = new ConcurrentHashMap<>();

    this.id  = new AtomicInteger(-1);
    this.max = m;

    this.format = "%0" + (String.valueOf(this.max).length() - 1)
        + "d." +  SpaceInfo.INFO.getConfiguration().getLocation();
  }

  /**
   * Adds a reference - actor pair.
   *
   * If the actor is new then this method creates automatically
   * the actor reference.
   *
   * @param a  the actor.
   *
   * @return the actor reference or <code>null</code> if the operation fails.
   *
  **/
  public Reference add(final Actor a)
  {
    int aid = this.id.incrementAndGet();

    while (aid < this.max)
    {
      ActorReference r = new ActorReference(
          String.format(this.format, aid), a);

      if (this.actors.putIfAbsent(r, a) == null)
      {
        return r;
      }

      aid = this.id.incrementAndGet();
    }

    return null;
  }

  /**
   * Checks if a reference is associated with an actor.
   *
   * @param r  the reference.
   *
   * @return <code>true</code> if there is an actor associated with
   * such a reference and <code>false</code> otherwise.
   *
  **/
  public boolean exist(final Reference r)
  {
    return (this.actors.get(r) != null);
  }

  /**
   * Gets the actor associated with a reference.
   *
   * @param r  the reference.
   *
   * @return the actor.
   *
   * Note that the return value is <code>null</code> if there is not
   * an actor associated with such a reference.
   *
  **/
  public Actor get(final Reference r)
  {
    return this.actors.get(r);
  }

  /**
   * Gets the registered actors.
   *
   * @return the actors.
   *
  **/
  public Collection<Actor> actors()
  {
    return this.actors.values();
  }

  /**
   * Gets the references of the registered actors.
   *
   * @return the references.
   *
  **/
  public Collection<ActorReference> references()
  {
    return this.actors.keySet();
  }

  /**
   * Gets the number of registered actors.
   *
   * @return the number.
   *
  **/
  public int size()
  {
    return this.actors.size();
  }

  /**
   * Removes a reference - actor pair.
   *
   * @param r  the reference.
   *
  **/
  public void remove(final Reference r)
  {
    Actor i = this.actors.get(r);

    if (i != null)
    {
      if (this.names.containsValue(r))
      {
        this.names.values().remove(r);
      }

      this.actors.remove(r);
      ((ActorReference) r).clear();

      if (this.actors.size() == 0)
      {
        Controller.CONTROLLER.notifyEmpty();
      }
    }
  }

  /**
   * Gets the reference of an actor.
   *
   * @param n  the name bound to the reference.
   *
   * @return the reference or <code>null</code> if there is not a binding.
   *
  **/
  public Reference lookup(final String n)
  {
    return this.names.get(n);
  }

  /**
   * Binds a name to an actor reference.
   *
   * @param n  the binding name.
   * @param r  the reference.
   *
   * @return <code>true</code> if the is the binding is successful.
   *
   * Note that the binding is not possible when the name
   * is already bound to a reference.
   *
  **/
  public boolean bind(final String n, final ActorReference r)
  {
    if (this.names.putIfAbsent(n, r) == null)
    {
      checkSubscription(n, r);

      return true;
    }

    return false;
  }

  private void checkSubscription(final String n, final ActorReference r)
  {
    for (Entry<Message, String> e : this.subscriptions.entrySet())
    {
      if (e.getValue().equals(n))
      {
       replyReference(e.getKey(), r);

       this.subscriptions.remove(e.getKey());
      }
    }
  }

  private void replyReference(final Message m, final ActorReference r)
  {
    Controller.CONTROLLER.getProvider().send(m, r);
  }

  /**
   * Unbinds a name from an actor reference.
   *
   * @param n  the binding name.
   * @param r  the reference.
   *
  **/
  public void unbind(final String n, final ActorReference r)
  {
    if (this.names.get(n).equals(r))
    {
      this.names.remove(n);
    }
  }

  /**
   * Subscribes the interest about the reference of an actor
   * that is bound or will be bound to a specific name.
   *
   * @param m  the subscription message.
   * @param n  the binding name.
   *
  **/
  public void subscribe(final Message m, final String n)
  {
    ActorReference r = this.names.get(n);

    if (r != null)
    {
      replyReference(m, r);
    }
    else
    {
      this.subscriptions.putIfAbsent(m, n);
    }
  }

  /**
   * Cancel the subscription about the interest about the reference
   * of an actor will be bound to a specific name.
   *
   * @param n  the binding name.
   * @param r  the subscriber reference.
   *
  **/
  public void unsubscribe(final String n, final ActorReference r)
  {
    Message m = null;

    for (Entry<Message, String> e : this.subscriptions.entrySet())
    {
      if (e.getValue().equals(n) && e.getKey().getSender().equals(r))
      {
        m = e.getKey();

        break;
      }
    }

    if (m != null)
    {
      this.subscriptions.remove(m);
    }
  }

  /**
   * Starts the registry.
   *
  **/
  public void start()
  {
    List<Actor> c = new ArrayList<>(this.actors.values());

    for (Actor a : c)
    {
      this.actors.remove(a.getReference());
    }

    for (Actor a : c)
    {
      ActorReference r = (ActorReference) a.getReference();

      r.restore(a);
      this.actors.put(r, a);
    }
  }

  /**
   * Shutdowns the registry.
   *
  **/
  public void shutdown()
  {
  }
}
