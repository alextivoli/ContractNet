package it.unipr.sowide.actodes.filtering.constraint;

import java.util.Collection;

/**
 *
 * The {@code AggregateConstraint} interface defines a constraint
 * that can be used for filtering different kinds of objects.
 *
 * @param <T>  the object type.
 *
**/

public interface AggregateConstraint<T> extends Constraint<T>
{
  /**
   * Evaluates the constraint.
   *
   * @param o  the object to which applied the constraint.
   *
   * @return <code>true</code> if the object satisfies the constraint.
   *
  **/
  boolean eval(Collection<T> o);
}
