package it.unipr.sowide.actodes.distribution.partitioner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unipr.sowide.actodes.distribution.partitioner.content.Partition;

/**
 *
 * The {@code Master} class defines an actor executor for distributed cellular
 * automata simulations that drives the work of the actor executors of the
 * other actor spaces participating in the simulation.
 *
 * Moreover, it performs the simulation of a partition of a distributed
 * cellular automata.
 *
**/

public class Partitioner<L, P>
{
  private final L environment;
  private final Map<AgentFactory<P>, Double> map;
  private final int parts;

  /**
   * Class constructor.
   *
   * @param w  the cellular automata dimensions.
   * @param m  the cell factory - probability map.
   * @param n  the number of partitions.
   *
  **/
  public Partitioner(final L w,
      final Map<AgentFactory<P>, Double> m, final int n)
  {
    this.environment = w;
    this.map   = m;
    this.parts = n;
  }

  /**
   * Defines the partitions.
   *
   * Each partition is a rectangle defined by its width and height and
   * by the coordinates of the left-bottom corner of the rectangle.
   *
   * @return the partitions.
   *
  **/
/*  public List<Partition<L, P>> define()
  {
    Double v = Math.sqrt(parts);

    int b = (int) Math.floor(v);
    int a = (int) Math.ceil(v);

    if ((parts == (b * b)) || (parts == (a * a)))
    {
      if (parts == (b * b))
      {
        a = b;
      }

      //return partition1(parts, a);
    }
    else
    {
      //return partition2(parts, a, b);
    }

  }
/*
  private List<Partition> partition1(final int p, final int a)
  {
    List<Partition> partitions = new ArrayList<>();

    int pw;
    int ph;

    int cx = 0;
    int cy = 0;
    int rs = a;
    int cs = a;
    int tw = this.environment.getX() / a;
    int th = this.environment.getY() / a;
    int fw = this.environment.getX() - (tw * (cs - 1));
    int fh = this.environment.getY() - (th * (rs - 1));

    for (int x = 0; x < cs; x++)
    {
      if (x == 0)
      {
        pw = fw;
        cx = 0;
      }
      else
      {
        pw = tw;
      }

      for (int y = 0; y < rs; y++)
      {
        if (y == 0)
        {
          ph = fh;
          cy = 0;
        }
        else
        {
          ph = th;
        }

        partitions.add(new Partition(new IntPair(pw, ph),
            new IntPair(cx, cy), this.environment, this.map));

        cy += ph;
      }

      cx += pw;
    }

    return partitions;
  }

  private List<Partition> partition2(final int p, final int a, final int b)
  {
    List<Partition> partitions = new ArrayList<>();

    int pw;
    int ph;
    int r;
    int s;

    int cx = 0;
    int cy = 0;

    if (p <= (b * (b + 1)))
    {
      r = (b * (b + 1)) - p;
      s = p - (b * b);
    }
    else
    {
      r = ((b + 1) * (b + 1)) - p;
      s = p - (b * (b + 1));
    }
/*
    int rw = this.environment.getX() / b;
    int rh = (this.environment.getY() * b) / p;
    int sw = this.environment.getX() / (b + 1);
    int sh = (this.environment.getY() * (b + 1)) / p;
    int fw = this.environment.getX() - (rw * (b - 1));
    int fh = this.environment.getY() - (rh * (r - 1)) - (sh * s);

    for (int x = 0; x < b; x++)
    {
      if (x == 0)
      {
        pw = fw;
        cx = 0;
      }
      else
      {
        pw = rw;
      }

      for (int y = 0; y < r; y++)
      {
        if (y == 0)
        {
          ph = fh;
          cy = 0;
        }
        else
        {
          ph = rh;
        }

        partitions.add(new Partition(new IntPair(pw, ph),
            new IntPair(cx, cy), this.environment, this.map));

        cy += ph;
      }

      cx += pw;
    }

    int b1 = b + 1;

    fw = this.environment.getX() - (sw * b);
    ph = sh;

    int iy = cy;

    for (int x = 0; x < b1; x++)
    {
      if (x == 0)
      {
        pw = fw;
        cx = 0;
      }
      else
      {
        pw = sw;
      }

      cy = iy;

      for (int y = 0; y < s; y++)
      {
        partitions.add(new Partition(new IntPair(pw, ph),
            new IntPair(cx, cy), this.environment, this.map));

        cy += ph;
      }

      cx += pw;
    }

    return partitions;
  }*/
}
