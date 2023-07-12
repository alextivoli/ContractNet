package it.unipr.sowide.actodes.service.logging;

import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.logging.content.Changed;
import it.unipr.sowide.actodes.service.logging.content.Created;
import it.unipr.sowide.actodes.service.logging.content.DataLoad;
import it.unipr.sowide.actodes.service.logging.content.DataSave;
import it.unipr.sowide.actodes.service.logging.content.Destroyed;
import it.unipr.sowide.actodes.service.logging.content.Execution;
import it.unipr.sowide.actodes.service.logging.content.Failure;
import it.unipr.sowide.actodes.service.logging.content.Initialized;
import it.unipr.sowide.actodes.service.logging.content.Initializing;
import it.unipr.sowide.actodes.service.logging.content.Processed;
import it.unipr.sowide.actodes.service.logging.content.Processing;
import it.unipr.sowide.actodes.service.logging.content.Runtime;
import it.unipr.sowide.actodes.service.logging.content.Sent;
import it.unipr.sowide.actodes.service.logging.content.Step;
import it.unipr.sowide.actodes.service.logging.content.Unprocessed;

/**
 *
 * The {@code TextualFormatter} class provides the methods for formatting
 * logging messages in a human readable format.
 *
**/

public final class TextualFormatter extends Formatter
{
  // String formats.
  private String rcinf;
  private String acinf;
  private String asinf;
  private String iminf;
  private String ominf;
  private String spinf;
  private String epinf;
  private String siinf;
  private String eiinf;
  private String scinf;
  private String akinf;
  private String etinf;
  private String ecinf;
  private String clinf;
  private String ifinf;
  private String ofinf;
  private String r0inf;
  private String r1inf;
  private String r2inf;
  private String r3inf;

  /**
   * Class constructor.
   *
  **/
  public TextualFormatter()
  {
    ResourceBundle bundle = ResourceBundle.getBundle("messages_en");

    if (bundle != null)
    {
      this.acinf = bundle.getString("acinf");
      this.asinf = bundle.getString("asinf");
      this.iminf = bundle.getString("iminf");
      this.ominf = bundle.getString("ominf");
      this.spinf = bundle.getString("spinf");
      this.epinf = bundle.getString("epinf");
      this.siinf = bundle.getString("siinf");
      this.eiinf = bundle.getString("eiinf");
      this.scinf = bundle.getString("scinf");
      this.akinf = bundle.getString("akinf");
      this.etinf = bundle.getString("etinf");
      this.ecinf = bundle.getString("ecinf");
      this.clinf = bundle.getString("clinf");
      this.ifinf = bundle.getString("ifinf");
      this.ofinf = bundle.getString("ofinf");
      this.r0inf = bundle.getString("r0inf");
      this.r1inf = bundle.getString("r1inf");
      this.r2inf = bundle.getString("r2inf");
      this.r3inf = bundle.getString("r3inf");
    }
  }

  /** {@inheritDoc} **/
  @Override
  public String format(final LogRecord rec)
  {
	if (rec.getParameters() == null)
	{
      return "";
	}
	else
	{
      return format(rec.getParameters()[0]);
	}
  }

