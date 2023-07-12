package it.unipr.sowide.actodes.service.mobile;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.executor.passive.Scheduler;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.SimpleService;
import it.unipr.sowide.actodes.service.logging.Logger;
import it.unipr.sowide.actodes.service.mobile.content.Accepted;
import it.unipr.sowide.actodes.service.mobile.content.Active;
import it.unipr.sowide.actodes.service.mobile.content.Mobility;
import it.unipr.sowide.actodes.service.mobile.content.Move;
import it.unipr.sowide.actodes.service.mobile.content.Transfer;

/**
 *
 * The {@code Mobiler} class support the moving of actors to another
 * actor space.
 *
 * It serves the {code Accepted}, {code ActiveBehavior},
 * {code Move}, and {code Transfer} requests.
 *
 * @see it.unipr.sowide.actodes.service.mobile.content.Accepted
 * @see it.unipr.sowide.actodes.service.mobile.content.Active
 * @see it.unipr.sowide.actodes.service.mobile.content.Move
 * @see it.unipr.sowide.actodes.service.mobile.content.Transfer
 *
**/

public final class Mobiler extends SimpleService
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public Mobiler()
  {
    super(new MessagePattern(new MessagePatternField(
        MessageField.CONTENT,  new IsInstance(Mobility.class))));
  }

  /** {@inheritDoc} **/
  @Override
  public Behavior process(final Message m)
  {
    if (m.getContent() instanceof Move)
    {
      Move i = (Move) m.getContent();

      if (!i.getDestination().equals(Behavior.PROVIDER)
          && (m.getSender() instanceof MobileReference)
          && (m.getSender().getLocation().equals(
              Behavior.PROVIDER.getLocation())))
      {
        Controller.CONTROLLER.getProvider().send(i.getDestination(),
            new Transfer((MobileReference) m.getSender(), i.getBehavior()));
      }
      else
      {
        Controller.CONTROLLER.getProvider().send(m, Error.REFUSEDREQUEST);
      }
    }
    else if (m.getContent() instanceof Transfer)
    {
      Transfer i = (Transfer) m.getContent();

      Actor a = build(i.getBehavior(), i.getCurrent().getHome());

      if (a != null)
      {
        Controller.CONTROLLER.getProvider().send(m.getSender(), new Accepted(
            i.getCurrent(), (MobileReference) a.getReference()));
      }
      else
      {
        Controller.CONTROLLER.getProvider().send(m, Error.WRONGARGUMENTS);
      }
    }
    else if (m.getContent() instanceof Accepted)
    {
      Accepted a = (Accepted) m.getContent();

      Controller.CONTROLLER.getProvider().send(a.getPrevious(), a);
    }
    else if (m.getContent() instanceof Active)
    {
      Actor a = Controller.CONTROLLER.getRegistry().get(
          ((Active) m.getContent()).getReference());

      if (a != null)
      {
        if (Controller.CONTROLLER.getExecutor().isEmpty())
        {
          Behavior.EXECUTOR.push(Scheduler.CYCLEMESSAGE);
        }

        Controller.CONTROLLER.getExecutor().add(a);
      }
      else
      {
        Controller.CONTROLLER.getProvider().send(m, Error.UNREACHABLEACTOR);
      }
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.UNKNOWNCONTENT);
    }

    return null;
  }

  // Builds a mobile actor.
  private Actor build(final Behavior b, final MobileReference h)
  {
    if (h != null)
    {
      try
      {
        Actor a = Controller.CONTROLLER.getExecutor().newActor(b);

        Reference r = Controller.CONTROLLER.getRegistry().add(a);

        if (r != null)
        {
          MobileReference m = new MobileReference(r.toString(), a, h);

          a.configure(Behavior.PROVIDER, m, b);
          b.configure(a);

          Logger.LOGGER.logActorCreation(Controller.CONTROLLER.getProvider(),
              r, b.getClass().getName());

          return a;
        }
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }
    }

    return null;
  }
}
