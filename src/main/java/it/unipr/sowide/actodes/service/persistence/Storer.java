package it.unipr.sowide.actodes.service.persistence;

import java.util.Set;

import it.unipr.sowide.actodes.actor.Actor;
import it.unipr.sowide.actodes.configuration.Configuration;
import it.unipr.sowide.actodes.service.Service;

/**
 *
 * The {@code Storer} interface defines a persistent storage service
 * for actors and actor-based applications.
 *
**/

public interface Storer extends Service
{
  /**
   * Starts the persistent storage manager.
   *
  **/
  void start();

  /**
   * Shutdowns the persistent storage manager.
   *
  **/
  void shutdown();

  /**
   * Creates a checkpoint of an actor space and stores it
   * in the persistent storage.
   *
   * @param c  the configuration object defining the actor space checkpoint.
   * @param i  the actor space persistent storage identifier.
   *
   * @return <code>true</code> if the operation is successful.
   *
  **/
  boolean save(Configuration c, String i);

  /**
   * Retrieves a checkpoint of an actor space from the persistent storage.
   *
   * @param i  the actor space persistent storage identifier.
   *
   * @return the configuration object defining the actor space checkpoint.
   *
   * Note that the return value is <code>null</code> if the operation failed.
   *
  **/
  Configuration retrieve(String i);

  /**
   * Removes an actor space from the persistent storage.
   *
   * @param i  the actor space persistent storage identifier.
   *
  **/
  void remove(String i);

  /**
   * Loads an actor from the persistent storage.
   *
   * @param i  the actor persistent storage identifier.
   *
   * @return  the actor.
   *
   * Note that the return value is <code>null</code> if the operation failed.
   *
  **/
  Actor load(String i);

  /**
   * Stores an actor in the persistent storage. If the persistent storage
   * does not exist, then it creates it.
   *
   * @param a  the actor.
   * @param i  the actor persistent storage identifier.
   *
   * @return <code>true</code> if the operation is successful.
   *
  **/
  boolean store(Actor a, String i);

  /**
   * Removes an actor from the persistent storage.
   *
   * @param i  the actor persistent storage identifier.
   *
  **/
  void delete(String i);

  /**
   * Gets the persistent storage identifier.
   *
   * @return the persistent storage identifier.
   *
   * Note that the return value is <code>null</code>
   * if the persistent storage does not exist.
   *
  **/
  String getId();

  /**
   * Lists the addresses (names) of the actors in the persistent storage.
   *
   * @return the list.
   *
  **/
  Set<String> list();

  /**
   * Gets the number of the actors in the persistent storage.
   *
   * @return the number.
   *
  **/
  int size();
}
