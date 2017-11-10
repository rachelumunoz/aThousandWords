package com.moon_rocks_dev.aThousandWords.AcitivityList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.moon_rocks_dev.aThousandWords.DynamicRecyclerView;
import com.moon_rocks_dev.aThousandWords.ModelLayer.ImageThought;
import com.moon_rocks_dev.aThousandWords.ModelLayer.ImageThoughtLab;
import com.moon_rocks_dev.aThousandWords.PictureUtils;
import com.moon_rocks_dev.aThousandWords.R;

import java.io.File;
import java.util.List;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListFragment extends Fragment implements DynamicRecyclerView {
	private static final String SAVED_COMPLETED_VISIBLE = "completed";

	public static final String ARGS_LIST_FILTER_TYPE = "ImageThoughtsListFilter";
	public static final String ARGS_IT_COMPLETE = "ImageThoughtComplete";

	public static final String ALL_FILTER = "all";

	public static final String COMPLETE_FILTER = "completed";

	private RecyclerView mRecyclerView;

	private ImageThoughtAdapter mAdapter;

	private Callbacks mCallbacks;

	private String mCurrentFilter = ALL_FILTER;

	private boolean mSubtitleVisible;

	private File mPhotoFile;

	public interface Callbacks {
		void onImageThoughtSelected(ImageThought imageThought);

		void updateList(String currentFilter);
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

	public static ImageThoughtsListFragment newInstance() {
		Bundle args = new Bundle();
		args.putSerializable(ARGS_LIST_FILTER_TYPE, ALL_FILTER);

		ImageThoughtsListFragment frag = new ImageThoughtsListFragment();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
//		setFilterBundleArg(mCurrentFilter);
		// set default
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
		switch (item.getItemId()) {
			case R.id.new_entry:
				ImageThought imageThought = new ImageThought();
				ImageThoughtLab.get(getActivity()).addImageThought(imageThought);

				updateUI(mCurrentFilter);
				mCallbacks.onImageThoughtSelected(imageThought);
				return true;

			case R.id.completed:
				// toggle filter type  -- refactor to case statement when more filters are added
				if (mCurrentFilter == COMPLETE_FILTER) {
					setCurrentFilter(ALL_FILTER);
				} else {
					setCurrentFilter(COMPLETE_FILTER);
				}

				// update the filter on each click, so filter type text will be set
				mSubtitleVisible = !mSubtitleVisible;

				// recreate menu
				getActivity().invalidateOptionsMenu();
//				updateUI(mCurrentFilter); // gets filtered ImageThoughts
				// need to remove fragment if in tablet view-- callbacks?
				mCallbacks.updateList(mCurrentFilter);

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

		Configuration config = getResources().getConfiguration(); // set different RecyclerView LayoutManager for phone vs tablet
		if (config.smallestScreenWidthDp < 600) {
			mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
		} else {
			mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		}

		if (savedInstanceState != null) {
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
		if (mCurrentFilter == COMPLETE_FILTER) {
			subtitleItem.setTitle(R.string.not_completed);
		} else {
			subtitleItem.setTitle(R.string.completed);
		}
	}

	private class ImageThoughtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView mImageThoughtImageView;
		private ImageThought mImageThought;
		private TextView mImageThoughtTitle;


		public ImageThoughtHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(getViewHolderResId(), parent, false));
			itemView.setOnClickListener(this);

			mImageThoughtImageView = (ImageView) itemView.findViewById(R.id.image_recycler);

			mImageThoughtTitle = (TextView) itemView.findViewById(R.id.imageThought_title);
		}

		@Override
		public void onClick(View view) {
			mCallbacks.onImageThoughtSelected(mImageThought);
			setImageThoughtCompleteBundleArg(mImageThought.isThoughtComplete());
		}

		public void bind(ImageThought imageThought, ImageView v) { // binds data each imageThought to UI
			mImageThought = imageThought;
			mPhotoFile = ImageThoughtLab.get(getActivity()).getPhotoFile(mImageThought);

			// repeats from ImageThoughtsFragment -- logic from updatePhotoView()
			if (mPhotoFile == null || !mPhotoFile.exists()) {
				mImageThoughtImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_black));
			} else {
				Glide.with(getActivity())
						.load(mPhotoFile)
						.apply(new RequestOptions()
								.placeholder(getResources().getDrawable(R.drawable.ic_photo_black))
								.signature(new MediaStoreSignature("image/jpeg", new java.util.Date().getTime(), 0)))
						.into(v);
			}

			mImageThoughtImageView.setAdjustViewBounds(true);
			mImageThoughtImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			if (mImageThoughtTitle != null) {
				mImageThoughtTitle.setText(mImageThought.getTitle());
			}


		}
	}

	private class ImageThoughtAdapter extends RecyclerView.Adapter<ImageThoughtHolder> {
		private List<ImageThought> mImageThoughts;

		public ImageThoughtAdapter(List<ImageThought> imageThoughts) {
			mImageThoughts = imageThoughts;
		}

		@Override
		public ImageThoughtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new ImageThoughtHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(ImageThoughtHolder holder, int position) {
			ImageView iv = holder.mImageThoughtImageView;
			ImageThought imageThought = mImageThoughts.get(position);
			mPhotoFile = ImageThoughtLab.get(getActivity()).getPhotoFile(imageThought);
			holder.bind(imageThought, iv);
		}

		@Override
		public int getItemCount() {
			return mImageThoughts.size();
		}

		public void setImageThoughts(List<ImageThought> imageThoughts) {
			mImageThoughts = imageThoughts;
		}
	}

	public void updateUI(String currentFilter) {  // string filterType
		ImageThoughtLab imageThoughtLab = ImageThoughtLab.get(getActivity());
		List<ImageThought> imageThoughts = imageThoughtLab.getImageThoughts(currentFilter);

		if (mAdapter == null) { //on Activity recreate?
			mAdapter = new ImageThoughtAdapter(imageThoughts);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.setImageThoughts(imageThoughts);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void setCurrentFilter(String filter) {
		mCurrentFilter = filter;
		setFilterBundleArg(filter);
	}

	private void setFilterBundleArg(String filter) {
		Bundle args = this.getArguments();
		args.putSerializable(ARGS_LIST_FILTER_TYPE, filter);
	}

	private void setImageThoughtCompleteBundleArg(boolean complete) {
		Bundle args = this.getArguments();
		args.putSerializable(ARGS_IT_COMPLETE, complete);
	}

	public String getCurrentFilter() {
		return mCurrentFilter;
	}
}