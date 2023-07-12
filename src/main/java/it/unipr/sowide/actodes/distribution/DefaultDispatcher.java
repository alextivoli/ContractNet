package it.unipr.sowide.actodes.distribution;

import java.util.List;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.interaction.Error;

/**
 *
 * The {@code DefaultDispatcher} class defines the default implementation of
 * a message dispatcher. This implementation immediately sends each message
 * directed to an actor of another actor space of the application.
 *
**/

public final class DefaultDispatcher extends Dispatcher
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public DefaultDispatcher()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public void deliver(final Message m)
  {
    Connection c = this.connections.get(m.getReceiver().getLocation());

    if (c != null)
    {
      if (c.forward(m))
      {
        return;
      }
    }
    else if (Behavior.PROVIDER.getLocation().equals(
        m.getReceiver().getLocation()))
    {
      // Manages local references received from remote actor spaces.
      Actor d = this.registry.get(m.getReceiver());

      if (d != null)
      {
        d.getReference().push(m);

        return;
      }
    }

    if (m.getType() == Type.TWOWAY)
    {
      deliver(new Message(
          0, Behavior.PROVIDER, List.of(m.getSender()), Error.UNREACHABLEACTOR,
          System.currentTimeMillis(), Type.ONEWAY, Message.NOINREPLYTO));
    }
  }
}
