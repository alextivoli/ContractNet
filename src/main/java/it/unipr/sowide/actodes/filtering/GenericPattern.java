package it.unipr.sowide.actodes.filtering;

import java.util.HashMap;
import java.util.Set;

import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.filtering.constraint.UnaryConstraint;

/**
 *
 * The {@code GenericPattern} class defines a pattern for filtering any type of
 * object applying some constraints on the value of its fields.
 *
**/

public final class GenericPattern implements Pattern
{
  private static final long serialVersionUID = 1L;

  /**
   * Instance of a generic pattern that accepts all the objects.
   *
  **/
  public static final GenericPattern NOCONSTRAINTS = new GenericPattern();

  private final HashMap<String, UnaryConstraint<Object>> pattern;

  // Class constructor for an empty pattern, i.e., a pattern that matches
  // all the objects.
  private GenericPattern()
  {
    this.pattern = new HashMap<>();
  }

  /**
   * Class constructor.
   *
   * @param p
   *
   * an array containing a sequence of field key - constraint value pairs.
   *
  **/
  @SuppressWarnings("unchecked")
  public GenericPattern(final Object... p)
  {
    this.pattern = new HashMap<>();

    int i = 0;

    while (i < p.length)
    {
      if (p[i] instanceof String)
      {
        String k = (String) p[i++];

        if ((i < p.length) && (p[i] instanceof UnaryConstraint))
        {
          this.pattern.put(k, (UnaryConstraint<Object>) p[i]);

          i++;
        }
        else
        {
          if (i < p.length)
          {
            ErrorManager.notify(new IllegalArgumentException(p[i].toString()));
          }
          else
          {
            ErrorManager.notify(new ArrayIndexOutOfBoundsException(p.length));

            return;
          }
        }
      }
      else
      {
        ErrorManager.notify(new IllegalArgumentException(p[i].toString()));
      }
    }
  }

  /**
   * Gets the names of the constrained fields.
   *
   * @return the names.
   *
  **/
  public Set<String> fieldSet()
  {
    return this.pattern.keySet();
  }

  /**
   * Gets the constraint associated with a field.
   *
   * @param f  the field name.
   *
   * @return the constraint.
   *
  **/
  public UnaryConstraint<Object> get(final String f)
  {
    return this.pattern.get(f);
  }
}
