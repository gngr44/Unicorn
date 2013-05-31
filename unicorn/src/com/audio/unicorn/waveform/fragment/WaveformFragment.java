package com.audio.unicorn.waveform.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.audio.unicorn.R;
import com.audio.unicorn.loader.BaseLoader;
import com.audio.unicorn.waveform.CheapSoundFile;
import com.audio.unicorn.waveform.view.WaveformView;
import com.audio.unicorn.waveform.view.WaveformView.WaveformListener;

public class WaveformFragment extends Fragment implements LoaderCallbacks<CheapSoundFile>, WaveformListener {
    private static final String ARG_FILE_PATH = "ARG_FILE_PATH";

    public static WaveformFragment newInstance() {
        return new WaveformFragment();
    }

    private WaveformView mWaveformView;
    private boolean mFirstWaveform;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waveform, container, false);
        mWaveformView = (WaveformView) view;
        mFirstWaveform = true;
        return view;
    }

    public void setFilePath(String filePath) {
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        getLoaderManager().restartLoader(0, args, this);
    }

    @Override
    public Loader<CheapSoundFile> onCreateLoader(int id, Bundle args) {
        final String filePath = args.getString(ARG_FILE_PATH);
        return new WavLoader(getActivity(), filePath);
    }

    @Override
    public void onLoadFinished(Loader<CheapSoundFile> loader, CheapSoundFile data) {
        if (data != null) {
            mWaveformView.setListener(this);
            mWaveformView.setSoundFile(data);
            mWaveformView.recomputeHeights(getResources().getDisplayMetrics().density);

            if (mFirstWaveform) {
                mFirstWaveform = false;
                ObjectAnimator.ofFloat(mWaveformView, "translationY", mWaveformView.getHeight(), 0.0f).start();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<CheapSoundFile> loader) {
    }

    @Override
    public void waveformTouchStart(float x) {
    }

    @Override
    public void waveformTouchMove(float x) {
    }

    @Override
    public void waveformTouchEnd() {
    }

    @Override
    public void waveformFling(float x) {
    }

    @Override
    public void waveformDraw() {
    }

    @Override
    public void waveformZoomIn() {
    }

    @Override
    public void waveformZoomOut() {
    }

    private static class WavLoader extends BaseLoader<CheapSoundFile> {

        private String mFilePath;

        public WavLoader(Context context, String filePath) {
            super(context);
            mFilePath = filePath;
        }

        @Override
        public CheapSoundFile loadInBackground() {
            try {
                CheapSoundFile wav = CheapSoundFile.create(mFilePath, null);
                wav.ReadFile(new File(mFilePath));
                return wav;
            } catch (FileNotFoundException e) {
                Log.e("TEST", "", e);
            } catch (IOException e) {
                Log.e("TEST", "", e);
            }
            return null;
        }

    }
}
