package edu.ualberta.adventstory;

/**
 * <code>TObserver</code> is an interface for views
 * as part of the MVC pattern.
 * 
 * @author Joey
 *
 * @param <M> View
 */
public interface TObserver<M> {
	public void update(M model);
}
