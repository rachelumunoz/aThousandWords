package io.rachelmunoz.imagethoughts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

	public abstract Fragment createFragment();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_thoughts);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.image_thoughts_fragment_container);

		if (fragment == null){
			fragment = createFragment();

			fm.beginTransaction()
					.add(R.id.image_thoughts_fragment_container, fragment)
					.commit();

		}

	}
}
