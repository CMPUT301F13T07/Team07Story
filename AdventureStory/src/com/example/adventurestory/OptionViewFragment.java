package com.example.adventurestory;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ListView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class OptionViewFragment extends ListFragment{
	
	public ListView activity_listview;

	public OptionViewFragment(ListView list_items) {
	    activity_listview = list_items;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

	}

}
