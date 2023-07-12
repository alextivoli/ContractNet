package it.unipr.sowide.actodes.filtering.constraint;

/**
 *
 * The {@code IsInstance} class defines a type constraints.
 *
 * It is satisfied by the objects that belong to
 * the class associated with such a constraint.
 *
**/

public final class IsInstance implements UnaryConstraint<Object>
{
  private static final long serialVersionUID = 1L;

  // Class.
  private final Class<?> type;

  /**
   * Class constructor.
   *
   * @param c  the class.
   *
  **/
  public IsInstance(final Class<?> c)
  {
    this.type = c;
  }

  /**
   * Gets the class.
   *
   * @return the class.
   *
  **/
  public Class<?> getType()
  {
    return this.type;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean eval(final Object o)
  {
    return this.type.isInstance(o);
  }

}
