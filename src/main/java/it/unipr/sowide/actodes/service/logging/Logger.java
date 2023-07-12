package it.unipr.sowide.actodes.service.logging;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.BehaviorState;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.executor.Executor;
import it.unipr.sowide.actodes.executor.passive.Scheduler;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.Service;
import it.unipr.sowide.actodes.service.logging.content.Changed;
import it.unipr.sowide.actodes.service.logging.content.Configured;
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
 * The {@code Logger} class logs information about the activities of an
 * actor space.
 *
 * It allows to log: errors, message reception and sending, actor creation
 * and remotion, message and timeout processing, behavior change, number of
 * created actors, and execution time.
 *
 *
 * Note that it always logs the error messages.
 *
 * Its implementation is based on the Java Logging API.
 *
 * @see java.util.logging
 *
**/

public final class Logger implements Service
{
  private static final long serialVersionUID = 1L;

  public static final Logger LOGGER = new Logger();

  /**
   * Defines the logging repository as a sub-directory
   * of the working directory.
   *
  **/
  public static final String REPOSITORY = "./log";

  /**
   * Defines the file extension of textual logging files.
   *
  **/
  public static final String TEXT = "txt";

  /**
   * Defines the file extension of binary logging files.
   *
  **/
  public static final String BINARY = "log";

  /**
   * Logs the creation of actors.
   *
  **/
  public static final int ACTORCREATION = 1;

  /**
   * Logs the starting of the initialization of behaviors.
   *
  **/
  public static final int BEHAVIORINIZIALIZING = 2;

  /**
   * Logs the end of the initialization of behaviors.
   *
  **/
  public static final int BEHAVIORINITIALIZED = 4;
  /**
   * Logs the changing of state.
   *
  **/
  public static final int STATECHANGE = 8;

  /**
   * Logs the sending of messages.
   *
  **/
  public static final int OUTPUTMESSAGE = 16;

  /**
   * Logs the starting of the processing of messages.
   *
  **/
  public static final int MESSAGEPROCESSING = 32;

  /**
   * Logs the end of the processing of messages.
   *
  **/
  public static final int MESSAGEPROCESSED = 64;

  /**
   * Logs the messages that do not match any message pattern of
   * the message pattern - handler pairs of the current behavior.
   *
  **/
  public static final int MESSAGENOMATCHED = 128;

  /**
   * Logs the shutdown of actors.
   *
  **/
  public static final int ACTORSHUTDOWN = 256;

  /**
   * Logs all the actions of actors.
   *
  **/
  public static final int ACTIONS = 511;

  /**
   * Logs the configuration of the actor space.
   *
  **/
  public static final int CONFIGURATION = 512;

  /**
   * Logs the execution time and the number of actors
   * created during the execution of the actor space.
   *
  **/
  public static final int EXECUTION = 1024;

  /**
   * Logs the time when a clock event is sent and
   * the number of actors running in the actor space.
   *
  **/
  public static final int STEP = 2048;

  /**
   * Logs the execution of the dispatcher methods.
   *
  **/
  public static final int DISPATCHER = 4096;

  /**
   * Logs the execution of the registry methods.
   *
  **/
  public static final int REGISTRY = 8192;

  /**
   * Logs the execution of the storer methods.
   *
  **/
  public static final int STORER = 16384;

  /**
   * Logs information about the resource from which data are loaded.
   *
  **/
  public static final int DATALOADING = 32768;

  /**
   * Logs information about the resource where data were saved.
   *
  **/
  public static final int DATASAVED = 65536;

  /**
   * Wrapped Java logging API logger.
   *
  **/
  protected transient java.util.logging.Logger logger;


  // Number of actors.
  private final AtomicInteger actors;

  /**
   * logging filter.
   *
  **/
  private int filter;

  // Class constructor.
  private Logger()
  {
    this.actors = new AtomicInteger(0);

    this.logger = java.util.logging.Logger.getLogger("");

    for (Handler h : this.logger.getHandlers())
    {
      this.logger.removeHandler(h);
    }

    this.filter = 0;
  }

  /**
   * Adds a logging writer.
   *
   * @param w  the writer.
   *
  **/
  public void addWriter(final Writer w)
  {
    this.logger.addHandler((Handler) w);
  }

  /**
   * Removes a logging handler.
   *
   * @param h  the handler.
   *
  **/
  public void removeHandler(final Handler h)
  {
    this.logger.removeHandler(h);
  }

