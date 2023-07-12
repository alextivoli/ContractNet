package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code Individual} interface defines a constraint that can be used
 * for filtering different kinds of objects.
 *
 * @param <T>  the object type.
 *
**/

public interface UnaryConstraint<T> extends Constraint<T>
{
  /**
   * Evaluates the constraint.
   *
   * @param o  the object to which applied the constraint.
   *
   * @return <code>true</code> if the object satisfies the constraint.
   *
  **/
  boolean eval(T o);
}
