package com.moon_rocks_dev.aThousandWords.ActivityMain;


import android.graphics.Bitmap;
import android.widget.EditText;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.moon_rocks_dev.aThousandWords.ModelLayer.ImageThought;
import com.moon_rocks_dev.aThousandWords.ModelLayer.ImageThoughtLab;
import com.moon_rocks_dev.aThousandWords.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThoughtsFragment extends Fragment {

	static final String ARG_IMAGE_THOUGHT_ID = "imageThought_id";
	private static final int REQUEST_PHOTO = 0;
	private static final String TAG = "ImageThoughtsFragment";
	private File mPhotoFile;
	private ImageThought mImageThought;

	private EditText mImageThoughtEditText;
	private EditText mTitleEditText;
	private TextView mImageThoughtDateTextView;
	private CheckBox mImageThoughtCompleteCheckBox;
	private ImageView mImageThoughtImageView;
	private Button mCameraButton;

	private Callbacks mCallbacks;

	public interface Callbacks {
		void onImageThoughtUpdated(ImageThought imageThought);
		void onImageThoughtDeleted();
	}

	public static ImageThoughtsFragment newInstance(UUID id){
		Bundle args = new Bundle();
		args.putSerializable(ARG_IMAGE_THOUGHT_ID, id);

		ImageThoughtsFragment fragment = new ImageThoughtsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mCallbacks = (Callbacks) context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		UUID imageThoughtId = (UUID) getArguments().getSerializable(ARG_IMAGE_THOUGHT_ID);
		mImageThought = ImageThoughtLab.get(getActivity()).getImageThought(imageThoughtId);
		mPhotoFile = ImageThoughtLab.get(getActivity()).getPhotoFile(mImageThought);
	}

	@Override
	public void onPause() {
		super.onPause();
		ImageThoughtLab.get(getActivity()).updateImageThought(mImageThought);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_image_thought, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.delete_entry:
				ImageThoughtLab.get(getActivity()).deleteImageThought(mImageThought);
				mCallbacks.onImageThoughtDeleted(); // different actions needed depending on device type

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_thoughts, container, false);

		mImageThoughtEditText = (EditText) view.findViewById(R.id.imageThought_text);

		String imageThoughText = mImageThought.getThought();

		// if imageThoughtText is empty, put descirption, else use thought
		mImageThoughtEditText.setText(mImageThought.getThought());
		mImageThoughtEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				mImageThought.setThought(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		mTitleEditText = (EditText) view.findViewById(R.id.imageThought_title);
		mTitleEditText.setText(mImageThought.getTitle());

		mTitleEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				mImageThought.setTitle(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		mImageThoughtDateTextView = (TextView) view.findViewById(R.id.imageThought_date);
		mImageThoughtDateTextView.setText(mImageThought.getFormattedDate());

		mImageThoughtCompleteCheckBox = (CheckBox) view.findViewById(R.id.imageThought_complete);
		mImageThoughtCompleteCheckBox.setChecked(mImageThought.isThoughtComplete());
		mImageThoughtCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				mImageThought.setThoughtComplete(b);
				updateImageThought();
			}
		});

		PackageManager packageManager = getActivity().getPackageManager();
		mCameraButton = (Button) view.findViewById(R.id.camera_button);
		final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
		mCameraButton.setEnabled(canTakePhoto);

		mCameraButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				Uri uri =
						FileProvider.getUriForFile(
							getActivity(),
								"com.moon_rocks_dev.aThousandWords.fileprovider",
							mPhotoFile
						);

				captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				List<ResolveInfo> cameraActivities =
									getActivity()
									.getPackageManager()
									.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

				for (ResolveInfo activity : cameraActivities){
					getActivity()
					.grantUriPermission(
						activity.activityInfo.packageName,
						uri,
						Intent.FLAG_GRANT_WRITE_URI_PERMISSION
					);
				}

				startActivityForResult(captureImage, REQUEST_PHOTO);
			}
		});

		mImageThoughtImageView = (ImageView) view.findViewById(R.id.imageThought_image);
		updatePhotoView();
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK){
			return;
		}

		if (requestCode == REQUEST_PHOTO){
			Uri uri = FileProvider.getUriForFile(getActivity(),  "com.moon_rocks_dev.aThousandWords.fileprovider", mPhotoFile);
			getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

			updateImageThought();
			updatePhotoView();
		}
	}

	private void updateImageThought(){  // when stuff changed in RecyclerView in tablet
		ImageThoughtLab.get(getActivity()).updateImageThought(mImageThought); // for DB
		mCallbacks.onImageThoughtUpdated(mImageThought);
	}

	private void updatePhotoView(){
		if (mPhotoFile == null || !mPhotoFile.exists()){
			mImageThoughtImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black));
		} else {
			Glide.with(getActivity())
					.load(mPhotoFile)
					.apply(new RequestOptions()
							.placeholder(getResources().getDrawable(R.drawable.ic_photo_black))
							.signature(new MediaStoreSignature("image/jpeg", new java.util.Date().getTime(),0)))
					.into(mImageThoughtImageView);
		}
	}
}
