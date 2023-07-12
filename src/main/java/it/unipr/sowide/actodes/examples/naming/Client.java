package it.unipr.sowide.actodes.examples.naming;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.naming.content.Lookup;
import it.unipr.sowide.actodes.service.naming.content.Subscribe;

/**
 *
 * The {@code Sender} class defines a behavior that describes a client that
 * checks if the server has already bound its name.
 *
 * If it is true, it sends a message to the server and kills itself.
 *
 * If it is false, it sends a message to the service provider asking
 * information about the future binding of the server name.
 *
 * When the server binds its name, then it receives the bound reference
 * from the service provider then it sends a message to the server
 * and kills itself.
 *
 * @see Server
 *
**/

public final class Client extends Behavior
{
  private static final long serialVersionUID = 1L;


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler k = (m) -> {
      if (m.getContent() instanceof Reference)
      {
        System.out.println("client sends \"hello server!\" to the server!");

        send((Reference) m.getContent(), "hello server!");
      }

      return Shutdown.SHUTDOWN;
    };

    MessageHandler h = (m) -> {
      if (m.getContent() instanceof Reference)
      {
        System.out.println("client sends \"hello server!\" to the server!");

        send((Reference) m.getContent(), "hello server!");

        return Shutdown.SHUTDOWN;
      }
      else
      {
        System.out.println("client sends a subscribe message");

        future(PROVIDER, new Subscribe("server"), k);

        return null;
      }
    };

    MessageHandler s = (m) -> {
      System.out.println("client sends a lookup message");

      future(PROVIDER, new Lookup("server"), h);

      return null;
    };

    c.define(START, s);
  }
}
