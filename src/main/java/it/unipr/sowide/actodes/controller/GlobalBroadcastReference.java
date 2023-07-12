package it.unipr.sowide.actodes.controller;

import java.util.List;

import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code SpecialReference} class defines the references for accessing
 * to the special actors of the actor space.
 *
**/

public final class GlobalBroadcastReference extends SpecialReference
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public GlobalBroadcastReference()
  {
    super();
  }

  /**
   * Class constructor.
   *
   * @param s  the special address.
   *
  **/
  public GlobalBroadcastReference(final String s)
  {
    super(s);
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
    else if (Controller.CONTROLLER.getExecutor() != null)
    {
      Controller.CONTROLLER.getExecutor().broadcast(m);

      for (Reference r : Controller.CONTROLLER.getDispatcher().lbroadcasts())
      {
        Controller.CONTROLLER.getDispatcher().deliver(new Message(
            m.getIdentifier(), m.getSender(), List.of(r), m.getContent(),
            m.getTime(), m.getType(), m.getInReplyTo()));
      }
    }
    else
    {
      Controller.CONTROLLER.getDispatcher().deliver(m);
    }
  }
}
