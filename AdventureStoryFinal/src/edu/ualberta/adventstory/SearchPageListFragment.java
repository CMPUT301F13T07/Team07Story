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
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import edu.ualberta.data.DbManager;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SearchPageListFragment extends ListFragment {

	private ArrayList<Page> pageList;
	private String title;
	private String text;
	private Page page;
	private DbManager database;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_searchstorypagelist,
				container, false);
		Button downloadall = (Button) view.findViewById(R.id.downloadall);

		if (!getArguments().getBoolean("isWeb")) {
			downloadall.setClickable(false);
			downloadall.setBackgroundColor(Color.parseColor("#808080"));
		}
		downloadall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Log.w("debug", "Button clicked");

				Story story = ((DataSingleton) getActivity()
						.getApplicationContext()).getCurrentStory();
				database = ((DataSingleton) getActivity()
						.getApplicationContext()).database;
				saveEntireStory(story.getRoot());
				story.setID((int) database.insert_story(story));
			}

		});
		return view;
	}

	/**
	 * Recursively runs through the tree and dumps it into the database
	 * 
	 * @param page
	 */
	private void saveEntireStory(Page page) {
		ArrayList<Page> pages = page.getPages();
		page.setID((int) database.insert_page(page));
		database.update_page(page);
		for (int i = 0; i < pages.size(); i++) {
			database.insert_page_option(page, pages.get(i));
			saveEntireStory(pages.get(i));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onStart() {
		super.onStart();
		pageList = (ArrayList<Page>) getArguments().getSerializable("pageList");
		ListAdapter adapter = new ArrayAdapter<Page>(getActivity(),
				R.layout.rowpagelayout, R.id.label, pageList);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		previewPage(position);
		page = (Page) pageList.get(position);
		((DataSingleton) getActivity().getApplicationContext())
				.setCurrentPage(page);
	}

	/**
	 * This method creates a new preview fragment on the selected page
	 * 
	 * @param position
	 */
	private void previewPage(int position) {
		text = pageList.get(position).getText();
		title = pageList.get(position).getTitle();

		Bundle bundle = new Bundle();

		// bundle.putSerializable("pageList", pageList);
		bundle.putString("title", title);
		bundle.putString("text", text);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		SearchPreviewFragment preview = new SearchPreviewFragment();

		// Setting Arguments being passed
		preview.setArguments(bundle);
		fragmentTransaction.replace(android.R.id.content, preview);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

}
