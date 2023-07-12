package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code IsDifferent} class defines a diversity comparative constraint.
 *
 * It is satisfied by the objects that are different from
 * the object associated with such a constraint.
 *
 * @param <T>  the object type.
 *
**/

public final class IsDifferent<T>
       implements UnaryConstraint<T>, BinaryConstraint<T, T>
{
  private static final long serialVersionUID = 1L;

  // Reference object.
  private final T value;

  /**
   * Class constructor.
   *
   * @param o  the object to be compared.
   *
  **/
  public IsDifferent(final T o)
  {
    this.value = o;
  }

  /**
   * Gets the object to be compared.
   *
   * @return the object.
   *
  **/
  public T getValue()
  {
    return this.value;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final T o)
  {
    return !o.equals(this.value);
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final T f, final T s)
  {
    return !f.equals(s);
  }
}
