package it.unipr.sowide.actodes.filtering;

import java.io.Serializable;

/**
 *
 * The {@code Matcher} interface defines a pattern based filters
 * for objects.
 *
 * @param <T>  the object type.
 * @param <P>  the object pattern.
 *
**/

public interface Matcher<T, P extends Pattern> extends Serializable
{
  /**
   * Checks if an object matches a pattern.
   *
   * @param o  the object.
   * @param p  the pattern.
   *
   * @return <code>true</code> if the object matches the pattern.
   *
  **/
  boolean match(T o, P p);
}
