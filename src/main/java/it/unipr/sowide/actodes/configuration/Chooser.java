package it.unipr.sowide.actodes.configuration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * The {@code Chooser} class allows to choose a value
 * through a probabilistic decision.
 *
 * @param <T>  the value type.
 *
**/

public class Chooser<T> implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final Random random;
  private final List<T> list;
  private final Map<T, Double> map;
  private final T dv;

  /**
   * Class constructor.
   *
   * @param l  the values-list.
   *
   * It supports the uniform choice among a set of values.
   *
  **/
  public Chooser(final List<T> l)
  {
    this.random = new Random();

    this.list = l;
    this.map  = null;
    this.dv   = null;
  }

  /**
   * Class constructor.
   *
   * @param m  the value-probability pairs.
   *
   * If the sum of the probabilities is less than 1,
   * then in some choices the return value may be <code>null</code>.
   *
   * Moreover, if the sum of the probabilities is greater than 1,
   * then it is possible that some values are never returned.
   *
  **/
  public Chooser(final Map<T, Double> m)
  {
    this.random = new Random();

    this.list = null;
    this.map  = m;
    this.dv   = null;
  }

  /**
   * Class constructor.
   *
   * @param m  the value-probability pairs.
   * @param d  the default value.
   *
   * If the sum of the probabilities is less than 1,
   * then in some choices the return value is the default value.
   *
   * Moreover, if the sum of the probabilities is greater than 1,
   * then it is possible that some values are never returned.
   *
  **/
  public Chooser(final Map<T, Double> m, final T d)
  {
    this.random = new Random();

    this.list = null;
    this.map  = m;
    this.dv   = d;
  }

  /**
   * Chooses one of the possible values.
   *
   * @return the value.
   *
  **/
  public T choose()
  {
    if ((this.list == null) && (this.map != null) && (this.map.size() > 0))
    {
      double p = this.random.nextDouble();

      double t = 0;

      for (Entry<T, Double> e : this.map.entrySet())
      {
        Double v = e.getValue();

        if ((v != null) && (v > 0))
        {
          t += v;

          if (p < t)
          {
            return e.getKey();
          }
        }
      }

      return this.dv;
    }
    else if (this.list.size() > 0)
    {
      int p = this.random.nextInt(this.list.size());

      return this.list.get(p);
    }

    return null;
  }
}
