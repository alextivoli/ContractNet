package it.unipr.sowide.actodes.filtering.constraint;

import java.util.Set;

/**
 *
 * The {@code IsOneOf} class defines a membership constraint.
 *
 * It is satisfied by the objects that belong to the set of objects
 * associated with such a constraint.
 *
 * @param <T>  the object type.
 *
**/

public final class IsOneOf<T> implements UnaryConstraint<T>
{
  private static final long serialVersionUID = 1L;

  // Reference objects.
  private final Set<? extends T> value;

  /**
   * Class constructor.
   *
   * @param s  the set of objects.
   *
  **/
  public IsOneOf(final Set<? extends T> s)
  {
    this.value = s;
  }

  /**
   * Gets the set of objects.
   *
   * @return the objects.
   *
  **/
  public Set<? extends T> getValue()
  {
    return this.value;
  }

  /** {@inheritDoc} **/
  @Override
  public  boolean eval(final T o)
  {
    return this.value.contains(o);
  }
}
