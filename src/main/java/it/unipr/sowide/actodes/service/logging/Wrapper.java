package it.unipr.sowide.actodes.service.logging;

/**
 *
 * The {@code Wrapper} interface defines a wrapper
 * for logging the activities of actor components.
 *
 * @param <T>  the actor component type.
 *
**/

public interface Wrapper<T>
{
  /**
   * Gets the wrapped actor component.
   *
   * @return the component.
   *
  **/
  T getWrapped();
}
