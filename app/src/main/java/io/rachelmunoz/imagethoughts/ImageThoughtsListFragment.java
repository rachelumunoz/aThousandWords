package io.rachelmunoz.imagethoughts;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListFragment extends Fragment implements DynamicRecyclerView {
	private static final String SAVED_COMPLETED_VISIBLE = "completed";

//	public static final String DEFAULT_FILTER = "default";
//	public static final String COMPLETE_FILTER = "complete";

	private RecyclerView mRecyclerView;
	private ImageThoughtAdapter mAdapter;
	private Callbacks mCallbacks;

	private String mCurrentFilter; // set to DEFAULT_FILTER

	private boolean mSubtitleVisible;

	public interface Callbacks {
		void onImageThoughtSelected(ImageThought imageThought);
	}


	@Override
	public int getViewHolderResId() { // alias resource for smallestWidth
		return R.layout.detail_list_item;
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
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI(mCurrentFilter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVED_COMPLETED_VISIBLE, mSubtitleVisible);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.new_entry:
				ImageThought imageThought = new ImageThought();
				ImageThoughtLab.get(getActivity()).addImageThought(imageThought);

				updateUI(mCurrentFilter);
				mCallbacks.onImageThoughtSelected(imageThought);
				return true;

			case R.id.completed:
				// update the UI (this list) with only ImageThoughts that are completed
//				setFilter("COMPLETED");
				mSubtitleVisible = !mSubtitleVisible;
				getActivity().invalidateOptionsMenu(); // recreates menu

				updateUI(mCurrentFilter);

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_thoughts_list, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);

		Configuration config = getResources().getConfiguration(); // set different RecylerView LayoutManager for phone vs tablet
		if (config.smallestScreenWidthDp < 600){
			mRecyclerView.setLayoutManager( new GridLayoutManager(getActivity(), 3));
		} else {
			mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		}

		if(savedInstanceState != null){
			mSubtitleVisible = savedInstanceState.getBoolean(SAVED_COMPLETED_VISIBLE);
		}

		updateUI(mCurrentFilter);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_image_thoughts_list, menu);

		MenuItem subtitleItem = menu.findItem(R.id.completed);
		if (mSubtitleVisible){
			subtitleItem.setTitle(R.string.not_completed);
		} else {
			subtitleItem.setTitle(R.string.completed);
		}
	}

	private class ImageThoughtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView mImageThoughtImageView;
		private ImageThought mImageThought;
		private TextView mImageThoughtTitle;

		private File mPhotoFile;

		public ImageThoughtHolder(LayoutInflater inflater, ViewGroup parent){
			super(inflater.inflate(getViewHolderResId(), parent, false));
			itemView.setOnClickListener(this);

			mImageThoughtImageView = (ImageView) itemView.findViewById(R.id.image_recycler);

			mImageThoughtTitle = (TextView) itemView.findViewById(R.id.imageThought_title);
		}

		@Override
		public void onClick(View view) {
			mCallbacks.onImageThoughtSelected(mImageThought);
		}

		public void bind(ImageThought imageThought){ // binds data each imageThought to UI
			mImageThought = imageThought;

			mPhotoFile = ImageThoughtLab.get(getActivity()).getPhotoFile(mImageThought);

			// repeats from ImageThoughtsFragment
			if (mPhotoFile == null || !mPhotoFile.exists()){
				mImageThoughtImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black));
			} else {
				Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
				mImageThoughtImageView.setImageBitmap(bitmap);
			}


			if (mImageThoughtTitle != null){
				mImageThoughtTitle.setText(mImageThought.getTitle());
			}

		}
	}

	private class ImageThoughtAdapter extends RecyclerView.Adapter<ImageThoughtHolder>{
		private List<ImageThought> mImageThoughts;

		public ImageThoughtAdapter(List<ImageThought> imageThoughts){
			mImageThoughts = imageThoughts;
		}

		@Override
		public ImageThoughtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

			return new ImageThoughtHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(ImageThoughtHolder holder, int position) {
			ImageThought imageThought = mImageThoughts.get(position);
			holder.bind(imageThought);
		}

		@Override
		public int getItemCount() {
			return mImageThoughts.size();
		}

		public void setImageThoughts(List<ImageThought>imageThoughts){
			mImageThoughts = imageThoughts;
		}
	}

	public void updateUI(String currentFilter){  // string filterType
		ImageThoughtLab imageThoughtLab = ImageThoughtLab.get(getActivity());
		List<ImageThought> imageThoughts = imageThoughtLab.getImageThoughts(mCurrentFilter);

		if (mAdapter == null){ //on Activity recreate?
			mAdapter = new ImageThoughtAdapter(imageThoughts);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.setImageThoughts(imageThoughts);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setFilter(String filter){
		mCurrentFilter = filter;
	}


}


// in updateUI -- get imageThoughts will require a filter type

// this filter type will be set to default of default
// will change on click of menu item
	// which will trigger an updateUI with new filter type


// listFragment will have a field called mCurrentFiltering initialized to ALL_IMAGE_THOUGHTS
// setFiltering in case statement of menu

// add completed option in menu
// when this is clicked, will invoke updateUI that will get the filtered imageTHoughts

// activity might be recreated with a selected filter
// want to start a fragment with looking for a filter -- add in the static factory method
	// in the activity, add a private static final string of EXTRA_FILTER = "list_filter";