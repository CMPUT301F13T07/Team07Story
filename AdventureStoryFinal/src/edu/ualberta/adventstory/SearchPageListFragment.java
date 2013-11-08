package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
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
	ArrayList<Page> pageList;
	
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
		pageList = (ArrayList<Page>) getArguments().getSerializable("pageList");
		ListAdapter myListAdapter = new ArrayAdapter<Page>(getActivity(), R.layout.fragment_searchstorypagelist, R.id.fragmentpagetext , pageList);
		setListAdapter(myListAdapter);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		previewPage(position);
	}
	
	private void previewPage(int position) {
		String text = pageList.get(position).getText();
		String title = pageList.get(position).getTitle();
		Bundle bundle = new Bundle();
		bundle.putSerializable("pageList", pageList);
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
