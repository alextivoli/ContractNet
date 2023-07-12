package it.unipr.sowide.actodes.filtering;

import java.lang.reflect.Field;

import it.unipr.sowide.actodes.filtering.constraint.UnaryConstraint;

/**
 *
 * The {@code GenericMatcher} enumeration defines a pattern matcher
 * that filters objects by applying constraints to the value of its fields.
 *
 * This enumeration has a single element: <code>INSTANCE</code> an it is used
 * for proving a thread safe implementation of the singleton pattern.
 *
**/

public enum GenericMatcher implements Matcher<Object, GenericPattern>
{
  /**
   * The singleton instance.
   *
  **/
  INSTANCE;

  /** {@inheritDoc} **/
  @Override
  public boolean match(final Object o, final GenericPattern p)
  {
    if (p == GenericPattern.NOCONSTRAINTS)
    {
      return true;
    }

    Class<?> c = o.getClass();

    for (String n : p.fieldSet())
    {
      UnaryConstraint<Object> v = p.get(n);

      try
      {
        Field f = c.getDeclaredField(n);
        f.setAccessible(true);

        if (!v.eval(f.get(o)))
        {
          return false;
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }

    return true;
  }
}
