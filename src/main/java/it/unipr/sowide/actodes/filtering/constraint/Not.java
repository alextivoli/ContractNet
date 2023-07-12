package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code Not} class defines a negation constraint.
 *
 * It is based on another constraint and it is satisfied
 * by the objects that do not satisfy such a constraint.
 *
 * @param <T>  the object type.
 *
**/

public final class Not<T> implements UnaryConstraint<T>
{
  private static final long serialVersionUID = 1L;

  // Field.
  private final UnaryConstraint<T> value;

  /**
   * Class constructor.
   *
   * @param c  the constraint.
   *
  **/
  public Not(final UnaryConstraint<T> c)
  {
    this.value = c;
  }

  /**
   * Gets the constraint.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<T> getValue()
  {
    return this.value;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final T o)
  {
    return !this.value.eval(o);
  }
}
