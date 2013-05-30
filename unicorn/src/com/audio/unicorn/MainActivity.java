package com.audio.unicorn;

import com.audio.unicorn.fragment.TrackGalleryFragment;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    private static final String TAG_TRACK_GALLERY_FRAGMENT = "TAG_TRACK_GALLERY_FRAGMENT";
    private TrackGalleryFragment mTrackGalleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mTrackGalleryFragment = TrackGalleryFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.TrackGalleryContainer, mTrackGalleryFragment, TAG_TRACK_GALLERY_FRAGMENT).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