  /**
   * Shutdown the logger.
   *
  **/
  public void shutdown()
  {
    for (Handler h : this.logger.getHandlers())
    {
      h.close();
      this.logger.removeHandler(h);
    }
  }

  /**
   * Gets the logging filter value.
   *
   * @return the value.
   *
  **/
  public int getFilter()
  {
    return this.filter;
  }

  /**
   * Sets the logging filter value.
   *
   * @param f  the value.
   *
  **/
  public void setFilter(final int f)
  {
    this.filter = f;
  }

  /**
   * Logs an execution error.
   *
   * @param r  the actor / actor space reference.
   * @param e  the relative exception.
   *
  **/
  public void logError(final Reference r, final Exception e)
  {
    this.logger.log(Level.SEVERE, "", new Failure(r, e));
  }

  /**
   * Logs the configuration of the actor space.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logConfiguration()
  {
    if ((this.filter & CONFIGURATION) > 0)
    {
      this.logger.log(Level.INFO, "", new Configured(
          Behavior.PROVIDER, SpaceInfo.INFO.getConfiguration()));
    }
  }

  /**
   * Logs the creation of an actor.
   *
   * @param b  the parent behavior.
   * @param n  the child reference.
   * @param c  the child behavior class qualified name.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logActorCreation(
      final Behavior b, final Reference n, final String c)
  {
    if ((this.filter & ACTORCREATION) > 0)
    {
      this.logger.log(Level.INFO, "", new Created(
          b.getReference(), b.getClass().getName(), n, c));
    }

    this.actors.incrementAndGet();
  }

  /**
   * Logs the creation of the first behavior of an actor and
   * the starting of its initialization.
   *
   * @param b  the behavior.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logBehaviorStart(final Behavior b)
  {
    if ((this.filter & BEHAVIORINIZIALIZING) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Initializing(b.getReference(), null, b.getClass().getName()));
    }
  }

  /**
   * Logs the change of a behavior of an actor
   * and the starting of its initialization.
   *
   * @param c  the current behavior.
   * @param n  the new behavior.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logBehaviorStart(final Behavior c, final Behavior n)
  {
    if ((this.filter & BEHAVIORINIZIALIZING) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Initializing(c.getReference(),
              c.getClass().getName(), n.getClass().getName()));
    }
  }

  /**
   * Logs the end of the initialization of a behavior.
   *
   * @param b  the current behavior.
   * @param t  the behavior initialization end time.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logBehaviorInit(final Behavior b, final Long t)
  {
    if ((this.filter & BEHAVIORINITIALIZED) > 0)
    {
      this.logger.log(Level.INFO, "", new Initialized(
          b.getReference(), b.getClass().getName(), b.log(), t));
    }
  }

  /**
   * Logs the change of state of the behavior of an actor.
   *
   * @param b  the current behavior.
   * @param s  the new behavior state.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logStateChange(final Behavior b, final BehaviorState s)
  {
    if ((this.filter & STATECHANGE) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Changed(b.getReference(), b.getClass().getName(), s.toString()));
    }
  }

  /**
   * Logs the sending of a message.
   *
   * @param b  the sender behavior.
   * @param m  the message.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logOutputMessage(final Behavior b, final Message m)
  {
    if ((this.filter & OUTPUTMESSAGE) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Sent(b.getReference(), b.getClass().getName(), m));
    }
  }

  /**
   * Logs the starting of the processing of a message.
   *
   * @param b  the behavior.
   * @param p  the message pattern.
   * @param m  the message.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logMessageProcessing(final Behavior b,
      final MessagePattern p, final Message m)
  {
    if ((this.filter & MESSAGEPROCESSING) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Processing(b.getReference(), b.getClass().getName(), p, m));
    }
  }

  /**
   * Logs the end of the processing of a message.
   *
   * @param b  the behavior.
   * @param p  the message pattern.
   * @param m  the message.
   * @param t  the processing time.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logMessageProcessed(final Behavior b,
      final MessagePattern p, final Message m, final long t)
  {
    if ((this.filter & MESSAGEPROCESSED) > 0)
    {
      this.logger.log(Level.INFO, "", new Processed(
          b.getReference(), b.getClass().getName(), b.log(), p, m, t));
    }
  }

  /**
   * Logs the a message when it does not match any behavior pattern.
   *
   * @param b  the behavior.
   * @param m  the message.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logUnmatchedMessage(final Behavior b, final Message m)
  {
    if ((this.filter & MESSAGENOMATCHED) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Unprocessed(b.getReference(), b.getClass().getName(), m));
    }
  }

  /**
   * Logs the shutdown of an actor.
   *
   * @param b  the behavior.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logActorShutdown(final Behavior b)
  {
    if ((this.filter & OUTPUTMESSAGE) > 0)
    {
      this.logger.log(Level.INFO, "",
          new Destroyed(b.getReference(), b.getClass().getName(), b.log()));
    }
  }

  /**
   * Logs the end time of the execution of the actor space
   * and computes the duration of the execution.
   *
   * Moreover, it logs the number of actors created during
   * the execution of the actor space.
   *
   * @param t  the execution starting time.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logEndExecution(final long t)
  {
    if ((this.filter & EXECUTION) > 0)
    {
      long e = System.nanoTime();

      Executor<?> s = Controller.CONTROLLER.getExecutor();

      if (s instanceof Scheduler<?>)
      {
        this.logger.log(Level.INFO, "", new Execution(Behavior.EXECUTOR,
            t, e, ((Scheduler<?>) s).cycles(), this.actors.get()));
      }
      else
      {
        this.logger.log(Level.INFO, "", new Execution(Behavior.EXECUTOR,
            t, e, 0, this.actors.get()));
      }
    }
  }

  /**
   * Logs the time when a clock event is sent and
   * the number of actors running in the actor space.
   *
   * @param t  the execution time.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logStep(final long t)
  {
    if ((this.filter & STEP) > 0)
    {
      this.logger.log(Level.INFO, "", new Step(
          Behavior.EXECUTOR, t, Controller.CONTROLLER.getRegistry().size()));
    }
  }

  /**
   * Logs information about the loading of data from a file.
   *
   * @param n  the name of the class that loads data.
   * @param f  the input file path.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logDataLoading(final String n, final String f)
  {
    if ((this.filter & DATALOADING) > 0)
    {
      this.logger.log(Level.INFO, "", new DataLoad(n, f));
    }
  }

  /**
   * Logs information about the saving of data into a file.
   *
   * @param n  the name of the class that saves data.
   * @param f  the output file path.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logDataSaving(final String n, final String f)
  {
    if ((this.filter & DATASAVED) > 0)
    {
      this.logger.log(Level.INFO, "", new DataSave(n, f));
    }
  }

  /**
   * Logs the execution of a runtime component.
   *
   * @param f  the runtime filter flag.
   * @param c  the runtime component class qualified name.
   * @param n  the runtime method name.
   * @param o  the runtime method result.
   * @param p  the runtime method parameters.
   *
   * Note that the log is done only if the logging filter enables it.
   *
  **/
  public void logRuntime(final int f, final String c,
      final String n, final Object o, final Object... p)
  {
    if ((this.filter & f) > 0)
    {
      this.logger.log(Level.INFO, "", new Runtime(c, n, p, o));
    }
  }

