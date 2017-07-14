package io.rachelmunoz.imagethoughts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThoughtsFragment extends Fragment {




	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_thoughts, container, false);

		return view;
	}
}
