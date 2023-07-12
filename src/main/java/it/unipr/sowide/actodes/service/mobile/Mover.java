package it.unipr.sowide.actodes.service.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.mobile.content.Accepted;
import it.unipr.sowide.actodes.service.mobile.content.Active;
import it.unipr.sowide.actodes.service.mobile.content.Move;

/**
 *
 * The {@code Mover} class defines a behavior that moves the actor
 * to another actor space.
 *
**/

public final class Mover extends Behavior
{
  private static final long serialVersionUID = 1L;

  private final MobileState state;
  private final ArrayList<Message> messages;

  /**
   * Class constructor.
   *
   * @param d
   *
   * the reference of the service provider of the destination actor space.
   *
   * @param b
   *
   * the qualified class name of the initial behavior in the destination
   * actor space.
   *
  **/
  public Mover(final Reference d, final Behavior b)
  {
    this.state    = new MobileState(d, b);
    this.messages = new ArrayList<>();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler h = (m) -> {
      send(PROVIDER, new Move(this.state));

      return null;
    };

    c.define(START, h);

    h = (m) -> {
      Accepted a = (Accepted) m.getContent();

      Iterator<Message> i = this.messages.iterator();

      while (i.hasNext())
      {
        Message e = i.next();
        a.getCurrent().push(new Message(
            e.getIdentifier(), e.getSender(), List.of(a.getCurrent()),
            e.getContent(), e.getTime(), e.getType(), e.getInReplyTo()));
        i.remove();
      }

      MobileReference r = (MobileReference) getReference();

      r.redirect(r, a.getCurrent());

      send(SpaceInfo.INFO.getProvider(
          a.getCurrent()), new Active(a.getCurrent()));

      return Shutdown.SHUTDOWN;
    };

    c.define(MessagePattern.contentPattern(new IsInstance(Accepted.class)), h);

    h = (m) -> {
      this.messages.add(m);

      return null;
    };

    //c.define(ACCEPTALL, h);
  }
}
