package it.unipr.sowide.actodes.filtering;

import java.io.Serializable;

/**
 *
 * The {@code OptionalField} class supports the initialization
 * of an optional field of an object.
 *
 * @param <T>  the field type.
 *
**/

public abstract class OptionalField<T extends Field> implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final T field;
  private final Object value;

  /**
   * Class constructor.
   *
   * @param f  the field identifier.
   * @param v  the field value.
   *
  **/
  protected OptionalField(final T f, final Object v)
  {
    this.field = f;
    this.value = v;
  }

  /**
   * Gets the field identifier.
   *
   * @return the identifier.
   *
  **/
  public T getField()
  {
    return this.field;
  }

  /**
   * Gets the field value.
   *
   * @return the value.
   *
  **/
  public Object getValue()
  {
    return this.value;
  }
}
