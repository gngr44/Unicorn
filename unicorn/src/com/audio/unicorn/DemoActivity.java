package com.audio.unicorn;

import android.media.AudioTrack;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.audio.unicorn.engine.UnicornAudioEngine;
import com.audio.unicorn.engine.exception.InvalidMediaTypeException;
import com.audio.unicorn.processor.GainProcessor;

public class DemoActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MainActivity";

    private Button mBtnStart1;
    private Button mBtnStop1;

    private Button mBtnStart2;
    private Button mBtnStop2;

    private Button mBtnStart3;
    private Button mBtnStop3;

    private SeekBar mSeekBar1;
    private SeekBar mSeekBar2;
    private SeekBar mSeekBar3;

    private UnicornAudioEngine mAudioEngine1;
    private UnicornAudioEngine mAudioEngine2;
    private UnicornAudioEngine mAudioEngine3;

    private GainProcessor mGainProcessor1;
    private GainProcessor mGainProcessor2;
    private GainProcessor mGainProcessor3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mBtnStart1 = (Button) findViewById(R.id.btnStart1);
        mBtnStop1 = (Button) findViewById(R.id.btnStop1);

        mBtnStart2 = (Button) findViewById(R.id.btnStart2);
        mBtnStop2 = (Button) findViewById(R.id.btnStop2);

        mBtnStart3 = (Button) findViewById(R.id.btnStart3);
        mBtnStop3 = (Button) findViewById(R.id.btnStop3);

        mSeekBar1 = (SeekBar) findViewById(R.id.sbScrubber1);
        mSeekBar2 = (SeekBar) findViewById(R.id.sbScrubber2);
        mSeekBar3 = (SeekBar) findViewById(R.id.sbScrubber3);

        mBtnStart1.setOnClickListener(this);
        mBtnStop1.setOnClickListener(this);

        mBtnStart2.setOnClickListener(this);
        mBtnStop2.setOnClickListener(this);

        mBtnStart3.setOnClickListener(this);
        mBtnStop3.setOnClickListener(this);

        mSeekBar1.setMax(100);
        mSeekBar2.setMax(100);
        mSeekBar3.setMax(100);

        mSeekBar1.setProgress(100);
        mSeekBar2.setProgress(100);
        mSeekBar3.setProgress(100);

        mSeekBar1.setOnSeekBarChangeListener(this);
        mSeekBar2.setOnSeekBarChangeListener(this);
        mSeekBar3.setOnSeekBarChangeListener(this);

        mAudioEngine1 = new UnicornAudioEngine("/sdcard/Music/Fiction [Funkagenda Bootleg Re-Edit].mp3");
        mAudioEngine2 = new UnicornAudioEngine("/sdcard/Music/Dimitri Vegas & Like Mike - Madness feat. Lil Jon and Coone (Corporate Slackrs Remix).mp3");
        mAudioEngine3 = new UnicornAudioEngine("/sdcard/Music/Drake - 5AM In Toronto.mp3");

        mGainProcessor1 = new GainProcessor();
        mGainProcessor2 = new GainProcessor();
        mGainProcessor3 = new GainProcessor();

        mAudioEngine1.addProcessor(mGainProcessor1);
        mAudioEngine2.addProcessor(mGainProcessor2);
        mAudioEngine3.addProcessor(mGainProcessor3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnStart1) {
            try {
                mAudioEngine1.start();
            } catch (InvalidMediaTypeException e) {
                Log.e(TAG, "Invalid media", e);
            }

        } else if (view == mBtnStop1) {
            mAudioEngine1.stop();
        } else if (view == mBtnStart2) {
            try {
                mAudioEngine2.start();
            } catch (InvalidMediaTypeException e) {
                Log.e(TAG, "Invalid media", e);
            }

        } else if (view == mBtnStop2) {
            mAudioEngine2.stop();
        } else if (view == mBtnStart3) {
            try {
                mAudioEngine3.start();
            } catch (InvalidMediaTypeException e) {
                Log.e(TAG, "Invalid media", e);
            }

        } else if (view == mBtnStop3) {
            mAudioEngine3.stop();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == mSeekBar1) {
//            mGainProcessor1.setGain((float) (i / (float) seekBar.getMax()));
            mAudioEngine1.setSamplingRate((float) (i / (float) seekBar.getMax()));
        } else if (seekBar == mSeekBar2) {
            mAudioEngine2.setSamplingRate((float) (i / (float) seekBar.getMax()));
//            mGainProcessor2.setGain((float) (i / (float) seekBar.getMax()));
        } else if (seekBar == mSeekBar3) {
            mAudioEngine3.setSamplingRate((float) (i / (float) seekBar.getMax()));
//            mGainProcessor3.setGain((float) (i / (float) seekBar.getMax()));
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
