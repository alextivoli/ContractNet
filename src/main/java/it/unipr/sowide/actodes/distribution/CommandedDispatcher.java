package it.unipr.sowide.actodes.distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.Message.Type;
import it.unipr.sowide.actodes.distribution.content.Container;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code CommandedDispatcher} class defines a message dispatcher that
 * queues the messages directed to an actor of another actor space of the
 * application.
 *
 * Messages coming from the service provider are immediately sent to its
 * destination.
 *
 * Messages coming from the actor executor to an actor executor of another actor
 * space also cause the delivery of the enqueued messages for such
 * an actor space.
 *
**/

public final class CommandedDispatcher extends Dispatcher
{
  private static final long serialVersionUID = 1L;

  // Provider location.
  private String location;
  // Messages to deliver.
  private final ConcurrentMap<String, List<Message>> messages;

  /**
   * Class constructor.
   *
  **/
  public CommandedDispatcher()
  {
    this.messages = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} **/
  @Override
  public void deliver(final Message m)
  {
    Connection c = this.connections.get(m.getReceiver().getLocation());

    if (c != null)
    {
      if (m.getSender() == Behavior.PROVIDER)
      {
        if (c.forward(m))
        {
          return;
        }
      }
      else if (m.getSender() == Behavior.EXECUTOR)
      {
        List<Message> l = this.messages.remove(m.getReceiver().getLocation());

        if (l != null)
        {
          l.add(m);

          Message b = new Message(
              0, Behavior.PROVIDER, List.of(m.getSender()), new Container(l),
              System.currentTimeMillis(), Type.ONEWAY, Message.NOINREPLYTO);

          if (c.forward(b))
          {
            return;
          }
        }
        else
        {
          if (c.forward(m))
          {
            return;
          }
        }
      }
      else
      {
        List<Message> l = this.messages.get(m.getReceiver().getLocation());

        if (l != null)
        {
          l.add(m);
        }
        else
        {
          l = new ArrayList<>();

          l.add(m);
          this.messages.put(m.getReceiver().getLocation(), l);
        }

        return;
      }
    }
    else if (this.location.equals(m.getSender().getLocation()))
    {
      // Manages local references received from remote actor spaces.
      Reference r = this.registry.get(m.getReceiver()).getReference();

      if (r != null)
      {
        r.push(m);

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
