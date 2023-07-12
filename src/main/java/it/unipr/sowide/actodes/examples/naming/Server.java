package it.unipr.sowide.actodes.examples.naming;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.service.naming.content.Bind;

/**
 *
 * The {@code Server} class defines a behavior that describes a server
 * that spends some time in its bootstrap, then binds its name and then
 * kills itself when it receives a message.
 *
 * @see Client
 *
**/

public final class Server extends Behavior
{
  private static final long serialVersionUID = 1L;

  private long time;

  /**
   * Class constructor.
   *
   * @param t  the server bootstrap time.
   *
  **/
  public Server(final long t)
  {
    this.time = t;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      return Shutdown.SHUTDOWN;
    };

    MessageHandler k = (m) -> {
      System.out.println("server bootstrap completed!");

      send(PROVIDER, new Bind("server"));

      onReceive(ACCEPTALL, h);

      return null;
    };

    MessageHandler l = (m) -> {
      onReceive(ACCEPTALL, this.time, k);

      return null;
    };

    c.define(START, l);
  }
}
