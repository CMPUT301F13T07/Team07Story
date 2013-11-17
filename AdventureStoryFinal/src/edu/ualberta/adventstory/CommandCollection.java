/**
 * 
 */
package edu.ualberta.adventstory;

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
	public static class Callback{
		public void callback(){
			// Unknown command.
		}
	}
	
	/**
	 * <code>Command</code> is the abstract classs for the command.
	 * This must be overriden. 
	 * @author Joey Andres
	 *	@version 1.0
	 */
	public static abstract class Command{		
		protected Callback mCallBackExecute;
		
		public Command(Callback cb){
			if( cb == null)
				mCallBackExecute = new Callback();
			else
				mCallBackExecute = cb;
		}
		
		public void execute(){ mCallBackExecute.callback(); }
		public void unexecute(){ }
		public boolean isReversible(){ return false; }		
		public void setCallback( Callback callback ){mCallBackExecute = callback;}
	}
	
	/**
	 * <code>addMultimediaCommand</code> is the command for
	 * handling adding Multimedias. Since this varies alot
	 * between implementation, the user must stOnCommandExecute
	 * to define call back method <code>act()</code>
	 * 
	 * @author JoeyAndres
	 *
	 */
	static public class AddMultimediaCommand extends Command{
		public AddMultimediaCommand(Callback cb){
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
	 * @author JoeyAndres
	 *
	 */
	static public class ExitActivityCommand extends Command{
		Activity mCurrentActivity;
		public ExitActivityCommand(Activity activity, Callback cb){
			super(cb);
			mCurrentActivity = activity;
			
			if( cb == null ){
				this.mCallBackExecute = new Callback(){
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
	 * 
	 * @author JoeyAndres
	 *
	 */
	static public class SaveCommand extends Command{
		public SaveCommand(Callback cb){super(cb);}
				
		@Override
		public void execute() {super.execute();}

		@Override
		public void unexecute() {super.unexecute();}

		@Override
		public boolean isReversible() {return false;}
	}
}
