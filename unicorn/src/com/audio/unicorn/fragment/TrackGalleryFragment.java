package com.audio.unicorn.fragment;

import java.io.FileNotFoundException;
import java.util.List;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.audio.unicorn.R;
import com.audio.unicorn.loader.TrackLoader;
import com.audio.unicorn.media.AlbumArtProvider;
import com.audio.unicorn.media.Track;
import com.audio.unicorn.view.gallery.TrackGalleryView;
import com.audio.unicorn.view.gallery.TrackGalleryView.OnViewInstantiatedListener;

public class TrackGalleryFragment extends Fragment implements LoaderCallbacks<List<Track>>, OnViewInstantiatedListener {

    private TrackGalleryView mTrackGalleryView;

    public static TrackGalleryFragment newInstance() {
        return new TrackGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_track_gallery, container, false);
        mTrackGalleryView = (TrackGalleryView) view;
        mTrackGalleryView.setListener(this);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        return new TrackLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> data) {
        mTrackGalleryView.setTracks(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {

    }

    @Override
    public void onViewInstantiated(ImageView view, long albumId) {
        new ImageLoadThread(getActivity(), view, albumId).start();
    }

    private class ImageLoadThread extends Thread {

        private long mAlbumId;
        private Context mContext;
        private Handler mHandler;
        private ImageView mImageView;

        public ImageLoadThread(Context context, ImageView imageView, long albumId) {
            mContext = context;
            mImageView = imageView;
            mAlbumId = albumId;
            mHandler = new Handler();
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = new AlbumArtProvider().getCircularAlbumArtwork(mContext, mAlbumId);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            } catch (FileNotFoundException e) {
                // Ignore image load fails.
            }
        }

    }

}