  /**
   * Builds the logging message for a logging event.
   *
   * @param o  the logging event.
   *
   * @return the logging message.
   *
  **/
  public String format(final Object o)
  {
    final DecimalFormat lf = new DecimalFormat("###,###");

    switch (o.getClass().getName())
    {
      case "it.unipr.sowide.actodes.service.logging.content.Created":
        Created c = (Created) o;

        if (c.getReference() != null)
        {
          return String.format(this.acinf,
              getReferenceName(c.getReference()),
              getReferenceName(c.getChild()),
              getShortName(c.getChildBehavior()));
        }

        return String.format(this.rcinf, getReferenceName(c.getChild()),
            getShortName(c.getChildBehavior()));
      case "it.unipr.sowide.actodes.service.logging.content.Initializing":
        Initializing u = (Initializing) o;
        if (u.getOldBehavior() != null)
        {
          return String.format(this.siinf, getReferenceName(u.getReference()),
              getShortName(u.getOldBehavior()),
              getShortName(u.getBehavior()));
        }

        return String.format(this.asinf, getReferenceName(u.getReference()),
            getShortName(u.getBehavior()));
      case "it.unipr.sowide.actodes.service.logging.content.Initialized":
        Initialized h = (Initialized) o;
        return String.format(this.eiinf, getInitReferenceName(h.getReference()),
            getShortName(h.getBehavior()));
      case "it.unipr.sowide.actodes.service.logging.content.Changed":
        Changed i = (Changed) o;
        return String.format(this.scinf, getReferenceName(i.getReference()),
            getShortName(i.getBehavior()), i.getState());
      case "it.unipr.sowide.actodes.service.logging.content.Sent":
        Sent m = (Sent) o;
        return String.format(this.ominf, getReferenceName(m.getReference()),
            getSimpleName(m.getMessage().getContent()),
            getShortName(m.getMessage().getContent().toString()),
            getReferenceName(m.getMessage().getReceiver()));
      case "it.unipr.sowide.actodes.service.logging.content.Processing":
        Processing j = (Processing) o;
        return String.format(this.spinf, getReferenceName(j.getReference()),
            getShortName(j.getBehavior()),
            getSimpleName(j.getMessage().getContent()),
            getShortName(j.getMessage().getContent().toString()),
            getReferenceName(j.getMessage().getSender()));
      case "it.unipr.sowide.actodes.service.logging.content.Processed":
        Processed l = (Processed) o;
        return String.format(this.epinf, getReferenceName(l.getReference()),
            getShortName(l.getBehavior()),
            getSimpleName(l.getMessage().getContent()),
            getShortName(l.getMessage().getContent().toString()),
            getReferenceName(l.getMessage().getSender()));
      case "it.unipr.sowide.actodes.service.logging.content.Unprocessed":
        Unprocessed k = (Unprocessed) o;
        return String.format(this.iminf, getReferenceName(k.getReference()),
            getSimpleName(k.getMessage().getContent()),
            getShortName(k.getMessage().getContent().toString()),
            getReferenceName(k.getMessage().getSender()));
      case "it.unipr.sowide.actodes.service.logging.content.Destroyed":
        Destroyed d = (Destroyed) o;
        return String.format(this.akinf, getReferenceName(d.getReference()),
            getShortName(d.getBehavior()));
      case "it.unipr.sowide.actodes.service.logging.content.Step":
        Step w = (Step) o;
        return String.format(this.clinf, getReferenceName(w.getReference()),
            w.getTime(), w.getRunning());
      case "it.unipr.sowide.actodes.service.logging.content.DataLoad":
        DataLoad y = (DataLoad) o;
        return String.format(this.ifinf, y.getName(), y.getFile());
      case "it.unipr.sowide.actodes.service.logging.content.DataSave":
        DataSave z = (DataSave) o;
        return String.format(this.ofinf, z.getName(), z.getFile());
      case "it.unipr.sowide.actodes.service.logging.content.Runtime":
        Runtime r = (Runtime) o;

        if (r.getResult() != null)
        {
          if ((r.getParameters() != null) && (r.getParameters().length > 0))
          {
            Object[] p = r.getParameters();
            int n      = 0;
            String s   = p[n++].toString();

            while (n < p.length)
            {
              s = s + ", " + p[n++];
            }

            return String.format(this.r3inf, r.getRuntime(),
                getShortName(r.getMethod()), s, r.getResult());
          }

          return String.format(this.r2inf, r.getRuntime(),
              getShortName(r.getMethod()), r.getResult());
        }

        if ((r.getParameters() != null) && (r.getParameters().length > 0))
        {
          Object[] p = r.getParameters();
          int n      = 0;
          String s   = p[n++].toString();

          while (n < p.length)
          {
            s = s + ", " + p[n++];
          }

          return String.format(this.r1inf, r.getRuntime(),
              getShortName(r.getMethod()), s);
        }

        return String.format(this.r0inf, r.getRuntime(),
            getShortName(r.getMethod()));

      case "it.unipr.sowide.actodes.service.logging.content.Execution":
        Execution e = (Execution) o;

        if (e.getCycles() > 0)
        {
          return String.format(
              this.ecinf, getReferenceName(e.getReference()),
              lf.format(e.getEnd() - e.getStart()), e.getCycles());
        }

        return String.format(
            this.etinf, getReferenceName(e.getReference()),
            lf.format(e.getEnd() - e.getStart()));

      case "it.unipr.sowide.actodes.service.logging.content.Failure":
        Failure f = (Failure) o;
        return String.format(f.getException().getMessage(),
            getReferenceName(f.getReference()));

      default:
        return "";
    }
  }

  private String getInitReferenceName(final Reference r)
  {
    if (r.getName().equals(Behavior.PROVIDER.getName()))
    {
      return r.getName() + " (actor space " + r.getLocation() + ")";
    }
    if (r.getLocation().equals(Behavior.PROVIDER.getLocation()))
    {
      return r.getName();
    }

    return r.toString();
  }

  private String getReferenceName(final Reference r)
  {
    if (r.getLocation().equals(Behavior.PROVIDER.getLocation()))
    {
      return r.getName();
    }

    return r.toString();
  }

  private String getShortName(final String c)
  {
    int n = c.lastIndexOf(".") + 1;

    return c.substring(n);
  }

  private String getSimpleName(final Object c)
  {
    return c.getClass().getSimpleName();
  }
}
