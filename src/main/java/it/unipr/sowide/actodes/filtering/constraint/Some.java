package it.unipr.sowide.actodes.filtering.constraint;

import java.util.Collection;

/**
 *
 * The {@code Some} class defines a constraint applied to a group of objects.
 *
 * It is satisfied if at least one of such objects satisfies it.
 *
 * @param <T>  the object type.
 *
**/

public final class Some<T> implements AggregateConstraint<T>
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
  public Some(final UnaryConstraint<T> c)
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
  public boolean eval(final Collection<T> o)
  {
    for (T i : o)
    {
      if (this.value.eval(i))
      {
        return true;
      }
    }

    return false;
  }
}
