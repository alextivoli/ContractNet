package it.unipr.sowide.actodes.filtering.constraint;

import java.util.Collection;
import java.util.List;

/**
 *
 * The {@code Contains} class defines a constraint
 * applied to a group of objects.
 *
 * It is satisfied if the group of objects contains all the objects
 * associated with the constraint.
 *
 * @param <T>  the object type.
 *
**/

public final class Contains<T> implements AggregateConstraint<T>
{
  private static final long serialVersionUID = 1L;

  // Field.
  private final List<T> values;

  /**
   * Class constructor.
   *
   * @param v  the values.
   *
  **/
  public Contains(final List<T> v)
  {
    this.values = v;
  }

  /**
   * Gets the values.
   *
   * @return the values.
   *
  **/
  public List<T> getValue()
  {
    return this.values;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Collection<T> o)
  {
    for (T i : this.values)
    {
      if (!o.contains(i))
      {
        return false;
      }
    }

    return true;
  }
}
