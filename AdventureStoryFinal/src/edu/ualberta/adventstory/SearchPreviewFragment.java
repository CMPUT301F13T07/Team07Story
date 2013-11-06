package edu.ualberta.adventstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchPreviewFragment extends Fragment {
	
	private TextView pageTitle;
	private TextView pageText;
	
	
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
	    			// Selects the selected page and goes to PageView Activity
	    			// Need to add that it passes through the id
	    			displayPage();
	    		}
	    	});
	    	
	    	mReturn.setOnClickListener(new OnClickListener(){
	    		public void onClick(View v){
	    			// End's fragment and goes back to activity
	    			getActivity().getSupportFragmentManager().popBackStack();
	    		}
	    	});
	    	
			// Inflate the layout for this fragment
			return mLinearLayout;
		
	}
	
	
	
	private void displayPage() {
		Intent i = new Intent(getActivity(), PageViewActivity.class);
		startActivityForResult(i, 0);
	}
	
	

}
