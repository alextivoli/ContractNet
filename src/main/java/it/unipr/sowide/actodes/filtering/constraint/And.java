package it.unipr.sowide.actodes.filtering.constraint;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * The {@code And} class defines a conjunction constraint.
 *
 * It is based on through a set of other constraints and it is satisfied
 * by the objects that satisfy all the constraints.
 *
 * @param <T>  the object type.
 *
**/

public final class And<T> implements UnaryConstraint<T>
{
  private static final long serialVersionUID = 1L;

  // Set of constraints.
  private final Set<UnaryConstraint<T>> value;

  /**
   * Class constructor.
   *
   * @param s  the set of constraints.
   *
  **/
  public And(final Set<UnaryConstraint<T>> s)
  {
    this.value = s;
  }

  /**
   * Class constructor.
   *
   * @param s  the set of constraints.
   *
  **/
  @SafeVarargs
  public And(final UnaryConstraint<T>... s)
  {
    this.value = new HashSet<>();

    for (UnaryConstraint<T> c : s)
    {
      this.value.add(c);
    }
  }

  /**
   * Gets the set of constraints.
   *
   * @return the constraints.
   *
  **/
  public Set<UnaryConstraint<T>> getValue()
  {
    return this.value;
  }


  /** {@inheritDoc} **/
  @Override
  public boolean eval(final T o)
  {
    for (UnaryConstraint<T> i : this.value)
    {
      if (!i.eval(o))
      {
        return false;
      }
    }

    return true;
  }
}
