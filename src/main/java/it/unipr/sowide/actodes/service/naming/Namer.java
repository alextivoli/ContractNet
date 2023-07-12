package it.unipr.sowide.actodes.service.naming;

import it.unipr.sowide.actodes.actor.ActorReference;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.SimpleService;
import it.unipr.sowide.actodes.service.naming.content.Bind;
import it.unipr.sowide.actodes.service.naming.content.Lookup;
import it.unipr.sowide.actodes.service.naming.content.Naming;
import it.unipr.sowide.actodes.service.naming.content.Subscribe;
import it.unipr.sowide.actodes.service.naming.content.Unbind;
import it.unipr.sowide.actodes.service.naming.content.Unsubscribe;

/**
 *
 * The {@code Namer} class defines an actor naming service.
 *
 * It serves the {code Bind}, {code Lookup} and {code Unbind} requests.
 *
 * @see it.unipr.sowide.actodes.service.naming.content.Bind
 * @see it.unipr.sowide.actodes.service.naming.content.Lookup
 * @see it.unipr.sowide.actodes.service.naming.content.Unbind
 *
**/

public final class Namer extends SimpleService
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public Namer()
  {
    super(new MessagePattern(new MessagePatternField(
        MessageField.CONTENT, new IsInstance(Naming.class))));
  }

  /** {@inheritDoc} **/
  @Override
  public Behavior process(final Message m)
  {
    if (m.getContent() instanceof Lookup)
    {
      Reference r = Controller.CONTROLLER.getRegistry().lookup(
          ((Lookup) m.getContent()).getName());

      if (r != null)
      {
        Controller.CONTROLLER.getProvider().send(m, r);
      }
      else
      {
        Controller.CONTROLLER.getProvider().send(m, Error.WRONGARGUMENTS);
      }

      return null;
    }
    else if (m.getContent() instanceof Bind)
    {
      boolean f = Controller.CONTROLLER.getRegistry().bind(
          ((Bind) m.getContent()).getName(),
          (ActorReference) m.getSender());

      if (f)
      {
        Controller.CONTROLLER.getProvider().send(m, Done.DONE);
      }
      else
      {
        Controller.CONTROLLER.getProvider().send(m, Error.WRONGARGUMENTS);
      }

      return null;
    }
    else if (m.getContent() instanceof Unbind)
    {
      Controller.CONTROLLER.getRegistry().unbind(
          ((Unbind) m.getContent()).getName(),
          (ActorReference) m.getSender());

      Controller.CONTROLLER.getProvider().send(m, Done.DONE);

      return null;
    }
    else if (m.getContent() instanceof Subscribe)
    {
      Controller.CONTROLLER.getRegistry().subscribe(
          m, ((Subscribe) m.getContent()).getName());

      return null;
    }
    else if (m.getContent() instanceof Unsubscribe)
    {
      Controller.CONTROLLER.getRegistry().unsubscribe(
          ((Unbind) m.getContent()).getName(),
          (ActorReference) m.getSender());

      Controller.CONTROLLER.getProvider().send(m, Done.DONE);

      return null;
    }

    return null;
  }
}
