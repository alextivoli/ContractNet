package it.unipr.sowide.actodes.examples.messaging;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code Sender} class defines a behavior that sends a specific number
 * of messages to a set of {@code Receiver} actors and then waits for their
 * replies.
 *
 * @see Receiver
 *
**/

public final class Sender extends Behavior
{
  private static final long serialVersionUID = 1L;

  // References of the receivers.
  private Reference[] receivers;
  // Number of messages.
  private int messages;
  // Umatched messages.
  private int received;
  // Total messages.
  private int total;
  // Alive processing handler.
  private MessageHandler process;

  /**
   * Class Constructor.
   *
   * @param r  the references of the receivers,
   * @param n  the number of messages.
   *
  **/
  public Sender(final Reference[] r, final int n)
  {
    this.receivers = r;
    this.messages  = n;

    this.received = this.receivers.length;
    this.total    = this.received * messages;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if ((this.receivers.length > 0) && (this.messages > 0))
      {
        this.process = (k) -> {
          this.received--;
          this.total--;

          if (this.total == 0)
          {
            send(SpaceInfo.INFO.getBroker(), Kill.KILL);

            return Shutdown.SHUTDOWN;
          }
          else if (this.received == 0)
          {
            for (Reference l : this.receivers)
            {
              future(l, Status.ALIVE, this.process);
            }

            this.received = this.receivers.length;
          }

          return null;
        };

        for (Reference l : this.receivers)
        {
          future(l, Status.ALIVE, this.process);
        }
      }

      return null;
    };

    c.define(START, a);
  }
}
