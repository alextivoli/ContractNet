package it.unipr.sowide.actodes.distribution.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.distribution.Connection;
import it.unipr.sowide.actodes.distribution.Connector;
import it.unipr.sowide.actodes.error.ErrorManager;
import it.unipr.sowide.actodes.registry.Reference;

/**
 *
 * The {@code RmiConnector} class manages the communication
 * towards remote actor spaces taking advantage of RMI software.
 *
**/

public final class RmiConnector extends Connector
{
  private static final long serialVersionUID = 1L;

  private static final String ADDR = "127.0.0.1";
  private static final int PORT    = 1099;

  // Remote object key.
  private static final String KEY = "remotekey";
  // sleep time for waiting the shutdown.
  private static final long SLEEPTIME = 10;

  // RMI registry
  private Registry registry;
  // RMI proxy.
  private RmiProxy proxy;
  // RMI proxy registry.
  private RmiHub hub;
  // RMI stub.
  private Remote stub;
  // Embedded registry flag.
  private boolean embedded;

  /**
   * Class constructor.
   *
   * This connector uses the default configuration
   * and does not act as communication broker.
   *
  **/
  protected RmiConnector()
  {
    this.embedded = false;

    configure(ADDR, PORT);
  }

  /**
   * Class constructor.
   *
   * @param f  a boolean flag indicating if the connector act as communication
   * broker (value = <code>true</code>) or not (value = <code>false</code>).
   *
   * This connector uses the default configuration.
   *
   */
  public RmiConnector(final boolean f)
  {
    this.embedded = f;

    setBroker(f);

    configure(ADDR, PORT);
  }

  /**
   * Class constructor.
   *
   * @param p  the IP port.
   *
   * This connector acts as communication broker.
   *
   */
  public RmiConnector(final int p)
  {
    this.embedded = true;

    setBroker(true);

    configure(null, p);
  }

  /**
   * Class constructor.
   *
   * @param a  the IP address.
   * @param p  the IP port.
   *
   * This connector does not act as communication broker.
   *
   */
  public RmiConnector(final String a, final int p)
  {
    this.embedded = false;

    configure(a, p);
  }

  private void configure(final String a, final int p)
  {
    try
    {
      if (isBroker())
      {
        // Creates an embedded RMI registry.
        this.registry = LocateRegistry.createRegistry(p);
      }
      else
      {
        if (a.equals("127.0.0.1"))
        {
          this.registry = LocateRegistry.getRegistry(p);
        }
        else
        {
          this.registry = LocateRegistry.getRegistry(a, p);
        }
      }
    }
    catch (Exception e)
    {
      ErrorManager.kill(e);
    }
  }

  /** {@inheritDoc} **/
  @Override
  public void start()
  {
    try
    {
      if (this.embedded)
      {
        this.hub = new RmiHubImpl(this, Behavior.PROVIDER);
        this.proxy = this.hub;

        this.hub.add(Behavior.PROVIDER, this.proxy);

        this.stub = UnicastRemoteObject.exportObject(this.proxy, 0);

        this.registry.rebind(KEY, this.stub);
      }
      else
      {
        this.proxy = new RmiProxyImpl(this, Behavior.PROVIDER);
        this.stub = UnicastRemoteObject.exportObject(this.proxy, 0);
        this.hub = (RmiHub) this.registry.lookup(KEY);

        if (this.hub == null)
        {
          new IllegalArgumentException();
        }

        this.hub.add(Behavior.PROVIDER, this.proxy);
      }
    }
    catch (Exception e)
    {
      ErrorManager.kill(e);
    }

    super.start();
  }

  /** {@inheritDoc} **/
  @Override
  protected Set<Reference> discover()
  {
    HashSet<Reference> s = new HashSet<Reference>();

    try
    {
      for (RmiProxy p : this.hub.list())
      {
        Reference e = p.getDestination();

        if (!Behavior.PROVIDER.equals(e))
        {
          s.add(e);
        }
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    return s;
  }

  /** {@inheritDoc} **/
  @Override
  protected Connection create(final Reference d)
  {
    try
    {
      RmiProxy p = this.hub.get(d);

      if (p != null)
      {
        return new RmiConnection(d, p);
      }
    }
    catch (Exception e)
    {
      ErrorManager.notify(e);
    }

    return null;
  }

  /** {@inheritDoc} **/
  @Override
  protected void destroy(final Reference d)
  {
    try
    {
      if (this.hub.get(d) != null)
      {
        this.hub.remove(d);
      }
    }
    catch (Exception e)
    {
      return;
    }
  }

  /** {@inheritDoc} **/
  @Override
  protected void destroy()
  {
    try
    {
      UnicastRemoteObject.unexportObject(this.proxy, true);

      if (this.hub != null)
      {
        this.hub.remove(Behavior.PROVIDER);
      }

      if (this.embedded)
      {
        while (this.hub.list().length != 0)
        {
          try
          {
            Thread.sleep(SLEEPTIME);
          }
          catch (InterruptedException e)
          {
            break;
          }
        }

        this.registry.unbind(KEY);
      }

      UnicastRemoteObject.unexportObject(this.hub, true);
      this.registry = null;
    }
    catch (Exception e)
    {
      return;
    }
  }

  // Class implementing the Proxy interface.
  private final class RmiProxyImpl implements RmiProxy
  {
    // Connector.
    private final Connector connector;
    // Local domain.
    private final Reference reference;

    // Class constructor.
    private RmiProxyImpl(final Connector c, final Reference d)
        throws RemoteException
    {
      this.connector = c;
      this.reference = d;
    }

    /** {@inheritDoc} **/
    @Override
    public Reference getDestination() throws Exception
    {
      return this.reference;
    }

    /** {@inheritDoc} **/
    @Override
    public void forward(final Message m) throws Exception
    {
      this.connector.manage(m);
    }
  }

  // Class implementing the Hub interface.
  private final class RmiHubImpl implements RmiHub
  {
    // Connector.
    private final Connector connector;
    // Local runtime node domain.
    private final Reference domain;
    // Domain proxy map.
    private final ConcurrentMap<Reference, RmiProxy> proxies;

    // Class constructor.
    private RmiHubImpl(final Connector c, final Reference d)
        throws RemoteException
    {
      this.connector = c;
      this.domain    = d;
      this.proxies   = new ConcurrentHashMap<Reference, RmiProxy>();
    }

    /** {@inheritDoc} **/
    @Override
    public Reference getDestination() throws Exception
    {
      return this.domain;
    }

    /** {@inheritDoc} **/
    @Override
    public void forward(final Message m) throws Exception
    {
      this.connector.manage(m);
    }

    /** {@inheritDoc} **/
    @Override
    public RmiProxy get(final Reference d) throws Exception
    {
      return this.proxies.get(d);
    }

    /** {@inheritDoc} **/
    @Override
    public void add(final Reference d, final RmiProxy p) throws Exception
    {
      this.proxies.put(d, p);
    }

    /** {@inheritDoc} **/
    @Override
    public void remove(final Reference d) throws Exception
    {
      this.proxies.remove(d);
    }

    /** {@inheritDoc} **/
    @Override
    public RmiProxy[] list() throws Exception
    {
      return this.proxies.values().toArray(new RmiProxy[0]);
    }
  }
}
