package it.unipr.sowide.actodes.service.group;

import java.util.List;
import java.util.Set;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code MulticastReference} class defines the references used for
 * delivering messages to a group of actors.
 *
**/

public class MulticastReference extends Reference
{
  private static final long serialVersionUID = 1L;

  private Set<Reference> publishers;
  private Set<Reference> subscribers;
  private Set<MulticastReference> remotes;

  /**
   * Class constructor.
   *
   * @param a  the address.
   * @param p  the local publisher references.
   * @param s  the local subscriber references.
   * @param r  the remote multicast references.
   *
  **/
  public MulticastReference(final String a, final Set<Reference> p,
      final Set<Reference> s,  final Set<MulticastReference> r)
  {
    super(a);

    this.publishers  = p;
    this.subscribers = s;
    this.remotes     = r;
  }

  /**
   * Pushes a message to the destinations.
   *
   * @param m  the message.
   *
   * Note that the sender of the message must be one of declared publishers
   * and that the message cannot require an answer.
   *
   * In fact, the possible answer to a two-way message is an error message.
   *
   * @see it.unipr.sowide.actodes.interaction.Error#WRONGARGUMENTS
   *
  **/
  @Override
  public void push(final Message m)
  {
    if (m.getType() == Type.TWOWAY)
    {
      Controller.CONTROLLER.getProvider().send(m, Error.WRONGARGUMENTS);
    }
    else if ((m.getSender() == Behavior.PROVIDER)
             || this.publishers.contains(m.getSender()))
    {
      Controller.CONTROLLER.getExecutor().multicast(m, this.subscribers);

      if (m.getSender().getLocation().equals(getLocation()))
      {
        for (Reference r : this.remotes)
        {
          Controller.CONTROLLER.getDispatcher().deliver(new Message(
              m.getIdentifier(), m.getSender(), List.of(r), m.getContent(),
              m.getTime(), m.getType(), m.getIdentifier()));
        }
      }
    }
    else if (!m.getSender().getLocation().equals(getLocation()))
    {
      Controller.CONTROLLER.getExecutor().multicast(m, this.subscribers);
    }
  }

  /**
   * Checks if a reference identifies a subscriber of the topic
   * associated with such a multicast reference.
   *
   * @param r  the reference;
   *
   * @return <code>true</code> if the reference identifies  a subscriber.
   *
  **/
  public boolean belongTo(final Reference r)
  {
    return this.subscribers.contains(r);
  }
}
