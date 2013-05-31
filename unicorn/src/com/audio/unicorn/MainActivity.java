package com.audio.unicorn;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.audio.unicorn.fragment.MainFragment;
import com.audio.unicorn.fragment.TrackGalleryFragment;

public class MainActivity extends Activity {

    private static final String TAG_TRACK_GALLERY_FRAGMENT = "TAG_TRACK_GALLERY_FRAGMENT";
    private static final String TAG_MAIN_FRAGMENT = "TAG_MAIN_FRAGMENT";
    private TrackGalleryFragment mTrackGalleryFragment;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mTrackGalleryFragment = TrackGalleryFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.TrackGalleryContainer, mTrackGalleryFragment, TAG_TRACK_GALLERY_FRAGMENT).commit();
            mMainFragment = MainFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.MainContainer, mMainFragment, TAG_MAIN_FRAGMENT).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

}
