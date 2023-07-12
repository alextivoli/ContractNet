package it.unipr.sowide.actodes.actor;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.filtering.MessageMatcher;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsEqual;
import it.unipr.sowide.actodes.filtering.constraint.Or;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Actor} class provides a partial implementation of an actor.
 *
**/

public abstract class Actor implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
     *
     * The {@code DefaultState} enumeration
     * that define the {@code DEFAULT} constant.
     *
    **/
  protected enum DefaultState implements BehaviorState
  {
    /**
     * The default actor state constant.
     *
    **/
    DEFAULT;
  }

  // Actor messages identifier.
  private long id;

  /**
   * Actor reference.
   *
  **/
  protected Reference reference;

  /**
   * Actor behavior static message pattern - handler definitions.
   *
  **/
  private Map<BehaviorState, Map<MessagePattern, MessageHandler>> allDefs;

  /**
   * Actor behavior static message pattern - handler pairs.
   *
  **/
  protected Map<MessagePattern, MessageHandler> statDefs;

  /**
   * Actor behavior dynamic message pattern - handler pairs.
   *
  **/
  protected ConcurrentHashMap<MessagePattern, MessageHandler> dynDefs;

  /**
   * Actor behavior dynamic timeouts.
   *
  **/
  protected Map<MessagePattern, Entry<Message, Long>> dTimeouts;

  private MessageHandler start;
  private MessageHandler acceptAll;

  /**
   * Actor behavior.
   *
  **/
  protected Behavior behavior;

  /**
   * Timer.
   *
  **/
  protected MessageHandler timer;

  /**
   * Actor execution phase.
   *
  **/
  public enum Phase
  {
    /**
     * The actor has been just created.
     *
    **/
    CREATED,
    /**
     * The actor is running.
     *
    **/
    RUNNING,
    /**
     * The actor is stopped.
     *
    **/
    STOPPED,
    /**
     * The actor is dead.
     *
    **/
    KILLED
  }

  /**
   * Execution phase.
   *
  **/
  protected Phase phase;

  // Parent actor reference.
  private Reference parent;

  /**
   * Class constructor.
   *
  **/
  protected Actor()
  {
    this.id = 0;

    this.reference = null;
    this.behavior  = null;
    this.start     = null;
    this.statDefs  = null;
    this.timer     = null;
    this.parent    = null;

    this.phase = Phase.CREATED;

    this.allDefs   = null;
    this.dynDefs   = new ConcurrentHashMap<>();
    this.dTimeouts = new HashMap<>();
  }

  /**
   * Configures the actor instance.
   *
   * @param p  the parent reference.
   * @param r  the actor reference.
   * @param b  the actor behavior.
   *
  **/
  public final void configure(final Reference p,
      final Reference r, final Behavior b)
  {
    this.parent    = p;
    this.reference = r;
    this.behavior  = b;
  }

  /**
   * Gets the reference of the parent actor.
   *
   * @return the reference.
   *
  **/
  public final Reference getParent()
  {
    return this.parent;
  }

  /**
   * Gets the actor reference.
   *
   * @return the reference.
   *
  **/
  public final Reference getReference()
  {
    return this.reference;
  }

  /**
   * Gets the actor behavior.
   *
   * @return the behavior.
   *
  **/
  public final Behavior getBehavior()
  {
    return this.behavior;
  }

  /**
   * Gets the actor execution phase.
   *
   * @return the phase.
   *
  **/
  public final Phase getPhase()
  {
    return this.phase;
  }

  /**
   * Gets the mailbox.
   *
   * @return the mailbox.
   *
  **/
  public abstract Iterable<Message> getMailbox();

  /**
   * Adds a message in the mailbox.
   *
   * @param m  the message.
   *
  **/
  public abstract void add(Message m);

  /**
   * Sends a two-way message to another actor.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * @param a  the destination reference.
   * @param c  the message content.
   * @param h  the reply processing handler.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable or cannot manage the message.
   *
  **/
  public void future(final Reference a, final Object c, final MessageHandler h)
  {
    Message m = message(a, c, Type.TWOWAY, Message.NOINREPLYTO);

    this.dynDefs.put(new MessagePattern(new MessagePatternField(
        MessageField.INREPLYTO, new IsEqual<Long>(m.getIdentifier()))), h);

    Logger.LOGGER.logOutputMessage(this.behavior, m);

    a.push(m);
  }

  /**
   * Sends a two-way message to another actor.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * @param a  the destination reference.
   * @param c  the message content.
   * @param t  the reply reception timeout.
   * @param h  the reply processing handler.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable, cannot manage the message or the timeout fires
   * before the reception of the reply.
   *
  **/
  public void future(final Reference a, final Object c, final long t,
      final MessageHandler h)
  {
    Message m = message(a, c, Type.TWOWAY, Message.NOINREPLYTO);

    MessagePattern p = new MessagePattern(new MessagePatternField(
        MessageField.INREPLYTO, new IsEqual<Long>(m.getIdentifier())));

    this.dynDefs.put(p, h);

    if (t > 0)
    {
      this.dTimeouts.put(p,
          new SimpleEntry<>(m, System.currentTimeMillis() + t));
    }

    Logger.LOGGER.logOutputMessage(this.behavior, m);

    a.push(m);
  }

  /**
   * Sends a two-way message as reply to an another message.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * @param m  the message to be replied.
   * @param c  the message content.
   * @param h  the reply processing handler.
   *
   * Note that the message is sent only if the incoming message requires
   * an answer.
   *
   * if the incoming message does not require an answer, then an error message
   * is logged.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable or cannot manage the message.
   *
  **/
  public void future(final Message m, final Object c, final MessageHandler h)
  {
    Message r = message(m.getSender(), c, Type.TWOWAY, m.getIdentifier());

    Logger.LOGGER.logOutputMessage(this.behavior, r);

    if (m.getType() == Type.TWOWAY)
    {
      this.dynDefs.put(new MessagePattern(new MessagePatternField(
          MessageField.INREPLYTO, new IsEqual<Long>(m.getIdentifier()))), h);

      m.getSender().push(r);
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(r, Error.UNEXPECTEDREPLY);
    }
  }

  /**
   * Sends a two-way message as reply to an another message.
   *
   * This kind of message requires a reply, but the sender does not stop
   * itself for waiting for the reply.
   *
   * @param m  the message to be replied.
   * @param c  the message content.
   * @param t  the reply reception timeout.
   * @param h  the reply processing handler.
   *
   * Note that the message is sent only if the incoming message requires
   * an answer.
   *
   * if the incoming message does not require an answer, then an error message
   * is logged.
   *
   * Note that the reply can be also an error message if the destination
   * is not reachable, cannot manage the message or the timeout fires
   * before the reception of the reply.
   *
  **/
  public void future(final Message m, final Object c, final long t,
      final MessageHandler h)
  {
    Message r = message(m.getSender(), c, Type.TWOWAY, m.getIdentifier());

    Logger.LOGGER.logOutputMessage(this.behavior, r);

    if (m.getType() == Type.TWOWAY)
    {
      MessagePattern p = new MessagePattern(new MessagePatternField(
          MessageField.INREPLYTO, new IsEqual<Long>(m.getIdentifier())));

      this.dynDefs.put(p, h);

      m.getSender().push(r);

      if (t > 0)
      {
        this.dTimeouts.put(p,
            new SimpleEntry<>(m, System.currentTimeMillis() + t));
      }
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(r, Error.UNEXPECTEDREPLY);
    }
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
    this.dynDefs.put(p, h);
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
  public void onReceive(final MessagePattern p, final long t,
      final MessageHandler h)
  {
    if (t > 0)
    {
      MessagePattern np = p;

      if (!p.equals(Behavior.ACCEPTALL) && (p.getContent() != null))
      {
        np = new MessagePattern(p,
            new MessagePatternField(MessageField.CONTENT, new Or<Object>(
                p.getContent(), new IsEqual<Object>(Error.TIMEOUT))));
      }

      this.dynDefs.put(np, h);
      this.dTimeouts.put(np,
          new SimpleEntry<>(null, System.currentTimeMillis() + t));
    }
    else
    {
      this.dynDefs.put(p, h);
    }
  }

  /**
   * Shutdowns the actor.
   *
  **/
  public void shutdown()
  {
    this.phase = Phase.KILLED;

    Controller.CONTROLLER.getExecutor().remove(this);
    Controller.CONTROLLER.getRegistry().remove(this.reference);

    Logger.LOGGER.logActorShutdown(getBehavior());
  }

  /**
   * Gets the absolute timeout value.
   *
   * @param t  the relative timeout value.
   *
   * @return the absolute timeout value.
   *
  **/
  protected long getTime(final long t)
  {
    return System.currentTimeMillis() + t;
  }

  /**
   * Initializes and configures the initial behavior of the actor.
   *
  **/
  protected void start()
  {
    this.statDefs = null;

    if (this.phase == Phase.KILLED)
    {
      return;
    }

    Logger.LOGGER.logBehaviorStart(this.behavior);

    if (this.start != null)
    {
      Behavior nb = this.start.process(null);

      if (nb != null)
      {
        if (nb.equals(Shutdown.SHUTDOWN))
        {
          shutdown();
          return;
        }

        become(nb);
        return;
      }
    }

    long t = System.nanoTime();

    configure();

    Logger.LOGGER.logBehaviorInit(this.behavior, System.nanoTime() - t);
  }

  /**
   * Moves to a new behavior.
   *
   * @param b  the behavior.
   *
  **/
  protected final void become(final Behavior b)
  {
    try
    {
      this.statDefs = null;

      rewind();

      Behavior o = this.behavior;

      this.behavior = b;

      this.phase = Phase.CREATED;
      this.behavior.configure(this);

      if (this.start != null)
      {
        Behavior nb = this.start.process(null);

        if (nb != null)
        {
          if (nb.equals(Shutdown.SHUTDOWN))
          {
            shutdown();
            this.phase = Phase.RUNNING;
            return;
          }

          become(nb);
          this.phase = Phase.RUNNING;
          return;
        }
      }

      Logger.LOGGER.logBehaviorStart(o, b);

      long t = System.nanoTime();

      configure();

      Logger.LOGGER.logBehaviorInit(b, System.nanoTime() - t);
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
      shutdown();
    }
  }

  /**
   * Configures the behavior message pattern - handler pairs.
   *
   * This method ends the life of the actor if either the actor killed itself
   * or it does not received any message pattern - handler pair to configure.
   *
  **/
  private void configure()
  {
    BehaviorState s = this.behavior.getState();

    Map<MessagePattern, MessageHandler> c;

    if (s != null)
    {
      c = this.allDefs.get(s);
    }
    else
    {
      c = this.allDefs.get(DefaultState.DEFAULT);
    }

    if (c != null)
    {
      this.statDefs = c;
    }
    else
    {
      this.statDefs = new HashMap<>();
    }

    if ((this.statDefs.size() == 0) && (this.dynDefs.size() == 0)
        && (this.timer == null))
    {
      shutdown();
      return;
    }

    this.phase = Phase.RUNNING;

    setSpecialDefs();
  }

  /**
   * Sets the actor state.
   *
   * @param s  the actor state.
   *
  **/
  protected void applyState(final BehaviorState s)
  {
    rewind();

    Map<MessagePattern, MessageHandler> c = this.allDefs.get(s);

    if (c != null)
    {
      this.statDefs = c;
    }

    setSpecialDefs();

    Logger.LOGGER.logStateChange(this.behavior, s);
  }

  /**
   * Initializes the message pattern - handler pairs.
   *
   * @param s  the handler starting the execution of the current behavior.
   * @param m  the message pattern - handler pairs of the current behavior.
   *
  **/
  protected void init(final MessageHandler s,
      final Map<BehaviorState, Map<MessagePattern, MessageHandler>> m)
  {
    this.start = s;
    this.allDefs = m;
  }

  /**
   * Sets the special definitions of the actor.
   *
  **/
  protected void setSpecialDefs()
  {
    MessageHandler c = this.statDefs.get(Behavior.ACCEPTALL);

    if (c != null)
    {
      this.acceptAll = c;

      this.statDefs.remove(Behavior.ACCEPTALL);
    }
    else
    {
      this.acceptAll = null;
    }
  }

  /**
   * Gets the message pattern - handler pair matching a specific type
   * of message.
   *
   * @param m  the message.
   *
   * @return message pattern - handler pair.
   *
  **/
  protected Entry<MessagePattern, MessageHandler> getDef(final Message m)
  {
    Entry<MessagePattern, MessageHandler> s = null;

    for (Entry<MessagePattern, MessageHandler> e : this.dynDefs.entrySet())
    {
      if (MessageMatcher.MATCHER.match(m, e.getKey()))
      {
        s = e;

        break;
      }
    }

    if (s != null)
    {
      this.dynDefs.remove(s.getKey());

      return s;
    }

    for (Entry<MessagePattern, MessageHandler> e : this.statDefs.entrySet())
    {
      if (MessageMatcher.MATCHER.match(m, e.getKey()))
      {
        return e;
      }
    }

    if (this.acceptAll != null)
    {
      return new SimpleEntry<MessagePattern, MessageHandler>(Behavior.ACCEPTALL,
          this.acceptAll);
    }

    return null;
  }

  /**
   * Processes a message.
   *
   * @param m  the message.
   * @param p  the message pattern.
   * @param h  the handler.
   *
   * @return the next behavior.
   *
  **/
  protected Behavior processMessage(final Message m, final MessagePattern p,
      final MessageHandler h)
  {
    Logger.LOGGER.logMessageProcessing(this.behavior, p, m);

    long t = System.nanoTime();

    Behavior b = h.process(m);

    Logger.LOGGER.logMessageProcessed(this.behavior, p, m,
        System.nanoTime() - t);

    return b;
  }

  /**
   * Processes a timeout message.
   *
  **/
  protected void processTimeout()
  {
    ArrayList<MessagePattern> processed = new ArrayList<>();

    for (Entry<MessagePattern, Entry<Message, Long>> e : this.dTimeouts
        .entrySet())
    {
      if (isTimeoutExpired(e.getValue().getValue()))
      {
        if (e.getValue().getKey() != null)
        {
          Controller.CONTROLLER.getProvider().send(e.getValue().getKey(),
              Error.TIMEOUT);
        }
        else
        {
          Controller.CONTROLLER.getProvider().send(this.reference,
              Error.TIMEOUT);
        }

        processed.add(e.getKey());
      }
    }

    for (MessagePattern m : processed)
    {
      this.dTimeouts.remove(m);
    }
  }

  /**
   * Checks if the timeout is expired.
   *
   * @param t  the timeout value.
   *
   * @return <code>true</code> if the timeout is expired.
   *
  **/
  protected boolean isTimeoutExpired(final long t)
  {
    return (t > 0) && (t <= System.currentTimeMillis());
  }

  /**
   * Builds a message.
   *
   * @param r the receiver reference.
   * @param c the message content.
   * @param t the message type.
   * @param i the replied message identifier.
   *
   * @return the message.
   *
  **/
  protected Message message(final Reference r, final Object c, final Type t,
      final long i)
  {
    return new Message(++this.id, this.reference, List.of(r), c,
        System.currentTimeMillis(), t, i);
  }

  /**
   * Builds a message.
   *
   * @param l the list of receiver references.
   * @param c the message content.
   * @param t the message type.
   * @param i the replied message identifier.
   *
   * @return the message.
   *
  **/
  protected Message message(final List<Reference> l, final Object c,
      final Type t, final long i)
  {
    return new Message(++this.id, this.reference, l, c,
        System.currentTimeMillis(), t, i);
  }

  /**
   * Sets the index to start a new iteration of all the messages
   * of the mailbox.
   *
  **/
  protected abstract void rewind();
}
