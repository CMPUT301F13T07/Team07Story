package edu.ualberta.controller;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
import edu.ualberta.controller.CallbackIntefaces.*;
import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;

/**
 * <code>OnAddMultimediaCommand</code> is the command for handling adding
 * Multimedias. Since this varies alot between implementation, the user must
 * setOnCommandExecute to define call back method.
 * 
 */
public class OnAddMultimediaListener 
					extends CommandAbstract<OnAddMultimedia, OnUndo> {
	Page mPage;		
	MultimediaAbstract mMultimedia;
	ActivityExtended mActivityExtended;
	/**		
	 * @param onAddMultimedia is the callback. Set to null
	 * to use default handling of AddMultimedia event.
	 */
	public OnAddMultimediaListener(OnAddMultimedia onAddMultimedia, OnUndo undo) {
		super(onAddMultimedia, undo);
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
	public OnAddMultimediaListener(Page page, MultimediaAbstract multimedia, ActivityExtended ae) {
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
	public void execute() {		
		if(mCallbackExecute == null){			
			// Check if already in current page.
			MultimediaAbstract temp = PageAdapter.getMultimediaByID(mPage, mMultimedia.getID());
			if(temp != null)
				return;
			
			DbManager db = mActivityExtended.getDatabase();
			db.insert_multimedia(mMultimedia, mPage.getID());
			
			mPage.getMultimedia().add(mMultimedia);
			mActivityExtended.localUpdate();
		}else{
			mCallbackExecute.onAddMultimedia();
		}
	}		

	@Override
	public void unexecute() {
		if(mCallbackUnExecute == null){
			DbManager db = mActivityExtended.getDatabase();
			db.delete_mult(mMultimedia, mPage);
		
			// Delete multimedia in current page.
			PageAdapter.deleteMultimediaByID(mPage, mMultimedia.getID());
			
			mActivityExtended.localUpdate();
		}else{
			mCallbackExecute.onAddMultimedia();
		}
	}

	@Override
	public boolean isReversible() {
		return true;
	}
	
	public void setOnAddMultimediaListener(OnAddMultimedia callback) {
		mCallbackExecute = callback;
	}
	
	public void setOnUndoAddMultimediaListener(OnUndo callback){
		mCallbackUnExecute = callback;
	}

	@Override
	public void setExecuteCallback(OnAddMultimedia callback) {
		setOnAddMultimediaListener(callback);
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		setOnUndoAddMultimediaListener(callback);
	}
}
