package io.rachelmunoz.imagethoughts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThoughtsFragment extends Fragment {

	static final String ARG_IMAGE_THOUGHT_ID = "imageThought_id";
	private static final int REQUEST_PHOTO = 0;
	private static final String TAG = "ImageThoughtsFragment";
	private ImageThought mImageThought;
	private File mPhotoFile;

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
							"io.rachelmunoz.imagethoughts.fileprovider",
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
			Uri uri = FileProvider.getUriForFile(getActivity(), "io.rachelmunoz.imagethoughts.fileprovider", mPhotoFile);
			getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

			updateImageThought();
			updatePhotoView();
		}
	}

	private void updateImageThought(){  // when stuff changed in RecyclerView
		ImageThoughtLab.get(getActivity()).updateImageThought(mImageThought);
		mCallbacks.onImageThoughtUpdated(mImageThought);
	}

	private void updatePhotoView(){
		if (mPhotoFile == null || !mPhotoFile.exists()){
			mImageThoughtImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black));
		} else {
			Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
			mImageThoughtImageView.setImageBitmap(bitmap);
		}
	}

}
