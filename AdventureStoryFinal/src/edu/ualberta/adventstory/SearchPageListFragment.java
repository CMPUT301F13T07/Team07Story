package edu.ualberta.adventstory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SearchPageListFragment extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
			// Inflate the layout for this fragment
			return inflater.inflate(
					R.layout.fragment_searchstorypagelist, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	


}
