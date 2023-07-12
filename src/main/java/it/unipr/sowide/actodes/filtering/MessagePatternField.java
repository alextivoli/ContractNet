package it.unipr.sowide.actodes.filtering;

import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;

/**
 *
 * The {@code SpeechActField} class supports the initialization
 * of an optional field of a message pattern.
 *
**/

public final class MessagePatternField extends OptionalField<MessageField>
{
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor.
   *
   * @param f  the field identifier.
   * @param v  the field value.
   *
  **/
  public MessagePatternField(final MessageField f, final Object v)
  {
    super(f, v);
  }
}
