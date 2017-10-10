package io.rachelmunoz.aThousandWords;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListActivity extends SingleFragmentActivity implements ImageThoughtsListFragment.Callbacks, ImageThoughtsFragment.Callbacks {

	private static final String TAG = "ImageThoughtsListAct";

	@Override
	public Fragment createFragment() {
		return ImageThoughtsListFragment.newInstance();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	@Override
	public void updateList(String currentFilter) {
		ImageThoughtsListFragment listFragment = (ImageThoughtsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.image_thoughts_fragment_container);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);

		if (fragment != null){
			String filter = listFragment.getArguments().getString(ImageThoughtsListFragment.ARGS_LIST_FILTER_TYPE);

			boolean complete = listFragment.getArguments().getBoolean(ImageThoughtsListFragment.ARGS_IT_COMPLETE);
//			Log.d(TAG, "the current filter selected is " + filter);
//			Log.d(TAG, "current imageThought is complete: " + complete);

			switch(filter){
				case ImageThoughtsListFragment.ALL_FILTER:
					if (complete == true){
						getSupportFragmentManager().beginTransaction().remove(fragment).commit();
					}
					break;
				case ImageThoughtsListFragment.COMPLETE_FILTER:
					if(complete == false){
						getSupportFragmentManager().beginTransaction().remove(fragment).commit();
					}
					break;
				default:
					getSupportFragmentManager().beginTransaction().remove(fragment).commit();
			}
		}

		listFragment.updateUI(currentFilter);
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

		listFragment.updateUI(listFragment.getCurrentFilter()); //updates list to current Filter, but the newly updated ImageThought not reflected
	}

	@Override
	public void onImageThoughtDeleted() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
		ImageThoughtsListFragment listFragment = (ImageThoughtsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.image_thoughts_fragment_container);

		if (fragment != null){
			getSupportFragmentManager().beginTransaction().remove(fragment).commit();

			listFragment.updateUI(listFragment.getCurrentFilter());
		}
	}
}

