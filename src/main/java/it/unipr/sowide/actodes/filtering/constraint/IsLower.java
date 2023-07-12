package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code IsLower} class defines lower comparative constraint.
 *
 * It is satisfied by the objects that are lower than
 * the object associated with such a constraint.
 *
 * @param <T>  the object type.
 *
 * This constraint is applied to objects that implements the
 * {@code Comparable} interface.
 *
 * @see Comparable
 *
**/

public final class IsLower<T extends Comparable<? super T>>
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
  public IsLower(final T o)
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
    return (o.compareTo(this.value) < 0);
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final T f, final T s)
  {
    return (f.compareTo(s) < 0);
  }
}
