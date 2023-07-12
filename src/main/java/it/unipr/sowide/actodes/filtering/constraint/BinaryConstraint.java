package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code Individual} interface defines a constraint that can be used
 * for filtering different kinds of objects.
 *
 * @param <F>  the first object type.
 * @param <S>  the second object type.
 *
**/

public interface BinaryConstraint<F, S> extends Constraint<F>
{
  /**
   * Evaluates the constraint.
   *
   * @param f  the first object to which applied the constraint.
   * @param s  the second object to which applied the constraint.
   *
   * @return <code>true</code> if the two objects satisfy the constraint.
   *
  **/
  boolean eval(F f, S s);
}
