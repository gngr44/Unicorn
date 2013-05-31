package com.audio.unicorn.view.gallery;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.unicorn.R;
import com.audio.unicorn.media.Track;

public class TrackGalleryView extends ViewPager {

    public interface OnViewInstantiatedListener {
        void onViewInstantiated(ImageView view, long albumId);
    }

    private OnViewInstantiatedListener mListener;

    public TrackGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setPageMargin((int) (5 * getResources().getDisplayMetrics().density + 0.5f));
    }

    public void setListener(OnViewInstantiatedListener listener) {
        mListener = listener;
    }

    public void setTracks(List<Track> tracks) {
        setAdapter(new TrackPagerAdapter(tracks));
    }

    public class TrackPagerAdapter extends PagerAdapter {

        private List<Track> mTracks;

        public TrackPagerAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public int getCount() {
            return mTracks.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.track_gallery_item_view, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.ImageView);
            TextView titleText = (TextView) view.findViewById(R.id.TitleText);
            titleText.setText(mTracks.get(position).getTitle());
            imageView.setImageResource(R.drawable.ic_launcher);
            container.addView(view);
            if (mListener != null) {
                mListener.onViewInstantiated(imageView, mTracks.get(position).getAlbumId());
            }
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public float getPageWidth(int position) {
            return (float) getMeasuredHeight() / getMeasuredWidth();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
