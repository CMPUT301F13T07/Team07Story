package edu.ualberta.controller;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
import edu.ualberta.controller.CommandCollection.CommandAbstract;
import edu.ualberta.controller.CommandCollection.OnAddMultimedia;
import edu.ualberta.controller.CommandCollection.OnMove;
import edu.ualberta.controller.CommandCollection.OnUndo;
import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;

/**
 * <code>ONMoveMultimediaListener</code> is the command for handling moving
 * Multimedias. Since this varies alot between implementation, the user must
 * stOnCommandExecute to define call back method <code>act()</code>
 * 
 */
public class OnMoveMultimediaListener
					extends CommandAbstract<OnMove, OnUndo> {
	int mCurrentIndex;		// Current Index of the multimedia.
	int mNewIndex;			// New Index of the multimedia.
	Page mPage;
	MultimediaAbstract mMultimedia;
	ActivityExtended mActivityExtended;
	/**		
	 * @param onMoveMultimedia is the callback. Set to null
	 * to use default handling of MoveMultimedia event.
	 */
	public OnMoveMultimediaListener(int newIndex, OnMove onMove, OnUndo undo) {
		super(onMove, undo);
		mNewIndex = newIndex;
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
	public OnMoveMultimediaListener(Page page, MultimediaAbstract multimedia, int newIndex, ActivityExtended ae) {
		super(null, null);			
		mPage = page;
		mMultimedia = multimedia;
		mActivityExtended = ae; 
		mCurrentIndex = multimedia.getIndex();
		mNewIndex = newIndex;
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
	public boolean isReversible() {
		return true;
	}
	
	public void setOnMoveMultimediaListener(OnMove callback) {
		mCallbackExecute = callback;
	}
	
	public void setOnUndoMoveMultimediaListener(OnUndo callback){
		mCallbackUnExecute = callback;
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		setOnUndoMoveMultimediaListener(callback);
	}

	@Override
	public void setExecuteCallback(OnMove callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// incase deleted.
		if(mMultimedia != null){
			mMultimedia.setIndex(mNewIndex);
			this.mActivityExtended.localUpdate();
		}
	}

	@Override
	public void unexecute() {
		// incase deleted.
		if(mMultimedia != null){
			mMultimedia.setIndex(mCurrentIndex);
			this.mActivityExtended.localUpdate();
		}
	}
}
