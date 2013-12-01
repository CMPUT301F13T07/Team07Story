/**
 * Purpose: This fragment starts when the user selects a page to show a preview
 * of the page consisting of the page title, and page text.
 * 
 * Outstanding Issues: N/A
 * 
 * Author: Henry Hoang
 */
package edu.ualberta.adventstory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SearchPreviewFragment extends Fragment {
	protected DataSingleton mDataSingleton;
	
	private TextView pageTitle;
	private TextView pageText;

	private String title;
	private String text;
	private String previewText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		return inflater.inflate(
					R.layout.fragment_searchpreview, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		// Initialization
		pageTitle = (TextView) getActivity().findViewById(R.id.searchpreviewtitle);
		pageText = (TextView) getActivity().findViewById(R.id.pagepreview);
		
		Button mReturn = (Button) this.getActivity().findViewById(R.id.buttonreturn);
		Button mSelect = (Button) this.getActivity().findViewById(R.id.buttonselect);
		
		title = getArguments().getString("title");
		text = getArguments().getString("text");
		
		// Gets 200 words max for preview text
		if (text.length() > 200) {
			previewText = text.substring(0, 200);
		} else {
			previewText = text;
		}
		// Set the page texts, not quite sure how to pull from database by id yet.
		pageTitle.setText(title);
		pageText.setText(previewText);
		
		// Sets the button usage
    	mReturn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// End's fragment and goes back to activity
    			getActivity().getFragmentManager().popBackStack();	
    		}
    	});
	
    	mSelect.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Selects the selected page and goes to PageView Activity
    			// Need to add that it passes through the id
    			displayPage();
    		}
    	});	
	}
	
	/**
	 * This method changes to the PageView activity
	 */
	private void displayPage() {
		Intent i = new Intent(getActivity(), PageViewActivity.class);
		startActivityForResult(i, 0);
	}
}
