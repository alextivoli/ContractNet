package it.unipr.sowide.actodes.provider;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.distribution.Dispatcher;
import it.unipr.sowide.actodes.distribution.content.Closed;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.service.SimpleService;

/**
 *
 * The {@code Provider} class serves the actor space service requests.
 *
**/

public final class Provider extends Behavior
{
  private static final long serialVersionUID = 1L;

  private final Set<SimpleService> services;

  /**
   * Class constructor.
   *
   * @param s  the set of services that the provider manages.
   *
  **/
  public Provider(final Set<SimpleService> s)
  {
    this.services = s;
  }


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    for (SimpleService i : this.services)
    {
      for (MessagePattern p : i.getPatterns())
      {
        c.define(p, i);
      }
    }

    MessageHandler a = (m) -> {
      send(m, Done.DONE);

      Dispatcher d = Controller.CONTROLLER.getDispatcher();

      if ((d != null) && (d.providers().size() != 0))
      {
        if (PROVIDER.equals(m.getSender()))
        {
          Controller.CONTROLLER.getDispatcher().providers().forEach(
            r -> send(r, new Closed(PROVIDER)));

          send(Behavior.EXECUTOR, Kill.KILL);

          return Shutdown.SHUTDOWN;
        }
        else
        {
          if ((d != null) && (d.providers().size() > 0))
          {
            Controller.CONTROLLER.shutdown();
          }

          return null;
        }
      }
      else if (PROVIDER.equals(m.getSender()))
      {
        send(Behavior.EXECUTOR, Kill.KILL);

        return Shutdown.SHUTDOWN;
      }

      return null;
    };

    c.define(KILL, a);

    a = (m) -> {
      send(m, Error.UNKNOWNCONTENT);

      return null;
    };

    c.define(ACCEPTALL, a);
  }
}
