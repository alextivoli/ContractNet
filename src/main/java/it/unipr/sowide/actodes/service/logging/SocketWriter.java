package it.unipr.sowide.actodes.service.logging;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import it.unipr.sowide.actodes.error.ErrorManager;

/**
 *
 * The {@code SocketWriter} defines a writer performing
 * its output on a TCP socket.
 *
 * Its implementation is based on the Java Logging API.
 *
 * @see java.util.logging.Handler Handler
 *
**/

public final class SocketWriter extends Handler implements Writer
{
  private static final long serialVersionUID = 1L;

  private static final String DEFAULTADDRESS = "localhost";
  private static final int DEFAULTPORT       = 26388;

  private Socket socket;
  private ObjectOutputStream os;
  private String tcpHost;
  private int tcpPort;

  /**
   * Class constructor.
   *
   * Creates a socket writer with the default TCP/IP address and port.
   *
  **/
  public SocketWriter()
  {
    this.tcpHost = DEFAULTADDRESS;
    this.tcpPort = DEFAULTPORT;

    try
    {
      this.socket = new Socket(this.tcpHost, this.tcpPort);
      this.os     = new ObjectOutputStream(this.socket.getOutputStream());
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  /**
   * Class constructor.
   *
   * @param a  the TCP/IP address
   * @param p  the port.
   *
  **/
  public SocketWriter(final String a, final int p)
  {
    this.tcpHost = a;
    this.tcpPort = p;

    try
    {
      this.socket = new Socket(this.tcpHost, this.tcpPort);
      this.os     = new ObjectOutputStream(this.socket.getOutputStream());
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  @Override
  public synchronized void publish(final LogRecord record)
  {
    if (isLoggable(record))
    {
      try
      {
        this.os.writeObject(record.getParameters()[0]);
      }
      catch (Exception e)
      {
        ErrorManager.notify(e);
      }
    }
  }

  /** {@inheritDoc} **/
  @Override
  public synchronized void flush()
  {
    try
    {
      this.os.flush();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }

  @Override
  public synchronized void close()
  {
    try
    {
      this.os.close();
      this.socket.close();
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }
  }
}
