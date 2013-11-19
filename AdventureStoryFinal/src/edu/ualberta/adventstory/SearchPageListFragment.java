/**
 * Purpose: This fragment starts when the user selects a story from the list and 
 * displays a list of the pages for the respective story selected. 
 * 
 * Outstanding Issues: N/A
 * 
 * Author: Henry Hoang
 */
package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import edu.ualberta.utils.Page;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SearchPageListFragment extends ListFragment {
	
	private ArrayList<Page> pageList;
	private String title;
	private String text;
	private Page page;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(
					R.layout.fragment_searchstorypagelist, container, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onStart(){
		super.onStart();
		pageList = (ArrayList<Page>) getArguments().getSerializable("pageList");
		ListAdapter myListAdapter = new ArrayAdapter<Page>(getActivity(), 
				R.layout.fragment_searchstorypagelist, R.id.fragmentpagetext , pageList);
		setListAdapter(myListAdapter);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		previewPage(position);
		page = (Page) pageList.get(position);
		((DataSingleton)getActivity().getApplicationContext()).setCurrentPage(page);
	}
	/**
	 * This method creates a new preview fragment on the selected page
	 * @param position
	 */
	private void previewPage(int position) {
		text = pageList.get(position).getText();
		title = pageList.get(position).getTitle();
		
		Bundle bundle = new Bundle();
		
		//bundle.putSerializable("pageList", pageList);
		bundle.putString("title", title);
		bundle.putString("text", text);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		SearchPreviewFragment preview = new SearchPreviewFragment();
		
		//Setting Arguments being passed
		preview.setArguments(bundle);
		fragmentTransaction.replace(android.R.id.content, preview);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	

	  

	


}
