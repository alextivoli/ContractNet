package it.unipr.sowide.actodes.service.logging;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;

import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.service.logging.content.Log;

/**
 * The {@code SoxketReader} defines a reader for binary logging information
 * coming from one or more socket writers.
 *
**/

public class SocketReader
{
  private static final int DEFAULTPORT = 26388;

  private ServerSocket server = null;

  /**
   * Queue maintaining the logging events.
   *
  **/
  protected BlockingDeque<Log> queue;

  /**
   * Class constructor.
   *
   * It uses the default TCP/IP port.
   *
  **/
  public SocketReader()
  {
    try
    {
        this.server = new ServerSocket(DEFAULTPORT);
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Class constructor.
   *
   * @param p  the TCP/IP port.
   *
  **/
  public SocketReader(final int p)
  {
    try
    {
        this.server = new ServerSocket(p);
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Starts the reader.
   *
  **/
  public void listen()
  {
    while (true)
    {
      try
      {
        Socket s = this.server.accept();

        new Thread(new WriterManager(s)).start();
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }
    }
  }

  private class WriterManager implements Runnable
  {
    private ObjectInputStream is;

    WriterManager(final Socket s)
    {
      try
      {
        this.is = new ObjectInputStream(s.getInputStream());
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }
    }

    /** {@inheritDoc} **/
    @Override
    public void run()
    {
      while (true)
      {
        try
        {
          Object o = this.is.readObject();

          if ((o != null) && (o instanceof Log))
          {
            SocketReader.this.queue.add((Log) o);
          }
        }
        catch (Exception e)
        {
          ErrorManager.notify(e);
        }
      }
    }
  }

  /**
   * Gets a logging event.
   *
   * @return the logging event.
   *
  **/
  public Log getLog()
  {
    try
    {
      return this.queue.take();
    }
    catch (Exception e)
    {
      e.printStackTrace();

      return null;
    }
  }
}
