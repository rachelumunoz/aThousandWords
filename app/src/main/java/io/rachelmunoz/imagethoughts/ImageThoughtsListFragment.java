package io.rachelmunoz.imagethoughts;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListFragment extends Fragment {
	private RecyclerView mRecyclerView;
	private ImageThoughtAdapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_thoughts_list, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
		mRecyclerView.setLayoutManager( new GridLayoutManager(getActivity(), 2));

		updateUI();

		return view;
	}

	private class ImageThoughtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView mImageThoughtImageView;
		private ImageThought mImageThought;
		private TextView mImageThoughtDate;

		public ImageThoughtHolder(LayoutInflater inflater, ViewGroup parent){
			super(inflater.inflate(R.layout.list_item_image_thought, parent, false));
			itemView.setOnClickListener(this);
			mImageThoughtImageView = (ImageView) itemView.findViewById(R.id.image_recycler);
			mImageThoughtDate = (TextView) itemView.findViewById(R.id.image_date_recycler);
		}

		@Override
		public void onClick(View view) {
			Intent intent = ImageThoughtsActivity.newIntent(getActivity(), mImageThought.getId());
			startActivity(intent);
		}

		public void bind(ImageThought imageThought){ // binds data each imageThought to UI
			mImageThought = imageThought;
			mImageThoughtDate.setText(imageThought.getFormattedDate());
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
	}

	private void updateUI(){
		ImageThoughtLab imageThoughtLab = ImageThoughtLab.get(getActivity());
		List<ImageThought> imageThoughts = imageThoughtLab.getImageThoughts();

		mAdapter = new ImageThoughtAdapter(imageThoughts);
		mRecyclerView.setAdapter(mAdapter);
	}

}
