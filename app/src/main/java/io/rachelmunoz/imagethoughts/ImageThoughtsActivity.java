package io.rachelmunoz.imagethoughts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class ImageThoughtsActivity extends SingleFragmentActivity {
	private static final String EXTRA_IMAGETHOUGHT_ID = "io.rachelmunoz.imagethoughts.extra_imagethought_id";

	@Override
	public Fragment createFragment() {
		return new ImageThoughtsFragment();
	}

	public static Intent newIntent(Context context, UUID id){
		Intent intent = new Intent(context, ImageThoughtsActivity.class);
		intent.putExtra(EXTRA_IMAGETHOUGHT_ID, id);
		return intent;
	}
}
