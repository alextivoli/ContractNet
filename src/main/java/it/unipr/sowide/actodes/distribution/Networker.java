package it.unipr.sowide.actodes.distribution;

import static it.unipr.sowide.actodes.controller.Controller.CONTROLLER;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.distribution.content.Closed;
import it.unipr.sowide.actodes.distribution.content.Connected;
import it.unipr.sowide.actodes.distribution.content.Container;
import it.unipr.sowide.actodes.distribution.content.Disconnected;
import it.unipr.sowide.actodes.distribution.content.Network;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.SimpleService;
/**
 *
 * The {@code Networker} class defines a network management service.
 *
 * It serves {code Network} events.
 *
 * @see it.unipr.sowide.actodes.distribution.content.Network
 *
**/

public final class Networker extends SimpleService
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public Networker()
  {
    super(new MessagePattern(new MessagePatternField(
        MessageField.CONTENT, new IsInstance(Network.class))));
  }

  /** {@inheritDoc} **/
  @Override
  public Behavior process(final Message m)
  {
    Dispatcher d = CONTROLLER.getDispatcher();

    if (d != null)
    {
      Object o = m.getContent();

      if (o instanceof Container)
      {
        Container c = (Container) m.getContent();

        for (Message i : c.getMessages())
        {
          // Manages local references received from remote actor spaces.
          Reference r = CONTROLLER.getRegistry().get(i.getReceiver())
              .getReference();

          if (r != null)
          {
            r.push(m);
          }
          else
          {
            CONTROLLER.getProvider().send(m, Error.UNREACHABLEACTOR);
          }
        }
      }
      else if (o instanceof Connected)
      {
        Connected e = (Connected) o;

        d.add(e.getSource());

        if (e.isBroker())
        {
          d.setBroker(e.getSource());
        }
      }
      else if (o instanceof Disconnected)
      {
        Disconnected e = (Disconnected) o;

        d.delete(e.getSource());
      }
      else if (o instanceof Closed)
      {
        Closed e = (Closed) o;

        if (d.getBroker().equals(e.getSource()))
        {
          for (Reference r : d.providers())
          {
            d.delete(r);
          }

          CONTROLLER.shutdown();
        }
        else
        {
          d.delete(e.getSource());
        }
      }
    }


    return null;
  }
}
