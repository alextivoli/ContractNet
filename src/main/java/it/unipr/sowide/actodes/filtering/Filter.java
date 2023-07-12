package it.unipr.sowide.actodes.filtering;

import java.io.Serializable;

/**
 *
 * The {@code Matcher} interface defines a pattern based filters
 * for objects.
 *
 * @param <T>  the object type.
 *
**/

public interface Filter<T> extends Serializable
{
  /**
   * Checks if an object matches a pattern.
   *
   * @param o  the object.
   *
   * @return <code>true</code> if the object matches the pattern.
   *
  **/
  boolean eval(T o);
}
