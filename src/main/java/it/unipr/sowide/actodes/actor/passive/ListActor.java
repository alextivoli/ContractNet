package it.unipr.sowide.actodes.actor.passive;

import java.util.concurrent.CopyOnWriteArrayList;

import it.unipr.sowide.actodes.actor.Message;

/**
 *
 * The {@code ListActor} class provides a partial implementation of
 * an actor using the passive threading solution and maintaining
 * messages into a list.
 *
**/

public abstract class ListActor extends PassiveActor
{
  private static final long serialVersionUID = 1L;

  // non processed message queue.
  protected final CopyOnWriteArrayList<Message> queue;

  /**
   * Class constructor.
   *
   * @param q  the message queue.
   *
  **/
  protected ListActor(final CopyOnWriteArrayList<Message> q)
  {
    this.queue = q;

    this.phase = Phase.CREATED;
  }

  /** {@inheritDoc} **/
  @Override
  public Iterable<Message> getMailbox()
  {
    return this.queue;
  }

  /** {@inheritDoc} **/
  @Override
  public void add(final Message m)
  {
    this.queue.add(m);
  };
}
