package it.unipr.sowide.actodes.service.group;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.unipr.sowide.actodes.actor.Behavior;
import it.unipr.sowide.actodes.actor.Message;
import it.unipr.sowide.actodes.actor.MessagePattern;
import it.unipr.sowide.actodes.actor.MessagePattern.MessageField;
import it.unipr.sowide.actodes.controller.Controller;
import it.unipr.sowide.actodes.controller.SpaceInfo;
import it.unipr.sowide.actodes.filtering.MessagePatternField;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Error;
import it.unipr.sowide.actodes.registry.Reference;
import it.unipr.sowide.actodes.service.SimpleService;
import it.unipr.sowide.actodes.service.group.content.Cancel;
import it.unipr.sowide.actodes.service.group.content.Deregister;
import it.unipr.sowide.actodes.service.group.content.Group;
import it.unipr.sowide.actodes.service.group.content.Link;
import it.unipr.sowide.actodes.service.group.content.Register;
import it.unipr.sowide.actodes.service.group.content.Signup;
import it.unipr.sowide.actodes.service.group.content.Unlink;
import it.unipr.sowide.actodes.service.group.content.Register.Type;

/**
 *
 * The {@code Grouper} class defines a group service.
 *
 * It serves the {code Register}, {code Deregister}, {code Signup},
 * {code Cancel}, {code Link} and {code Unlink} requests.
 *
 * Note that the name of a group needs to be different from the name of the
 * provider, the executor actor and the broadcast references.
 *
 * Moreover, its initial character must be a letter.
 *
 * @see it.unipr.sowide.actodes.service.group.content.Register
 * @see it.unipr.sowide.actodes.service.group.content.Deregister
 * @see it.unipr.sowide.actodes.service.group.content.Signup
 * @see it.unipr.sowide.actodes.service.group.content.Cancel
 * @see it.unipr.sowide.actodes.service.group.content.Link
 * @see it.unipr.sowide.actodes.service.group.content.Unlink
 *
**/

public class Grouper extends SimpleService
{
  private static final long serialVersionUID = 1L;

  private final Map<String, Description> groups;

  private final class Description implements Serializable
  {
    private static final long serialVersionUID = 1L;

    protected final MulticastReference reference;
    protected final Reference owner;
    protected final Type type;
    protected final Set<Reference> publishers;
    protected final Set<Reference> subscribers;
    protected final Set<MulticastReference> multicasts;

    /**
     * Class constructor.
     *
     * @param n  the group name.
     * @param o  the owner reference.
     * @param t  the group type.
     *
    **/
    Description(final String n, final Reference o, final Type t)
    {
      this.owner = o;
      this.type  = t;

      this.publishers  = new HashSet<>();
      this.subscribers = new HashSet<>();
      this.multicasts  = new HashSet<>();

      this.reference = new MulticastReference(
          Reference.address(n, Behavior.PROVIDER.getLocation()),
          this.publishers, this.subscribers, this.multicasts);
    }
  }

  /**
   * Class constructor.
   *
  **/
  public Grouper()
  {
    super(new MessagePattern(new MessagePatternField(
        MessageField.CONTENT,  new IsInstance(Group.class))),
        new MessagePattern(new MessagePatternField(
            MessageField.RECEIVERS,  new IsInstance(Group.class)))
        );

    this.groups = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} **/
  @Override
  public Behavior process(final Message m)
  {
    if (m.getContent() instanceof Signup)
    {
      signup(m);
    }
    else if (m.getContent() instanceof Cancel)
    {
      cancel(m);
    }
    else if (m.getContent() instanceof Register)
    {
      register(m);
    }
    else if (m.getContent() instanceof Deregister)
    {
      deregister(m);
    }
    else if (m.getContent() instanceof Link)
    {
      link(m);
    }
    else if (m.getContent() instanceof Unlink)
    {
      unlink(m);
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.UNKNOWNCONTENT);
    }

