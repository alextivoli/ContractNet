package it.unipr.sowide.actodes.examples.pubsub;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.service.group.MulticastReference;
import it.unipr.sowide.actodes.service.group.Role;
import it.unipr.sowide.actodes.service.group.content.Signup;

/**
 *
 * The {@code Processor} class defines a behavior that represents a control
 * system that processes the temperature values received from some
 * {code Sensor} actors.
 *
 * In particular, it processes temperature values until
 * the {@code Initiator} actor asks it to kill itself.
 *
**/

public final class Processor extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final String TOPIC = "temperature";

  private static final MessagePattern TEMPERATURE =
      MessagePattern.contentPattern(new IsInstance(Temperature.class));


  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    MessageHandler a = (m) -> {
      if ((m.getContent() == null)
          || !(m.getContent() instanceof MulticastReference))
      {
        return Shutdown.SHUTDOWN;
      }

      System.out.println("Shut");
      return null;
    };

    MessageHandler h = (m) -> {
      System.out.println("Start Pr");
      future(PROVIDER, new Signup(TOPIC, Role.SUBSCRIBER), a);

      return null;
    };

    c.define(START, h);

    c.define(TEMPERATURE, DUMMY);

    h = (m) -> {
      send(m.getSender(), Done.DONE);

      return Shutdown.SHUTDOWN;
    };

    c.define(KILL, h);
  }
}
