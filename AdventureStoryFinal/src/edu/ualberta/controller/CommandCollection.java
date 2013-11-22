/**
 * 
 */
package edu.ualberta.controller;

import android.app.Activity;
import android.content.Context;

/**
 *	<code>CommandCollection</code> is a container for Command's subclass.
 *	These are all implementation of Command Pattern
 *	as discussed in Patterns-01.pdf in class.
 *	
 *
 * @author JoeyAndres
 * @version 1.0
 */
public class CommandCollection {
	/**
	 * <code>Callback</code> is the class containing the callback function.
	 * This callback function can be set via <code>setONCommandexecute<code>
	 * method in <code>Command</code> class.
	 * 
	 * @author Joey Andres
	 *
	 */
	public static interface OnCallbackListener{
		public void callback();		
	}
	
	/**
	 * <code>Command</code> is the abstract classs for the command.
	 * This must be overriden. 
	 *	@version 1.0
	 */
	public static class OnCommand{		
		protected OnCallbackListener mCallBackExecute;
		
		public OnCommand(OnCallbackListener cb){
			mCallBackExecute = cb;
		}
		
		public void execute(){ mCallBackExecute.callback(); }
		public void unexecute(){ }
		public boolean isReversible(){ return false; }		
		public void setCallback( OnCallbackListener callback ){mCallBackExecute = callback;}
	}
	
	/**
	 * <code>addMultimediaCommand</code> is the command for
	 * handling adding Multimedias. Since this varies alot
	 * between implementation, the user must stOnCommandExecute
	 * to define call back method <code>act()</code>
	 *
	 */
	static public class OnAddMultimedia extends OnCommand{
		public OnAddMultimedia(OnCallbackListener cb){
			super(cb);
		}
				
		@Override
		public void execute() { super.execute();}

		@Override
		public void unexecute() { super.unexecute();}

		@Override
		public boolean isReversible() { return false; }
	}
	
	/**
	 * <code>exitActivityCommand</code> contains the call back for
	 * exiting current activity.
	 *
	 */
	static public class OnExitActivity extends OnCommand{
		Activity mCurrentActivity;
		public OnExitActivity(Activity activity, OnCallbackListener cb){
			super(cb);
			mCurrentActivity = activity;
			
			if( cb == null ){
				this.mCallBackExecute = new OnCallbackListener(){
					@Override
					public void callback(){
						mCurrentActivity.finish();
					}
				};
			}
		}
		
		@Override
		public void execute() {super.execute();}

		@Override
		public void unexecute() {super.unexecute();}

		@Override
		public boolean isReversible() {return super.isReversible();}		
	}
	
	/**
	 * Command for saving.
	 */
	static public class OnSave extends OnCommand{
		public OnSave(OnCallbackListener cb){super(cb);}
				
		@Override
		public void execute() {super.execute();}

		@Override
		public void unexecute() {super.unexecute();}

		@Override
		public boolean isReversible() {return false;}
	}
}
