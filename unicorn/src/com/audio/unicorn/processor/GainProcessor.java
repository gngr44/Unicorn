package com.audio.unicorn.processor;

/**
 * Created by jsaund on 5/30/13.
 */
public class GainProcessor implements UnicornAudioProcessor {
    public static final float GAIN_MIN = 0.0f;
    public static final float GAIN_MAX = 1.0f;

    private float mGain = GAIN_MAX;

    public GainProcessor() {

    }

    public void setGain(float gain) {
        mGain = gain;
    }

    public synchronized float getGain() {
        return mGain;
    }

    @Override
    public byte[] processAudio(byte[] buffer) {
        for (int i=0; i<buffer.length; i++) {
            buffer[i] = (byte) (buffer[i] * mGain);
        }

        return buffer;
    }
}
