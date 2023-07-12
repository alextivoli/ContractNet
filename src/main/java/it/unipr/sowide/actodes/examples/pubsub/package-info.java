/**
 *
 * Shows how to use the topic-based publish and subscribe service by creating
 * a set of actors acting as temperature sensors and another set of actors
 * processing the information coming from the sensors.
 *
 * Its execution can involve one or more actors spaces.
 *
 * Both standalone and distributed execution are started by the {@code main}
 * method contained in the {@code Initiator} class.
 *
 * This class allows to a user to select the type of execution,
 * the number of processors and sensors, the lifetime of the application
 * and the number of nodes of the distributed application.
 *
 * When the execution is distributed on a set of actors spaces, then
 * the first must be an broker actor space, then there can be zero or more
 * node actor space, and finally the last one must be the
 * initiator actor space.
 *
**/
package it.unipr.sowide.actodes.examples.pubsub;

