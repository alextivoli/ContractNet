package it.unipr.sowide.actodes.examples.pubsub;

import java.util.Random;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.CaseFactory;
import it.unipr.sowide.actodes.actor.MessageHandler;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.service.group.MulticastReference;
import it.unipr.sowide.actodes.service.group.Role;
import it.unipr.sowide.actodes.service.group.content.Signup;

/**
 *
 * The {@code Sensor} class defines a behavior that represents a temperature
 * sensor.
 *
 * In  particular, it publishes temperature values until
 * the {@code Initiator} actor asks it to kill itself.
 *
**/

public final class Sensor extends Behavior
{
  private static final long serialVersionUID = 1L;

  private static final int DELAY = 100;

  private static final String TOPIC = "temperature";

  // Minimum temperature value.
  private static final int MIN = -100;
  // Maximum temperature value.
  private static final int MAX = 100;

  private final Random random;

  private MessageHandler c1;

  /**
   * The multicast reference.
   *
  **/
  protected MulticastReference multicast;

  /**
   * Class constructor.
   *
  **/
  public Sensor()
  {
    this.random = new Random();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {
    this.c1 = (m) -> {
      send(Sensor.this.multicast, temperature());

      onReceive(TIMEOUT, DELAY, this.c1);

      return null;
    };

    MessageHandler c2 = (m) -> {
      if ((m.getContent() == null)
          || !(m.getContent() instanceof MulticastReference))
      {
        return Shutdown.SHUTDOWN;
      }

      Sensor.this.multicast = (MulticastReference) m.getContent();

      onReceive(TIMEOUT, DELAY, this.c1);

      return null;
    };

    MessageHandler h = (m) -> {
      future(PROVIDER, new Signup(TOPIC, Role.PUBLISHER), c2);

      return null;
    };

    c.define(START, h);

    h = (m) -> {
      send(m.getSender(), Done.DONE);

      return Shutdown.SHUTDOWN;
    };

    c.define(KILL, h);
  }

  // Measures the temperature.
  private Temperature temperature()
  {
    return new Temperature(
        ((this.random.nextInt(Math.abs(MAX - MIN) + 1)
            * this.random.nextFloat()) + MIN));
  }
}
