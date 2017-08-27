package io.rachelmunoz.imagethoughts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListActivity extends SingleFragmentActivity implements ImageThoughtsListFragment.Callbacks, ImageThoughtsFragment.Callbacks {

	@Override
	public Fragment createFragment() {
		return new ImageThoughtsListFragment();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onImageThoughtSelected(ImageThought imageThought) {
		if (findViewById(R.id.detail_fragment_container) == null){
			Intent intent = ImageThoughtsActivity.newIntent(this, imageThought.getId());
			startActivity(intent);
	 	} else {
			Fragment newDetail = ImageThoughtsFragment.newInstance(imageThought.getId());

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detail_fragment_container, newDetail)
					.commit();
		}
	}

	@Override
	public void onImageThoughtUpdated(ImageThought imageThought) {
		ImageThoughtsListFragment listFragment = (ImageThoughtsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.image_thoughts_fragment_container);
		listFragment.updateUI();
	}
}