  /**
   * Builds a new file where store data.
   *
   * @param p  the file path.
   * @param e  the file extension.
   *
   * @return the file.
   *
   * Note that if the file directory is represented by a relative path,
   * then it is considered a sub-directory of the logging repository.
   *
   * This method allows to create different files from the same file path.
   *
   * It is possible because it concatenates the file names with
   * incremental numbers starting from zero.
   *
  **/
  public static File file(final String p, final String e)
  {
    File f;

    String s = Paths.get(p).toString();

    String d = s.substring(0, s.lastIndexOf(File.separator));

    String n;

    if (s.lastIndexOf(".") > s.lastIndexOf(File.separator))
    {
      n = s.substring(s.lastIndexOf(File.separator) + 1, s.lastIndexOf("."));
    }
    else
    {
      n = s.substring(s.lastIndexOf(File.separator) + 1);
    }

    Path i = Paths.get(d);

    if (i.isAbsolute())
    {
      f = new File(i.toString());
    }
    else
    {
      f = new File(Paths.get(REPOSITORY, i.toString()).toString());
    }

    if (!f.exists())
    {
      f.mkdirs();
    }

    String a = Paths.get(f.getPath(), n).normalize().toString();

    f = new File(a + "0." + e);

    int j = 1;

    while (f.exists())
    {
      f = new File(a + j++ + "." + e);
    }

    return f;
  }
}
