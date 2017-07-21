package io.rachelmunoz.imagethoughts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThoughtsFragment extends Fragment {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageThought mImageThought;

	private EditText mImageThoughtEditText;
	private TextView mImageThoughtDateTextView;
	private CheckBox mImageThoughtCompleteCheckBox;
	private ImageView mImageThoughtImageView;
	private Button mCameraButton;

	private Context context;
	private File mPhotoFile;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mImageThought = new ImageThought(getString(R.string.thought_text_default));
//		mPhotoFile = getPhotoFile(getActivity(), mImageThought);

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

		mImageThoughtDateTextView = (TextView) view.findViewById(R.id.imageThought_date);
		mImageThoughtDateTextView.setText(mImageThought.getFormattedDate());

		mImageThoughtCompleteCheckBox = (CheckBox) view.findViewById(R.id.imageThought_complete);
		mImageThoughtCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				mImageThought.setThoughtComplete(b);
			}
		});


		mCameraButton = (Button) view.findViewById(R.id.camera_button);

		final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		boolean canTakePhoto = takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null;
		mCameraButton.setEnabled(canTakePhoto);

		mCameraButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				Uri uri = FileProvider.getUriForFile(getActivity(), "io.rachelmunoz.imagethoughts.fileprovider", mPhotoFile);

//				takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
//
//				List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
//
//				for (ResolveInfo activity : cameraActivities){
//					getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//				}

				mPhotoFile = null;
				mPhotoFile = getPhotoFile(getActivity(), mImageThought);

				if (mPhotoFile != null) {
					Uri photoURI = FileProvider.getUriForFile(getActivity(),
							"io.rachelmunoz.imagethoughts.fileprovider",
							mPhotoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}

			}
		});

		mImageThoughtImageView = (ImageView) view.findViewById(R.id.imageThought_image);

		return view;
	}

	private File getPhotoFile(Context context, ImageThought imageThought){
		File filesDir = context.getApplicationContext().getFilesDir();
		return new File(filesDir, imageThought.getPhotoFilename());
	}


}
