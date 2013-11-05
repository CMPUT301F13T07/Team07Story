package edu.ualberta.adventstory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SearchPreviewFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
	
			LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(
					R.layout.fragment_searchpreview, container, false);
		
			Button mSelect = (Button) mLinearLayout.findViewById(R.id.buttonselect);
			Button mReturn = (Button) mLinearLayout.findViewById(R.id.buttonreturn);
			
			
			// Sets the button usage
	    	mSelect.setOnClickListener(new OnClickListener(){
	    		public void onClick(View v){
	    			// Selects the selected page
	    			
	    		}
	    	});
	    	
	    	mReturn.setOnClickListener(new OnClickListener(){
	    		public void onClick(View v){
	    			// Goes back to activity
	    			
	    		}
	    	});
	    	
			// Inflate the layout for this fragment
			return mLinearLayout;
		
	}
	
	

}