    return null;
  }

  private void signup(final Message m)
  {
    Signup i = (Signup) m.getContent();

    Description d = this.groups.get(i.getName());

    if ((d != null)
        && (((d.type == Type.OPEN) && (i.getReferences() == null))
            || (m.getSender().equals(d.owner))))
    {
      int size = d.subscribers.size();

      if (i.getReferences() == null)
      {
        add(i.getRole(), m.getSender(), d);
      }
      else
      {
        for (Reference r : i.getReferences())
        {
          add(i.getRole(), r, d);
        }
      }

      Controller.CONTROLLER.getProvider().send(m, d.reference);

      if (((i.getRole() == Role.SUBSCRIBER)
           || (i.getRole() == Role.EXCHANGER))
          && (size == 0) && (d.subscribers.size() > 0))
      {
        Link link = new Link(i.getName(), d.owner, d.type, d.reference);

        SpaceInfo.INFO.getProviders().forEach(
            r -> Controller.CONTROLLER.getProvider().send(r, link));
      }
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.REFUSEDREQUEST);
    }
  }

  private void add(final Role r, final Reference u, final Description d)
  {
    switch (r)
    {
      case SUBSCRIBER:
        d.subscribers.add(u);
        break;
      case PUBLISHER:
        d.publishers.add(u);
        break;
      case EXCHANGER:
        d.subscribers.add(u);
        d.publishers.add(u);
        break;
      default:
        break;
    }
  }

  private void cancel(final Message m)
  {
    Cancel i = (Cancel) m.getContent();

    Description d = this.groups.get(i.getName());

    if ((d != null)
        && ((i.getReferences() == null)
            || (m.getSender().equals(d.owner))))
    {
      if (i.getReferences() == null)
      {
        remove(i.getRole(), m.getSender(), d);
      }
      else
      {
        for (Reference r : i.getReferences())
        {
          remove(i.getRole(), r, d);
        }
      }

      if (d.subscribers.size() == 0)
      {
        Unlink delete = new Unlink(i.getName(), d.reference);

        SpaceInfo.INFO.getProviders().forEach(
            r -> Controller.CONTROLLER.getProvider().send(r, delete));
      }

      Controller.CONTROLLER.getProvider().send(m, Done.DONE);
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.REFUSEDREQUEST);
    }
  }

  private void remove(final Role r, final Reference u, final Description d)
  {
    switch (r)
    {
      case SUBSCRIBER:
        d.subscribers.remove(u);
        break;
      case PUBLISHER:
        d.publishers.remove(u);
        break;
      case EXCHANGER:
        d.subscribers.remove(u);
        d.publishers.remove(u);
        break;
      default:
        break;
    }
  }

  private void register(final Message m)
  {
    Register i = (Register) m.getContent();

    Description d = this.groups.get(i.getName());

    if ((d == null) && (isGroupName(i.getName())))
    {
      d = new Description(i.getName(), m.getSender(), i.getType());

      this.groups.put(i.getName(), d);

      Controller.CONTROLLER.getProvider().send(m, d.reference);

      SpaceInfo.INFO.getProviders().forEach(
          r -> Controller.CONTROLLER.getProvider().send(r, i));
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.REFUSEDREQUEST);
    }
  }

  private void deregister(final Message m)
  {
    Deregister i = (Deregister) m.getContent();

    Description d = this.groups.get(i.getName());

    if ((d != null) &&  m.getSender().equals(d.owner))
    {
      d.subscribers.clear();
      d.publishers.clear();
      d.multicasts.clear();

      this.groups.remove(i.getName());

      Unlink delete = new Unlink(i.getName(), d.reference);

      SpaceInfo.INFO.getProviders().forEach(
          r -> Controller.CONTROLLER.getProvider().send(r, delete));
    }
  }

  private void link(final Message m)
  {
    Link i = (Link) m.getContent();

    Description d = this.groups.get(i.getName().toString());

    if (d != null)
    {
      d.multicasts.add(i.getReference());
    }
    else if (isGroupName(i.getName()))
    {
      d = new Description(i.getName(),
          Controller.CONTROLLER.getProvider().getReference(),
          Type.CLOSED);

      d.multicasts.add(i.getReference());

      this.groups.put(i.getName(), d);
    }
  }

  private void unlink(final Message m)
  {
    Unlink i = (Unlink) m.getContent();

    Description d = this.groups.get(i.getName().toString());

    if (d != null)
    {
      d.multicasts.remove(i.getReference());
    }
    else
    {
      Controller.CONTROLLER.getProvider().send(m, Error.REFUSEDREQUEST);
    }
  }

  private boolean isGroupName(final String n)
  {
    return (!(!Character.isLetter(n.charAt(0))
        || n.equals(Behavior.PROVIDER.getName())
        || n.equals(Behavior.EXECUTOR.getName())
        || n.equals(Behavior.SPACE.getName())
        || n.equals(Behavior.APP.getName())));
  }
}
