package it.unipr.sowide.actodes.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.unipr.sowide.actodes.actor.Actor.DefaultState;
import it.unipr.sowide.actodes.actor.Actor.Phase;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.GlobalBroadcastReference;
import it.unipr.sowide.actodes.controller.LocalBroadcastReference;
import it.unipr.sowide.actodes.controller.SpecialReference;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.passive.Scheduler.Cycle;
import it.unipr.sowide.actodes.filtering.constraint.IsEqual;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Behavior} class provides a partial implementation
 * of an actor behavior.
 *
**/

public abstract class Behavior implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * Actor space service provider reference.
   *
  **/
  public static final Reference PROVIDER = new SpecialReference();

  /**
   * Actor space executor actor reference.
   *
  **/
  public static final Reference EXECUTOR = new SpecialReference();

  /**
   * Actor space broadcast reference.
   *
   * It is used for sending a message to all the actors of the actor space.
   *
  **/
  public static final Reference  SPACE = new LocalBroadcastReference();

  /**
   * Application broadcast reference.
   *
   * It is used for sending a message to all the actors of an application.
   *
  **/
  public static final Reference APP = new GlobalBroadcastReference();

  /**
   * Message pattern that accepts any message.
   *
  **/
  public static final MessagePattern ACCEPTALL = new MessagePattern();

  /**
   * Message pattern for matching the starting behavior message.
   *
  **/
  public static final MessagePattern START = new MessagePattern();

  /**
   * Message pattern for matching a kill message.
   *
  **/
  public static final MessagePattern KILL =
      MessagePattern.contentPattern(new IsEqual<Object>(Kill.KILL));

  /**
   * Message pattern for matching a timeout firing message.
   *
  **/
  public static final MessagePattern TIMEOUT =
      MessagePattern.contentPattern(new IsEqual<Object>(Error.TIMEOUT));

  /**
   * A handler that processes a kill message
   * performing the shutdown of the actor.
   *
   * Notes that it does not send a reply to the kill request message.
   *
  **/
  public static final MessageHandler DESTROYER = (m) -> {
    return Shutdown.SHUTDOWN;
  };

  /**
   * A dummy handler.
   *
  **/
  public static final MessageHandler DUMMY = (m) -> {
    return null;
  };

  /**
   * Message pattern matching a cycle message received from the
   * scheduling actor.
   *
   * Note such kind of message is usually sent by the executors
   * for passive actors.
   *
  **/
  public static final MessagePattern CYCLE =
      MessagePattern.contentPattern(new IsEqual<Object>(Cycle.CYCLE));

  private transient Actor actor;

  private BehaviorState state;
  private BehaviorState[] states;

  /**
   * Class constructor.
   *
  **/
  protected Behavior()
  {
    this.actor  = null;
    this.state  = DefaultState.DEFAULT;
    this.states = null;
  }

  /**
   * Class constructor.
   *
   * @param i  the initial behavior state.
   * @param s  the possible behavior states.
   *
  **/
  protected Behavior(final BehaviorState i, final BehaviorState[] s)
  {
    this.actor  = null;
    this.state  = i;
    this.states = s;
  }

  private final class BehaviorCase implements CaseFactory
  {
    private Map<BehaviorState, Map<MessagePattern, MessageHandler>> allDefs;

    private MessageHandler start;

    protected BehaviorCase()
    {
      this.allDefs = new HashMap<>();

      if (Behavior.this.states != null)
      {
        for (BehaviorState e : Behavior.this.states)
        {
          this.allDefs.put(e, new LinkedHashMap<>());
        }
      }
      else
      {
        this.allDefs.put(DefaultState.DEFAULT, new LinkedHashMap<>());
      }
    }

    @Override
	public void define(final MessagePattern p, final MessageHandler h)
    {
      if (Behavior.this.actor.getPhase() == Phase.CREATED)
      {
        if (p == Behavior.START)
        {
          this.start = h;
        }
        else if (getStates() != null)
        {
           for (BehaviorState e : getStates())
           {
             this.allDefs.get(e).put(p, h);
           }
        }
        else
        {
          this.allDefs.get(DefaultState.DEFAULT).put(p, h);
        }
      }
    }

    @Override
	public void define(final MessagePattern p,
        final MessageHandler h, final BehaviorState... s)
    {
      if (Behavior.this.actor.getPhase() == Phase.CREATED)
      {
        if (p == Behavior.START)
        {
          this.start = h;
        }
        else
        {
          for (BehaviorState e : s)
          {
            this.allDefs.get(e).put(p, h);
          }
        }
      }
    }

    protected MessageHandler getStart()
    {
      return this.start;
    }

    protected Map<BehaviorState, Map<MessagePattern, MessageHandler>> getDefs()
    {
      return this.allDefs;
    }

    protected void clear()
    {
      this.start   = null;
      this.allDefs = null;
    }
  }

  /**
   * It must define the message pattern - message handler
   * pairs (cases) that drive the execution of the behavior.
   *
   * @param c  the case factory.
   *
  **/
  public abstract void cases(CaseFactory c);

  /**
   * Configures the behavior.
   *
   * @param a  the actor.
   *
   * Note that the operation fails if an actor is already bound to
   * a behavior instance.
   *
  **/
  public void configure(final Actor a)
  {
    this.actor = a;

    if (this.actor.getPhase() == Phase.CREATED)
    {
      BehaviorCase c = new BehaviorCase();

      cases(c);

      a.init(c.getStart(), c.getDefs());
      c.clear();
    }
  }

  /**
   * Gets the reference of the actor.
   *
   * @return the reference.
   *
  **/
  public Reference getReference()
  {
    return this.actor.getReference();
  }

  /**
   * Gets the reference of the parent actor.
   *
   * @return the reference.
   *
  **/
  public Reference getParent()
  {
    return this.actor.getParent();
  }

  /**
   * Gets the possible actor states.
   *
   * @return
   *
   * the possible actor states or <code>null</code> if the actor
   * does not move from a set of different states.
   *
   *
  **/
  public BehaviorState[] getStates()
  {
    return this.states;
  }

  /**
   * Gets the actor state.
   *
   * @return the current actor state or <code>null</code> if the actor
   * does not move from a set of different states.
   *
  **/
  public BehaviorState getState()
  {
    return this.state;
  }

  /**
   * Sets the actor state.
   *
   * @param s  the actor state.
   *
  **/
  public void setState(final BehaviorState s)
  {
    if (this.actor != null)
    {
      this.state = s;

      this.actor.applyState(s);
    }
  }

  /**
   * Creates a new actor.
   *
   * @param b  the behavior.
   *
   * @return
   *
   * the reference of new actor or <code>null</code> if the creation
   * of the actor failed.
   *
  **/
  public Reference actor(final Behavior b)
  {
    try
    {
      Reference r = Controller.CONTROLLER.getExecutor().actor(
          this.actor.reference, b);

      if (r != null)
      {
        Logger.LOGGER.logActorCreation(
            this, r, b.getClass().getName());

        return r;
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
      this.actor.shutdown();
    }

    return null;

  }

  /**
   * Sends an one-way message to an actor.
   *
   * Note that the sender does not receive any information about
   * the successful reception of the message.
   *
   * @param r  the destination reference.
   * @param c  the message content.
   *
  **/
  public void send(final Reference r, final Object c)
  {
    Message m = this.actor.message(
        r, c, Type.ONEWAY, Message.NOINREPLYTO);

    Logger.LOGGER.logOutputMessage(this, m);

    r.push(m);
  }

  /**
   * Sends an one-way message to an actor.
   *
   * Note that the sender does not receive any information about
   * the successful reception of the message.
   *
   * @param l  the list of destination references.
   * @param c  the message content.
   *
  **/
  public void send(final List<Reference> l, final Object c)
  {
    Message m = this.actor.message(
        l, c, Type.ONEWAY, Message.NOINREPLYTO);

    Logger.LOGGER.logOutputMessage(this, m);

    for (Reference r : l)
    {
      r.push(m);
    }
  }

  /**
   * Sends an one-way message as reply to another message.
   *
   * Note that the message is sent only if the incoming message requires
   * an answer.
   *
   * Moreover, the sender does not receive any information about
   * the successful reception of the message.
   *
   * @param m  the message to be replied.
   * @param c  the message content.
   *
  **/
  public void send(final Message m, final Object c)
  {
    if (m.getType() == Type.TWOWAY)
    {
      Message r = this.actor.message(m.getSender(),
          c, Type.ONEWAY, m.getIdentifier());

      Logger.LOGGER.logOutputMessage(this, r);

      m.getSender().push(r);
    }
  }

  /**
   * Sends a two-way message to another actor.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * When the sender gets the reply from its message queue,
   * then it processes it.   
   *
   * @param a  the destination reference.
   * @param c  the message content.
   * @param p  the reply processing handler.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable or cannot manage the message.
   *
   * @see it.unipr.sowide.actodes.interaction.Error
   *
  **/
  public void future(final Reference a, final Object c, final MessageHandler p)
  {
    this.actor.future(a, c, p);
  }

  /**
   * Sends a two-way message to another actor.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * When the sender gets the reply from its message queue,
   * then it processes it.   
   *
   * @param a  the destination reference.
   * @param c  the message content.
   * @param t  the reply reception timeout.
   * @param p  the reply processing handler.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable, cannot manage the message or the timeout fires
   * before the reception of the reply.
   *
   * @see it.unipr.sowide.actodes.interaction.Error
   *
  **/
  public void future(final Reference a,
      final Object c, final long t, final MessageHandler p)
  {
    this.actor.future(a, c, t, p);
  }

  /**
   * Sends a two-way message as reply to an another message.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * When the sender gets the reply from its message queue,
   * then it processes it.   
   *
   * @param m  the message to be replied.
   * @param c  the message content.
   * @param p  the reply processing handler.
   *
   * Note that the reply can be also an error message if the incoming message
   * does not require an answer, the destination is not reachable or
   * cannot manage the message.
   *
   * @see it.unipr.sowide.actodes.interaction.Error
   *
  **/
  public void future(final Message m, final Object c, final MessageHandler p)
  {
    this.actor.future(m, c, p);
  }

  /**
   * Sends a two-way message as reply to an another message.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * When the sender gets the reply from its message queue,
   * then it processes it.   
   *
   * @param m  the message to be replied.
   * @param c  the message content.
   * @param t  the reply reception timeout.
   * @param p  the reply processing handler.
   *
   * Note that the reply can be also an error message if the incoming message
   * does not require an answer, the destination is not reachable, cannot
   * manage the message or the timeout fires before the reception of the reply.
   *
   * @see it.unipr.sowide.actodes.interaction.Error
   *
  **/
  public void future(final Message m,
      final Object c, final long t, final MessageHandler p)
  {
    this.actor.future(m, c, t, p);
  }

  /**
   * Processes an incoming message that satisfies a specific message pattern.
   *
   * @param p  the message pattern.
   * @param h  the message processing handler.
   *
   * Note that the actor does not stop itself for waiting for the message.
   *
   * Moreover, such a message pattern - message handler pair manages a single
   * message (i.e., it is removed after the processing of a message).
   *
  **/
  public void onReceive(final MessagePattern p, final MessageHandler h)
  {
    this.actor.onReceive(p, h);
  }

  /**
   * Processes either an incoming message that satisfies a specific message
   * pattern or an error message if the timeout fires.
   *
   * @param p  the message pattern.
   * @param t  the message reception timeout.
   * @param h  the message processing handler.
   *
   * Note that the actor does not stop itself for waiting for the message.
   *
   * Moreover, such a message pattern - message handler pair manages a single
   * message (i.e., it is removed after the processing of a message).
   *
  **/
  public void onReceive(final MessagePattern p,
      final long t, final MessageHandler h)
  {
    this.actor.onReceive(p, t, h);
  }

  /**
   * Provides information about the current behavior.
   *
   * @return the information.
   *
   *
   * This method returns <code>null</code> and must be overridden
   * to provides some information about the current behavior.
   *
  **/
  public Serializable log()
  {
    return null;
  }
}
