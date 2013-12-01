package edu.ualberta.controller;

import java.util.ArrayList;

import edu.ualberta.adventstory.TObserver;
import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;

/**
 * <code>PageAdapter</code> utilizes the Adapter Pattern as discussed in class.
 * @author Joey Andres
 *
 */
public class PageAdapter{

	static public boolean isMultimediaEmpty(Page page) {
		return page.getMultimedia().isEmpty();		
	}

	/**
	 * @param page is where we search for the Multimedia
	 * @param multimediaID
	 * @return Multimedia in <code>page</code> that matches multimediaID
	 */
	static public MultimediaAbstract getMultimediaByID(Page page, int multimediaID) {
		return getMultimediaByID(page.getMultimedia(), multimediaID);
	}
	
	/**
	 * @param maList is the Multimedia list
	 * @param multimediaID
	 * @return Multimedia that matches id.
	 */
	static public MultimediaAbstract getMultimediaByID(ArrayList<MultimediaAbstract> maList, int multimediaID){
		for(MultimediaAbstract ma:maList){
			if(ma.getID() == multimediaID){
				return ma;
			}
		}
		return null;
	}
		
	/**
	 * @param maList ArrayList of Multimedia.
	 * @param multimediID id of the Multimedia being deleted.
	 * @return true if operation is success. false if Multimedia don't exist in <code>maList</code>.
	 */
	static public boolean deleteMultimediaByID(ArrayList<MultimediaAbstract> maList, int multimediID){
		MultimediaAbstract temp = getMultimediaByID(maList, multimediID);
		if(temp == null)
			return false;
		
		maList.remove(temp);
		return true;
	}
	
	static public boolean deleteMultimediaByID(Page page, int multimediID){
		return deleteMultimediaByID(page.getMultimedia(), multimediID);
	}
	
	/**
	 * Get the currently selected multimedia.
	 * @param page
	 * @return the first multimedia with selected attribute set.
	 */
	static public MultimediaAbstract getSelectedMultimedia(Page page){
		for (MultimediaAbstract ma: page.getMultimedia()){
			if(ma.getIsSelected()){
				return ma;
			}
		}	
		return null;
	}
	
	/**
	 * Clears the selection of given page.
	 * @param page is the Page where mIsSelected attribute is cleared.
	 */
	static public void clearMultimediaSelection(Page page){
		for( MultimediaAbstract ma: page.getMultimedia()){
			ma.setIsSelected(false);
		}
	}
	
	/**
	 * Since MultimediaAbstract is a model, a View can be attached to it. This
	 * allows instant updating when an attribute changed.
	 */
	static public void attachView(Page page, TObserver view){
		for( MultimediaAbstract ma: page.getMultimedia()){
			ma.addObserver(view);
		}
	}
}
