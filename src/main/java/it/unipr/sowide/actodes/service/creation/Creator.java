package it.unipr.sowide.actodes.service.creation;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Create;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.SimpleService;
import it.unipr.sowide.actodes.service.logging.Logger;

/**
 *
 * The {@code Creator} class defines an actor creation service.
 *
 * It serves the creation requests and it is mainly used
 * for creating actors in remote actor spaces.
 *
 * @see it.unipr.sowide.actodes.interaction.Create
 *
**/

public final class Creator extends SimpleService
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
  **/
  public Creator()
  {
    super(new MessagePattern(new MessagePatternField(
        MessageField.CONTENT, new IsInstance(Create.class))));
  }

  /** {@inheritDoc} **/
  @Override
  public Behavior process(final Message m)
  {
    Reference a = create(m);

    if (a != null)
    {
      Logger.LOGGER.logActorCreation(Controller.CONTROLLER.getProvider(),
          a, ((Create) m.getContent()).getBehavior().getClass().getName());
    }

    if (a != null)
    {
      Controller.CONTROLLER.getProvider().send(m, a);
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.WRONGARGUMENTS);
    }

    return null;
  }

  private Reference create(final Message m)
  {
    if (Controller.CONTROLLER.getExecutor().isEmpty())
    {
      Reference a = Controller.CONTROLLER.getExecutor().actor(
          m.getSender(), ((Create) m.getContent()).getBehavior());

      Controller.CONTROLLER.getExecutor().manage();

      return a;
    }
    else
    {
      return Controller.CONTROLLER.getExecutor().actor(
          m.getSender(), ((Create) m.getContent()).getBehavior());
    }
  }
}
