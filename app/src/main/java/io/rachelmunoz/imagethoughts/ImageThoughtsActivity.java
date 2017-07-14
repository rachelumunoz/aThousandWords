package io.rachelmunoz.imagethoughts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ImageThoughtsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_thoughts);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.image_thoughts_fragment_container);

		if (fragment == null){
			fragment = new ImageThoughtsFragment();

			fm.beginTransaction()
				.add(R.id.image_thoughts_fragment_container, fragment)
				.commit();

		}

	}
}
