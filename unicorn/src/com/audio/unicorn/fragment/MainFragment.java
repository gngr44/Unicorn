package com.audio.unicorn.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.audio.unicorn.R;

public class MainFragment extends Fragment {
    private static final String TAG_CONTROL_1 = "TAG_CONTROL_1";
    private static final String TAG_CONTROL_2 = "TAG_CONTROL_2";
    private static final String TAG_CONTROL_3 = "TAG_CONTROL_3";
    private static final String TAG_CONTROL_4 = "TAG_CONTROL_4";
    private static final String TAG_CONTROL_5 = "TAG_CONTROL_5";

    private TrackControlFragment mControl1;
    private TrackControlFragment mControl2;
    private TrackControlFragment mControl3;
    private TrackControlFragment mControl4;
    private TrackControlFragment mControl5;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            mControl1 = TrackControlFragment.newInstance();
            mControl2 = TrackControlFragment.newInstance();
            mControl3 = TrackControlFragment.newInstance();
            mControl4 = TrackControlFragment.newInstance();
            mControl5 = TrackControlFragment.newInstance();

            transaction.add(R.id.TrackControl1, mControl1, TAG_CONTROL_1);
            transaction.add(R.id.TrackControl2, mControl2, TAG_CONTROL_2);
            transaction.add(R.id.TrackControl3, mControl3, TAG_CONTROL_3);
            transaction.add(R.id.TrackControl4, mControl4, TAG_CONTROL_4);
            transaction.add(R.id.TrackControl5, mControl5, TAG_CONTROL_5);

            transaction.commit();
        }
    }
}
