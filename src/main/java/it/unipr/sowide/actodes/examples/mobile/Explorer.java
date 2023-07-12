package it.unipr.sowide.actodes.examples.mobile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.interaction.Status;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.mobile.Mover;

/**
 *
 * The {@code Explorer} class defines a behavior that allows the movement of
 * the actor among all the actors spaces of the application.
 *
 * In particular, in each actor space it creates a {@code Responder} actor,
 * sends a message to all the responder and waits for their answers and then
 * moved to another actor space.
 *
 * When the actor crossed all the actor spaces it asks to all the
 * actors actors to kill themselves and then it kills itself.
 *
 * @see Responder
 *
**/

public final class Explorer extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final MessagePattern ALIVEPATTERN =
      MessagePattern.contentPattern(new IsInstance(Status.class));

  private final ArrayList<Reference> path;
  private final HashSet<Reference> responders;
  /**
   * Number of {@code Done} messages to be processed.
   *
  **/
  protected int done;

  /**
   * Class constructor.
   *
   * @param p  the completed path.
   * @param r  the responders.
  **/
  Explorer(final ArrayList<Reference> p, final HashSet<Reference> r)
  {
    this.path       = p;
    this.responders = r;

    this.done = 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      Iterator<Reference> i = SpaceInfo.INFO.getProviders().iterator();

      Reference h = null;

      while (i.hasNext())
      {
        Reference d = i.next();

        if (!this.path.contains(d))
        {
          h = d;

          this.path.add(h);

          break;
        }
      }

      if (h != null)
      {
        final Reference e = h;

        this.responders.add(actor(new Responder()));

        this.done = this.responders.size();

        MessageHandler c1 = (n) -> {
          this.done--;

          if (this.done == 0)
          {
            return new Mover(e, this);
          }

          return null;
        };

        this.responders.forEach(k -> future(k, Status.ALIVE, c1));
      }
      else
      {
        send(APP, Kill.KILL);
        send(SpaceInfo.INFO.getBroker(), Kill.KILL);
      }

      return null;
    };

    c.define(START, a);

    a = (m) -> {
      send(m, Done.DONE);

      return null;
    };

    c.define(ALIVEPATTERN, a);

    c.define(KILL, DESTROYER);
  }
}
