package edu.ualberta.multimedia;

import java.util.ArrayList;

import edu.ualberta.adventstory.TObserver;

/**
 * <code>Observable</code> is an interface that the model 
 * implement.
 * 
 * @author JoeyAndres
 *
 * @param <TView> is the View associated with the model implementing
 * interface.
 */
@SuppressWarnings("rawtypes")
abstract public class TObservable<V extends TObserver> {
	ArrayList<V> mViews;
	boolean mHasChanged = false;	// Set to true when changed.
	
	// All models need to keep track of their views.
	public TObservable(){ mViews = new ArrayList<V>(); }
	public void addObserver(V view){ mViews.add(view); }
	public void deleteObserver(V view){ mViews.remove(view); }
	
	// All models modify their views to update.
	public void notifyObservers(){
		/*for(V v : mViews)
			v.update(this);*/
	}
	
	// note wether the model has changed.
	public boolean hasChanged(){ return mHasChanged; }
	protected void clearChanged(){ mHasChanged = false; }
	protected void setChanged(){ mHasChanged = true; }
}
