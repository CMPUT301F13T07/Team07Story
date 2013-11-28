package edu.ualberta.controller;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
import edu.ualberta.controller.CallbackIntefaces.*;
import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;

/**
 * <code>OnDeleteMultimediaListener</code> is the command for handling deleting
 * Multimedias. Since this varies alot between implementation, the user must
 * setOnCommandExecute to define call back method.
 * 
 */
public class OnDeleteMultimediaListener 
					extends CommandAbstract<OnDelete, OnUndo> {
	Page mPage;		
	MultimediaAbstract mMultimedia;
	ActivityExtended mActivityExtended;
	/**		
	 * @param onAddMultimedia is the callback. Set to null
	 * to use default handling of AddMultimedia event.
	 */
	public OnDeleteMultimediaListener(OnDelete onDelete, OnUndo undo) {
		super(onDelete, undo);
		mPage = null;
		mMultimedia = null;
		mActivityExtended = null;
	}
	
	/**
	 * Constructor that overrides the callback methods.
	 * 
	 * @param page is the page currently on.
	 * @param multimedia multimedia being added.
	 * @param ae 
	 */
	public OnDeleteMultimediaListener(Page page, MultimediaAbstract multimedia, ActivityExtended ae) {
		super(null, null);			
		mPage = page;
		mMultimedia = multimedia;
		mActivityExtended = ae; 
	}

	public void setMultimedia(MultimediaAbstract multimedia){
		mMultimedia = multimedia;
	}
	
	public void setPage(Page page){
		mPage = page;
	}
	
	public MultimediaAbstract getMultimedia(){
		return mMultimedia;
	}		
	
	public Page getPage(){
		return mPage;
	}
	
	@Override
	public void unexecute() {		
		if(mCallbackExecute == null){			
			// Check if already in current page.
			for(MultimediaAbstract ma : mPage.getMultimedia()){
				if(mMultimedia.getID() == ma.getID()){
					return;
				}
			}
			
			DbManager db = mActivityExtended.getDatabase();
			db.insert_multimedia(mMultimedia, mPage.getID());
			
			mPage.getMultimedia().add(mMultimedia);
			mActivityExtended.localUpdate();
		}else{
			mCallbackExecute.delete();
		}
	}		

	@Override
	public void execute() {
		if(mCallbackUnExecute == null){
			DbManager db = mActivityExtended.getDatabase();
			db.delete_mult(mMultimedia, mPage);
			// Delete multimedia in current page.
			for(MultimediaAbstract ma : mPage.getMultimedia()){
				if(mMultimedia.getID() == ma.getID()){
					mPage.getMultimedia().remove(ma);
					break;
				}
			}
			
			mActivityExtended.localUpdate();
		}else{
			mCallbackExecute.delete();
		}
	}

	@Override
	public boolean isReversible() {
		return true;
	}
	
	public void setOnDeleteMultimediaListener(OnDelete callback) {
		mCallbackExecute = callback;
	}
	
	public void setOnUndoDeleteMultimediaListener(OnUndo callback){
		mCallbackUnExecute = callback;
	}

	@Override
	public void setExecuteCallback(OnDelete callback) {
		setOnDeleteMultimediaListener(callback);
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		setOnUndoDeleteMultimediaListener(callback);
	}
}
