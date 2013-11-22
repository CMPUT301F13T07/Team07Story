package edu.ualberta.adventstory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;

@SuppressLint({ "ValidFragment", "NewApi" })
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
	
	@SuppressLint("NewApi")
	@Override
	public void onStart(){
		super.onStart();
		
		mMultimediaTypeTextView = (TextView)getActivity().findViewById(R.id.multimediaTypeTextView);
		mMultimediaFilePath = (TextView)getActivity().findViewById(R.id.multimediaFileNameTextView);
		mDeleteMultimediaButton = (Button)getActivity().findViewById(R.id.deleteMultimediaButton);
		mChangeMultimediaButton = (Button)getActivity().findViewById(R.id.changeMultimediaButton);
		LinearLayout mButtonLayout = (LinearLayout)getActivity().findViewById(R.id.multimediaSizeLayout);
		ImageButton mButtonSmallSize = (ImageButton)getActivity().findViewById(R.id.multimediaSmallSizeButton); 
		ImageButton mButtonMediumSize = (ImageButton)getActivity().findViewById(R.id.multimediaMediumSizeButton); 
		ImageButton mButtonLargeSize = (ImageButton)getActivity().findViewById(R.id.multimediaLargeSizeButton);
		
		// Set mMultimediaTypeTextView's string.
		for(MultimediaAbstract m : mCurrentPage.getMultimedia()){
			if( m.getIsSelected() ){
				mMultimediaTypeTextView.setText(m.getClass().getName());
				break;
			}
		}
		
		// Set mMultimediaFilePath string.
		for(MultimediaAbstract m : mCurrentPage.getMultimedia()){
			if( m.getIsSelected() ){
				mMultimediaFilePath.setText(m.getFileDir());
				break;
			}
		}
		
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
		
		mButtonSmallSize.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				MultimediaAbstract m = getSelectedMultimedia();
				if( m instanceof Picture ){
					Picture p = (Picture)m;
					p.setPictureSize(Picture.SMALL);
				}
			}
		});
		
		mButtonMediumSize.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				MultimediaAbstract m = getSelectedMultimedia();
				if( m instanceof Picture ){
					Picture p = (Picture)m;
					p.setPictureSize(Picture.MEDIUM);
				}
			}
		});
		
		mButtonLargeSize.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				MultimediaAbstract m = getSelectedMultimedia();
				if( m instanceof Picture ){
					Picture p = (Picture)m;
					p.setPictureSize(Picture.LARGE);
				}
			}
		});
		
		if(!(getSelectedMultimedia() instanceof Picture)){
			mButtonLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void update(TObservable model) {
		((PageEditActivity)this.getActivity()).update(model);
	}
	
	private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(MultimediaOptionsFragment.this).commit();
    }
	
	/**
	 * Acquire the currently selected multimedia in current page.
	 * @return
	 */
	private MultimediaAbstract getSelectedMultimedia(){
		for(MultimediaAbstract m : mCurrentPage.getMultimedia()){
			if(m.getIsSelected()){
				return m;
			}
		}
		
		// UNKNOWN STATE.
		return null;
	}
}
