package edu.ualberta.adventstory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ualberta.controller.CommandManager;
import edu.ualberta.controller.OnDeleteMultimediaListener;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;

@SuppressLint({ "ValidFragment", "NewApi" })
public class MultimediaOptionsFragment extends Fragment implements
		TObserver<TObservable> {
	protected Page mCurrentPage; 
	protected MultimediaAbstract mSelectedMultimedia;

	private Button mDeleteMultimediaButton;
	private Button mChangeMultimediaButton;

	public MultimediaOptionsFragment(Page currentPage,
			MultimediaAbstract selectedMultimedia) {
		super();
		mCurrentPage = currentPage;
		mSelectedMultimedia = selectedMultimedia;
	}
	
	/**
	 * Default Constructor to avoid crash in orientation change.
	 */
	public MultimediaOptionsFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_pageedit_multimedia_options,
				container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onStart() {
		super.onStart();
		mDeleteMultimediaButton = (Button) getActivity().findViewById(
				R.id.deleteMultimediaButton);
		mChangeMultimediaButton = (Button) getActivity().findViewById(
				R.id.changeMultimediaButton);
		LinearLayout mButtonLayout = (LinearLayout) getActivity().findViewById(
				R.id.multimediaSizeLayout);
		ImageButton mButtonSmallSize = (ImageButton) getActivity()
				.findViewById(R.id.multimediaSmallSizeButton);
		ImageButton mButtonMediumSize = (ImageButton) getActivity()
				.findViewById(R.id.multimediaMediumSizeButton);
		ImageButton mButtonLargeSize = (ImageButton) getActivity()
				.findViewById(R.id.multimediaLargeSizeButton);
		Button mExitButton = (Button) getActivity().findViewById(
				R.id.exitButton);

		mDeleteMultimediaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentPage.getMultimedia() == null) {
					// UNKNOWN STATE, EXIT AT ONCE.
					closeFragment();
				}
				for (MultimediaAbstract m : mCurrentPage.getMultimedia()) {
					if (m.getIsSelected()) {
						OnDeleteMultimediaListener odm = new OnDeleteMultimediaListener(
								mCurrentPage, m,
								(ActivityExtended) getActivity());
						CommandManager cm = CommandManager.getInstance();
						cm.invokeCommand(odm);

						closeFragment();
						break;
					}
				}
			}
		});

		mChangeMultimediaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((PageEditActivity) getActivity())
						.swapMultimedia(mSelectedMultimedia.getID());
				// It is implied that a save will occur.
				closeFragment();
			}

		});

		mButtonSmallSize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelectedMultimedia instanceof Picture) {
					Picture p = (Picture) mSelectedMultimedia;
					p.setPictureSize(Picture.SMALL);
					update(p);
				}
			}
		});

		mButtonMediumSize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelectedMultimedia instanceof Picture) {
					Picture p = (Picture) mSelectedMultimedia;
					p.setPictureSize(Picture.MEDIUM);
					update(p);
				}
			}
		});

		mButtonLargeSize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelectedMultimedia instanceof Picture) {
					Picture p = (Picture) mSelectedMultimedia;
					p.setPictureSize(Picture.LARGE);
					update(p);
				}
			}
		});

		mExitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeFragment();
			}
		});

		if (!(mSelectedMultimedia instanceof Picture)) {
			mButtonLayout.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void update(TObservable model) {
		((PageEditActivity) this.getActivity()).update(model);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		this.closeFragment();
	}

	private void closeFragment() {
		((PageEditActivity) getActivity()).save();
		((PageEditActivity) getActivity()).setFRAGMENTINFLATED(false);
		getActivity().getFragmentManager().beginTransaction()
				.remove(MultimediaOptionsFragment.this).commit();
	}
}
