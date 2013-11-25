package edu.ualberta.controller;

import java.util.ArrayList;

import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
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

	static public MultimediaAbstract getMultimediaByID(Page page, int multimediaID) {
		return getMultimediaByID(page.getMultimedia(), multimediaID);
	}
	
	static public MultimediaAbstract getMultimediaByID(ArrayList<MultimediaAbstract> maList, int multimediaID){
		for(MultimediaAbstract ma:maList){
			if(ma.getID() == multimediaID){
				return ma;
			}
		}
		return null;
	}
	
	/**
	 * 
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
}
