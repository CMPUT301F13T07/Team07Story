/** 
 * @author Joey Andres
 */
package edu.ualberta.extendedViews;

import android.content.Context;
import android.widget.EditText;

/**
 * The primary purpose of <code>EditTextEx</code> is due to EditText
 * not allowing to modify onSelectionChanged event handler method. To
 * go around that, an attribute <code>OnSelectionChangedListener</code> is added. 
 * It contains the callback function that is called when the selection change event
 * is called.
 *
 * <p>
 * To set a call back function, do the following:
 * <pre>
 * EditTextEx ete = new EditTextEx();
 * ete.setOnSelectionChangedListener(new OnSelectionChangedListener(){
 * 		onSelectionChangedListener(int selStart, int selEnd){
 * 			// Insert callback code here.
 * 		}
 * });
 * </pre>
 * 
 * @author Joey Andres
 * @version 1.0
 */

public class EditTextEx extends EditText {
	public static class OnSelectionChangedListener{
		public OnSelectionChangedListener(){}
		public void onSelectionChangedListener(int selStart, int selEnd){}
	}
	
	private  OnSelectionChangedListener mOnSelectionChangedListener;
	
	public EditTextEx(Context context) {
		super(context);
		init();
	}
	
	/**
	 * <code>onSelectionChanged</code> is called during the super(context)
	 * in the constructor, when <code>mOnSelectionChangedListener</code> is
	 * still null, thus a null check is implemented.
	 * 
	 * (non-Javadoc)
	 * @see android.widget.TextView#onSelectionChanged(int, int)
	 */
	@Override
	protected void onSelectionChanged(int selStart, int selEnd){		
		super.onSelectionChanged(selStart, selEnd);
		if( mOnSelectionChangedListener != null ){
			mOnSelectionChangedListener.onSelectionChangedListener(selStart, selEnd);
		}
	}
	
	public void setOnSelectionChangedListener(OnSelectionChangedListener action){
		mOnSelectionChangedListener = action;
	}
	
	private void init(){
		mOnSelectionChangedListener = new OnSelectionChangedListener();
	}
}