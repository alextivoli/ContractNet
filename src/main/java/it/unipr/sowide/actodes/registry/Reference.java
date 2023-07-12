package it.unipr.sowide.actodes.registry;

import java.io.Serializable;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.controller.Controller;

/**
 *
 * The {@code Reference} class provides a partial implementation
 * of a reference.
 *
 * Usually it identifies an actor through its identifier (i.e., the actor
 * address) and allows the delivery of messages to the actor. However, it
 * can support the broadcast or multicast of messages.
 *
**/

public abstract class Reference implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * Address.
   *
  **/
  protected String address;
  /**
   * Location part of the address.
   *
  **/
  protected String location;

  /**
   * Actor.
   *
  **/
  protected transient Actor actor;

  /**
   * Class constructor.
   *
  **/
  protected Reference()
  {
    this.address  = null;
    this.location = null;
  }

  /**
   * Class constructor.
   *
   * @param a  the address.
   *
  **/
  public Reference(final String a)
  {
    this.address  = a;
    this.location = a.substring(a.indexOf(".") + 1);
  }

  /**
   * Gets the name part of the address.
   *
   * @return the name.
   *
  **/
  public String getName()
  {
    return this.address.substring(0, this.address.indexOf("."));
  }

  /**
   * Gets the location part of the address.
   *
   * @return the location.
   *
  **/
  public String getLocation()
  {
    return this.location;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean equals(final Object obj)
  {
    if (obj instanceof Reference)
    {
      Reference a = (Reference) obj;

      if (this.address.equals(a.toString()))
      {
        return true;
      }
    }

    return false;
  }

  /** {@inheritDoc} **/
  @Override
  public int hashCode()
  {
    return toString().hashCode();
  }

  /**
   * Gets the address.
   *
   * @return the address.
   *
  **/
  @Override
  public String toString()
  {
    return this.address;
  }

  /**
   * Pushes a message towards the destination.
   *
   * @param m  the message.
   *
  **/
  public void push(final Message m)
  {
    if (this.actor != null)
    {
      this.actor.add(m);
    }
    else
    {
      Controller.CONTROLLER.getDispatcher().deliver(m);
    }
  }

  /**
   * Builds the address.
   *
   * @param n  the name part of the address.
   * @param l  the location part of the address.
   *
   * @return the address.
   *
  **/
  public static String address(final String n, final String l)
  {
    return n + "." + l;
  }
}

