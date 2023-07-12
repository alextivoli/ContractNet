package it.unipr.sowide.actodes.executor;

import it.unipr.sowide.actodes.filtering.constraint.UnaryConstraint;

/**
 *
 * The {@code Termination} interface defines a termination condition
 * of the execution of an executor actor.
 *
 * This interface has no methods and serves only to identify
 * a termination condition.
 *
**/

public interface Termination extends UnaryConstraint<Executor<?>>
{
}
