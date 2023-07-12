package it.unipr.sowide.actodes.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code PropertiesGroup} class maintains a set of properties
 * that are usually used for configuring a particular object of an
 * application.
 *
**/

public class PropertiesGroup implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Specific application properties.
  private final Map<String, Serializable> properties;

  /**
   * Class constructor.
   *
  **/
  public PropertiesGroup()
  {
    this.properties = new HashMap<>();
  }

  /**
   * Class constructor.
   *
   * @param p  the set of properties.
   *
  **/
  public PropertiesGroup(final Map<String, Serializable> p)
  {
    this.properties = p;
  }

  /**
   * Gets the set of the names of the properties.
   *
   * @return the names of the properties.
   *
  **/
  public Set<String> getNames()
  {
    return this.properties.keySet();
  }
  /**
   * Gets a property value.
   *
   * @param n  the property name.
   *
   * @return the property value.
   *
  **/
  public Serializable get(final String n)
  {
    return this.properties.get(n);
  }

  /**
   * Gets a property value as a boolean.
   *
   * @param n  the property name.
   *
   * Note that the property value must be saved
   * either as a string or as a boolean.
   *
   * @return the property boolean value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public Boolean getBoolean(final String n)
  {
    Object p = this.properties.get(n);

    if (p == null)
    {
      return null;
    }
    else if (p instanceof Boolean)
    {
      return (Boolean) p;
    }
    else if (p instanceof String)
    {
      try
      {
        return Boolean.parseBoolean((String) p);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return null;
  }

  /**
   * Gets a property value as an array of boolean values.
   *
   * @param n  the property name.
   * @param d  the delimiters.
   *
   * Note that the property value must be saved as a string.
   *
   * @return the array of property boolean values.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public boolean[] getBoolean(final String n, final String d)
  {
    if (n == null)
    {
      return null;
    }

    String[]  s = n.split(d);
    boolean[] l = new boolean[s.length];

    for (int i = 0; i < s.length; i++)
    {
      try
      {
        l[i] = Boolean.parseBoolean(s[i]);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return l;
  }

  /**
   * Gets a property value as a double.
   *
   * @param n  the property name.
   *
   * Note that the property value must be saved
   * either as a string or as a double.
   *
   * @return the property double value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public Double getDouble(final String n)
  {
    Object p = this.properties.get(n);

    if (p == null)
    {
      return null;
    }
    else if (p instanceof Double)
    {
      return (Double) p;
    }
    else if (p instanceof String)
    {
      try
      {
        return Double.parseDouble((String) p);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return null;
  }

  /**
   * Gets a property value as an array of double values.
   *
   * @param n  the property name.
   * @param d  the delimiters.
   *
   * Note that the property value must be saved as a string.
   *
   * @return the array of property double values.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public double[] getDouble(final String n, final String d)
  {
    if (n == null)
    {
      return null;
    }

    String[] s = n.split(d);
    double[] l = new double[s.length];

    for (int i = 0; i < s.length; i++)
    {
      try
      {
        l[i] = Double.parseDouble(s[i]);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return l;
  }

  /**
   * Gets a property value as a float.
   *
   * @param n  the property name.
   *
   * Note that the property value must be saved
   * either as a string or as a float.
   *
   * @return the property float value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public Float getFloat(final String n)
  {
    if (n == null)
    {
      return null;
    }

    Object p = this.properties.get(n);

    if (p == null)
    {
      return null;
    }
    else if (p instanceof Float)
    {
      return (Float) p;
    }
    else if (p instanceof String)
    {
      try
      {
        return Float.parseFloat((String) p);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return null;
  }

  /**
   * Gets a property value as an array of float values.
   *
   * @param n  the property name.
   * @param d  the delimiters.
   *
   * Note that the property value must be saved as a string.
   *
   * @return the array of property float values.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public float[] getFloat(final String n, final String d)
  {
    if (n == null)
    {
      return null;
    }

    String[] s = n.split(d);
    float[] l  = new float[s.length];

    for (int i = 0; i < s.length; i++)
    {
      try
      {
        l[i] = Float.parseFloat(s[i]);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return l;
  }

  /**
   * Parses the a property value as a long.
   *
   * @param n  the property name.
   *
   * Note that the property value must be saved
   * either as a string or as a long.
   *
   * @return the property long value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public Long getLong(final String n)
  {
    Object p = this.properties.get(n);

    if (p == null)
    {

      return null;
    }
    else if (p instanceof Long)
    {
      return (Long) p;
    }
    else if (p instanceof String)
    {
      try
      {
        return Long.parseLong((String) p);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return null;
      }
    }

    return null;
  }

  /**
   * Gets a property value as an array of long values.
   *
   * @param n  the property name.
   * @param d  the delimiters.
   *
   * Note that the property value must be saved as a string.
   *
   * @return the array of property long values.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public long[] getLong(final String n, final String d)
  {
    if (n == null)
    {
      return null;
    }

    String[] s = n.split(d);
    long[] l   = new long[s.length];


    for (int i = 0; i < s.length; i++)
    {
      try
      {
        l[i] = Long.parseLong(s[i]);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return l;
  }

  /**
   * Gets a property value as a signed decimal integer.
   *
   * @param n  the property name.
   *
   * Note that the property value must be saved
   * either as a string or as an integer.
   *
   * @return the property integer value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public Integer getInt(final String n)
  {
    Object p = this.properties.get(n);

    if (p == null)
    {
      return null;
    }
    else if (p instanceof Integer)
    {
      return (Integer) p;
    }
    else if (p instanceof String)
    {
      try
      {
        return Integer.parseInt((String) p);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return null;
  }

  /**
   * Gets a property value as an array of integer values.
   *
   * @param n  the property name.
   * @param d  the delimiters.
   *
   * Note that the property value must be saved as a string.
   *
   * @return the array of property integer values.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public int[] getInt(final String n, final String d)
  {
    if (n == null)
    {
      return null;
    }

    String[] s = n.split(d);
    int[] l    = new int[s.length];

    for (int i = 0; i < s.length; i++)
    {
      try
      {
        l[i] = Integer.parseInt(s[i]);
      }
      catch (Exception e)
      {
        return null;
      }
    }

    return l;
  }

  /**
   * Gets the value of a property as a string.
   *
   * @param n  the property name.
   *
   * @return the property string value.
   *
   * Note that the return value is <code>null</code>
   * if the operation failed.
   *
  **/
  public String getString(final String n)
  {
    if (n == null)
    {
      return null;
    }

    Object p = this.properties.get(n);

    if (p == null)
    {
      return null;
    }
    else if (p instanceof String)
    {
      return (String) p;
    }

    return null;
  }

  /**
   * Adds a property.
   *
   * @param n  the property name.
   * @param v  the property value.
   *
  **/
  public void addProperty(final String n, final Serializable v)
  {
    this.properties.put(n, v);
  }

  /**
   * Loads a set of properties from a properties file.
   *
   * @param f  the properties file path.
   *
   * @return <code>true</code> if the operation is successful.
   *
  **/
  public boolean loadProperties(final String f)
  {
    try
    {
      File e = new File(Paths.get(f).toString());

      Properties c = new Properties();

      c.load(new FileInputStream(e));

      for (String s : c.stringPropertyNames())
      {
        this.properties.put(s, (Serializable) c.get(s));
      }

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }
}
