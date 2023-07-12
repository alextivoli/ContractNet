package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code IsNull} enumeration defines an unbound object field constraint.
 *
 * It is satisfied by the object fields to which no value is assigned.
 *
 * This constraint is applied to non primitive data fields.
 *
 * This enumeration has a single element: <code>INSTANCE</code> and it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
**/

public enum IsNull implements UnaryConstraint<Object>
{
  /**
   * The singleton instance.
   *
  **/
  INSTANCE;

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Object o)
  {
    if (o == null)
    {
      return true;
    }

    return false;
  }
}
