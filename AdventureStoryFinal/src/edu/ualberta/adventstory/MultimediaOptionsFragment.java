package edu.ualberta.adventstory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;

@SuppressLint("ValidFragment")
public class MultimediaOptionsFragment extends Fragment implements 
												TObserver<TObservable> {
	// A simple static factory problem.
	static public MultimediaOptionsFragment MultimediaOptionsFragmentFactory(
																Page currentPage){
		return new MultimediaOptionsFragment(currentPage);
	}
		
	protected Page mCurrentPage;							// Current page.
	
	private TextView mMultimediaTypeTextView;				
	private TextView mMultimediaFilePath;					
	private Button mDeleteMultimediaButton;
	private Button mChangeMultimediaButton;
	
	private MultimediaOptionsFragment(Page currentPage){	
		mCurrentPage = currentPage;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		return inflater.inflate(
					R.layout.fragment_pageedit_multimedia_options, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		mMultimediaTypeTextView = (TextView)getActivity().findViewById(R.id.multimediaTypeTextView);
		mMultimediaFilePath = (TextView)getActivity().findViewById(R.id.multimediaFileNameTextView);
		mDeleteMultimediaButton = (Button)getActivity().findViewById(R.id.deleteMultimediaButton);
		mChangeMultimediaButton = (Button)getActivity().findViewById(R.id.changeMultimediaButton);
		
		mDeleteMultimediaButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mCurrentPage.getMultimedia() == null){
					// UNKNOWN STATE, EXIT AT ONCE.
					closeFragment();
				}
				for(MultimediaAbstract m : mCurrentPage.getMultimedia()){
					if( m.getIsSelected() ){
						mCurrentPage.getMultimedia().remove(m);
						closeFragment();
						((PageEditActivity)getActivity()).mDataSingleton.database.delete_mult(m, mCurrentPage);	
						((PageEditActivity)getActivity()).update(m);
						((PageEditActivity)getActivity()).save();						
						((PageEditActivity)getActivity()).restart();
						break;
					}
				}
			}
		});
		
		mChangeMultimediaButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				closeFragment();
			}
			
		});
	}
	
	@Override
	public void update(TObservable model) {
		((PageEditActivity)this.getActivity()).update(model);
	}
	
	private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(MultimediaOptionsFragment.this).commit();
    }
}
