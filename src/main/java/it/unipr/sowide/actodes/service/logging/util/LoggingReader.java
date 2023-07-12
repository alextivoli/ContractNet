package it.unipr.sowide.actodes.service.logging.util;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.service.logging.content.Configured;
import it.unipr.sowide.actodes.service.logging.content.Execution;
import it.unipr.sowide.actodes.service.logging.content.Log;
import it.unipr.sowide.actodes.service.logging.content.Processed;
import it.unipr.sowide.actodes.service.logging.content.Step;

/**
 * The {@code LoggingReader} defines a reader for binary logging information
 * coming from a socket writers.
 *
 * In particular, it prints on console information about the plant devices
 * for each step of the simulation.
 *
**/

public class LoggingReader implements Runnable
{
  private static final int DEFAULTPORT = 26388;

  private ServerSocket server = null;

  /**
   * Class constructor.
   *
   * It uses the default TCP/IP port.
   *
  **/
  public LoggingReader()
  {
    try
    {
        this.server = new ServerSocket(DEFAULTPORT);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Class constructor.
   *
   * @param p  the TCP/IP port.
   *
  **/
  public LoggingReader(final int p)
  {
    try
    {
       this.server = new ServerSocket(p);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Reads the logging data.
   *
  **/
  public void run()
  {
    ObjectInputStream is;

    try
    {
      Socket s = this.server.accept();

      is = new ObjectInputStream(s.getInputStream());

      while (true)
      {
        Object o = is.readObject();

        if ((o != null) && (o instanceof Log))
        {
          if (o instanceof Step)
          {
            Step step = (Step) o;

            System.out.println("step " + step.getTime());
          }
          else if (o instanceof Processed)
          {
            Processed p = (Processed) o;

            System.out.println("proc " + p.getState());
          }
          else if (o instanceof Configured)
          {
            Configuration c = ((Configured) o).getConfiguration();

            System.out.println("width  " + c.get("width"));
            System.out.println("height " + c.get("height"));
            System.out.println("length " + c.get("length"));
          }
          else if (o instanceof Execution)
          {
            break;
          }
        }
      }
    }
    catch (Exception e)
    {
      //e.printStackTrace();
    }
  }

  /**
   * Starts the reading of the logging data.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    new Thread(new LoggingReader()).start();
  }
}
