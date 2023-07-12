package it.unipr.sowide.actodes.service.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code FileStorer} class provides a file based persistent storage
 * service.
 *
**/

public final class FileStorer implements Storer
{
  private static final long serialVersionUID = 1L;

  /**
   * Defines the persistent storage repository as a sub-directory
   * of the working directory.
   *
  **/
  public static final String REPOSITORY = "./storage";

  private static final String ACTORDIR     = "./bin/actors";
  private static final String ACTOREXT     = ".act";
  private static final String SNAPSHOTNAME = "snapshot.data";

  // Actors storage directory.
  private String actors;

  private boolean temporary;
  /**
   * Class constructor.
   *
   * It creates a persistent file storage for maintaining the snapshots
   * and the actors of an actor spaces.
   *
  **/
  public FileStorer()
  {
    this.actors = null;

    this.temporary = false;
  }

  /** {@inheritDoc} **/
  @Override
  public boolean save(final Configuration c, final String i)
  {
    String s;

    try
    {
      s = Paths.get(REPOSITORY, URLEncoder.encode(i, "UTF-8")).toString();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }

    File f = new File(s);

    if (!f.exists())
    {
      f.mkdirs();
    }
    else
    {
      return false;
    }

    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
            Paths.get(s, SNAPSHOTNAME).toString()))))
    {
      out.writeObject(c);

      if (this.actors != null)
      {
        File d = new File(this.actors);

        if ((d != null) && d.exists() && d.isDirectory())
        {
          for (File e : d.listFiles())
          {
            Files.copy(
                new FileInputStream(e), Paths.get(s, e.getName()));
          }
        }
      }

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }

  /** {@inheritDoc} **/
  @Override
  public Configuration retrieve(final String i)
  {
    String s;

    try
    {
      s = Paths.get(REPOSITORY, URLEncoder.encode(i, "UTF-8")).toString();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return null;
    }

    File f = new File(s);

    if (!f.exists() || !f.isDirectory())
    {
      return null;
    }

    try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
        new FileInputStream(Paths.get(s, SNAPSHOTNAME).toString()))))
    {
      Configuration c = (Configuration) in.readObject();

      if (f.listFiles().length > 1)
      {
        create();

        for (File e : f.listFiles())
        {
          if (!e.getName().equals(SNAPSHOTNAME))
          {
            Files.copy(
                new FileInputStream(e), Paths.get(this.actors, e.getName()));
          }
        }
      }

      return c;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return null;
    }
  }

  // Creates the directory where store or retrieve the actors.
  private void create()
  {
    final String dataFormat = "yyyy-MM-dd-HH-mm-ss";

    String h = String.valueOf(Behavior.PROVIDER.getLocation().hashCode());
    String t = new SimpleDateFormat(dataFormat).format(
        Calendar.getInstance().getTime());

    this.actors = Paths.get(ACTORDIR, h + t).toString();

    File d = new File(this.actors);

    d.mkdirs();

    this.temporary = true;
  }

  /** {@inheritDoc} **/
  @Override
  public void remove(final String i)
  {
    String s;

    try
    {
      s = Paths.get(REPOSITORY, URLEncoder.encode(i, "UTF-8")).toString();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return;
    }

    clear(s);
  }

  // Removes a directory and its files
  private void clear(final String p)
  {
    File d = new File(p);

    if ((d != null) && d.exists())
    {
      for (File f : d.listFiles())
      {
        f.delete();
      }

      d.delete();
    }

    File f = new File(ACTORDIR);

    if ((f.isDirectory()) && (f.listFiles().length == 0))
    {
      f.delete();
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized Actor load(final String i)
  {
    if (this.actors == null)
    {
      return null;
    }

    String f;

    if (i.endsWith(ACTOREXT))
    {
      f = this.actors + File.separator + i;
    }
    else
    {
      String n;

      try
      {
        n = URLEncoder.encode(i, "UTF-8");
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);

        return null;
      }

      f = this.actors + File.separator + n + ACTOREXT;
    }

    try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
        new FileInputStream(f))))
    {
      return (Actor) in.readObject();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return null;
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized boolean store(final Actor a, final String i)
  {
    if (this.actors == null)
    {
      create();
    }

    String n;

    try
    {
      n = URLEncoder.encode(i, "UTF-8");
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }

    String f = this.actors + File.separator + n + ACTOREXT;

    try (ObjectOutputStream out = new ObjectOutputStream(
        new BufferedOutputStream(new FileOutputStream(f))))
    {
      out.writeObject(a);
      out.flush();
      out.close();

      return true;
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);

      return false;
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void delete(final String i)
  {
    if (this.actors != null)
    {
      String n;

      try
      {
        n = URLEncoder.encode(i, "UTF-8");
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);

        return;
      }

      File f = new File(this.actors + File.separator  + n + ACTOREXT);

      if (f.exists())
      {
        f.delete();
      }
    }
  }

  /** {@inheritDoc} **/
  @Override
  public String getId()
  {
    return this.actors;
  }

  /** {@inheritDoc} **/
  @Override
  public Set<String> list()
  {
    HashSet<String> set = new HashSet<>();

    if (this.actors != null)
    {
      File d = new File(this.actors);

      for (String f : d.list())
      {
        try
        {
          String n = URLDecoder.decode(
              f.substring(0, f.indexOf(ACTOREXT)), "UTF-8");

          set.add(n);
        }
        catch (Exception e)
        {
          ErrorManager.notify(e);

          continue;
        }
      }
    }

    return set;
  }

  /** {@inheritDoc} **/
  @Override
  public int size()
  {
    if (this.actors != null)
    {
      File d = new File(this.actors);

      return d.list().length;
    }

    return 0;
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
  }

  /** {@inheritDoc} **/
  @Override
  public void shutdown()
  {
    if ((this.actors != null) || this.temporary)
    {
      clear(this.actors);
    }
  }
}
