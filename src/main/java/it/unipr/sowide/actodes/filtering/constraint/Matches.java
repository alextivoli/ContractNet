package it.unipr.sowide.actodes.filtering.constraint;

import it.unipr.sowide.actodes.filtering.Matcher;
import it.unipr.sowide.actodes.filtering.Pattern;

/**
 *
 * The {@code Matches} class defines a pattern constraint.
 *
 * It is satisfied by the objects that satisfy the pattern associated
 * with such a constraint.
 *
 *
 * @param <T>  the object type.
 * @param <P>  the object pattern.
 *
 *
**/

public final class Matches<T, P extends Pattern> implements UnaryConstraint<T>
{
  private static final long serialVersionUID = 1L;

  // Pattern.
  private final P pattern;
  // Pattern matcher.
  private final Matcher<T, P> matcher;

  /**
   * Class constructor.
   *
   * @param p  the pattern.
   * @param m  the matcher.
   *
  **/
  public Matches(final P p, final Matcher<T, P> m)
  {
    this.pattern = p;
    this.matcher = m;
  }

  /**
   * Gets the pattern.
   *
   * @return the pattern.
   *
  **/
  public Pattern getPattern()
  {
    return this.pattern;
  }

  /**
   * Gets the pattern matcher.
   *
   * @return the matcher.
   *
  **/
  public Matcher<T, P> getMatcher()
  {
    return this.matcher;
  }

  /** {@inheritDoc} **/
  @Override
  public  boolean eval(final T o)
  {
    return this.matcher.match(o, this.pattern);
  }
}
