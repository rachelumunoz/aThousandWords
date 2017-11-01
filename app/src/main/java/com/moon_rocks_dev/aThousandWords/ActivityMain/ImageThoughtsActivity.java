package com.moon_rocks_dev.aThousandWords.ActivityMain;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.moon_rocks_dev.aThousandWords.ModelLayer.ImageThought;
import com.moon_rocks_dev.aThousandWords.SingleFragmentActivity;

import java.util.UUID;

public class ImageThoughtsActivity extends SingleFragmentActivity implements ImageThoughtsFragment.Callbacks {
	private static final String EXTRA_IMAGE_THOUGHT_ID = "com.moon_rocks_dev.aThousandWords.extra_imagethought_id";

	@Override
	public Fragment createFragment() {
		UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_IMAGE_THOUGHT_ID);
		ImageThoughtsFragment fragment = ImageThoughtsFragment.newInstance(id);

		return fragment;
	}

	public static Intent newIntent(Context context, UUID id){
		Intent intent = new Intent(context, ImageThoughtsActivity.class);
		intent.putExtra(EXTRA_IMAGE_THOUGHT_ID, id);
		return intent;
	}

	@Override
	public void onImageThoughtUpdated(ImageThought imageThought) {
			//
	}

	@Override
	public void onImageThoughtDeleted() {
		finish();
	}
}


