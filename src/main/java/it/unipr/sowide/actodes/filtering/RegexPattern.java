package it.unipr.sowide.actodes.filtering;

/**
 *
 * The {@code RegexPattern} class defines a pattern for filtering
 * objects. It selects all the objects whose string representation
 * satisfies the regular expression associated with the pattern.
 *
**/

public final class RegexPattern implements Pattern
{
  private static final long serialVersionUID = 1L;

  // Regular expression.
  private final String value;

  /**
   * Class constructor.
   *
   * @param r  the regular expression.
   *
  **/
  public RegexPattern(final String r)
  {
    this.value = r;
  }

  /**
   * Gets the regular expression.
   *
   * @return the regular expression.
   *
  **/
  public String getValue()
  {
    return this.value;
  }
}
