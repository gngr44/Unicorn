package com.audio.unicorn.processor;

/**
 * Created by jsaund on 5/30/13.
 */
public class TimeStretchProcessor implements UnicornAudioProcessor {

    private int mSeekWindowLength;
    private int mSeekLength;
    private int mOverlapLength;
    private int mSkip;
    private int mSampleReq;

    private double mTempo;

    private float[] mMidBuffer;
    private float[] mRefMidBuffer;

    private Parameters mParameters;

    public TimeStretchProcessor(Parameters parameters) {
        mParameters = parameters;

        mOverlapLength = (int) ((mParameters.getSampleRate() * mParameters.getOverlapMs())/1000);
        mSeekWindowLength = (int) ((mParameters.getSampleRate() * mParameters.getSequenceMs())/1000);
        mSeekLength = (int) ((mParameters.getSampleRate() *  mParameters.getSeekWindowMs())/1000);

        mTempo = mParameters.getTempo();

        mMidBuffer = new float[mOverlapLength * 8];
        mRefMidBuffer = new float[mOverlapLength * 8];

        double nominalSkip = mTempo * (mSeekWindowLength - mOverlapLength);
        mSkip = (int) (nominalSkip + 0.5);

        mSampleReq = Math.max(mSkip + mOverlapLength, mSeekWindowLength) + mSeekLength;


    }

    @Override
    public byte[] processAudio(byte[] buffer) {
        return new byte[0];
    }

    public static class Parameters {
        private final int mSequenceMs;
        private final int mSeekWindowMs;
        private final int mOverlapMs;

        private final double mTempo;
        private final double mSampleRate;

        /**
         * @param tempo
         *            The tempo change 1.0 means unchanged, 2.0 is + 100% , 0.5
         *            is half of the speed.
         * @param sampleRate
         *            The sample rate of the audio 44.1kHz is common.
         * @param newSequenceMs
         *            Length of a single processing sequence, in milliseconds.
         *            This determines to how long sequences the original sound
         *            is chopped in the time-stretch algorithm.
         *
         *            The larger this value is, the lesser sequences are used in
         *            processing. In principle a bigger value sounds better when
         *            slowing down tempo, but worse when increasing tempo and
         *            vice versa.
         *
         *            Increasing this value reduces computational burden & vice
         *            versa.
         * @param newSeekWindowMs
         *            Seeking window length in milliseconds for algorithm that
         *            finds the best possible overlapping location. This
         *            determines from how wide window the algorithm may look for
         *            an optimal joining location when mixing the sound
         *            sequences back together.
         *
         *            The bigger this window setting is, the higher the
         *            possibility to find a better mixing position will become,
         *            but at the same time large values may cause a "drifting"
         *            artifact because consequent sequences will be taken at
         *            more uneven intervals.
         *
         *            If there's a disturbing artifact that sounds as if a
         *            constant frequency was drifting around, try reducing this
         *            setting.
         *
         *            Increasing this value increases computational burden &
         *            vice versa.
         * @param newOverlapMs
         *            Overlap length in milliseconds. When the chopped sound
         *            sequences are mixed back together, to form a continuous
         *            sound stream, this parameter defines over how long period
         *            the two consecutive sequences are let to overlap each
         *            other.
         *
         *            This shouldn't be that critical parameter. If you reduce
         *            the DEFAULT_SEQUENCE_MS setting by a large amount, you
         *            might wish to try a smaller value on this.
         *
         *            Increasing this value increases computational burden &
         *            vice versa.
         */
        public Parameters(double tempo, double sampleRate, int newSequenceMs, int newSeekWindowMs, int newOverlapMs) {
            mTempo = tempo;
            mSampleRate = sampleRate;
            mOverlapMs = newOverlapMs;
            mSeekWindowMs = newSeekWindowMs;
            mSequenceMs = newSequenceMs;
        }

        public static Parameters speechDefaults(double tempo, double sampleRate){
            int sequenceMs = 40;
            int seekWindowMs = 15;
            int overlapMs = 12;
            return new Parameters(tempo,sampleRate,sequenceMs, seekWindowMs,overlapMs);
        }

        public static Parameters musicDefaults(double tempo, double sampleRate){
            int sequenceMs = 82;
            int seekWindowMs =  28;
            int overlapMs = 12;
            return new Parameters(tempo,sampleRate,sequenceMs, seekWindowMs,overlapMs);
        }

        public static Parameters slowdownDefaults(double tempo, double sampleRate){
            int sequenceMs = 100;
            int seekWindowMs =  35;
            int overlapMs = 20;
            return new Parameters(tempo,sampleRate,sequenceMs, seekWindowMs,overlapMs);
        }

        public static Parameters automaticDefaults(double tempo, double sampleRate){
            double tempoLow = 0.5; // -50% speed
            double tempoHigh = 2.0; // +100% speed

            double sequenceMsLow = 125; //ms
            double sequenceMsHigh = 50; //ms
            double sequenceK = ((sequenceMsHigh - sequenceMsLow) / (tempoHigh - tempoLow));
            double sequenceC = sequenceMsLow - sequenceK * tempoLow;

            double seekLow = 25;// ms
            double seekHigh = 15;// ms
            double seekK =((seekHigh - seekLow) / (tempoHigh-tempoLow));
            double seekC = seekLow - seekK * seekLow;

            int sequenceMs = (int) (sequenceC + sequenceK * tempo + 0.5);
            int seekWindowMs =  (int) (seekC + seekK * tempo + 0.5);
            int overlapMs = 12;
            return new Parameters(tempo,sampleRate,sequenceMs, seekWindowMs,overlapMs);
        }

        public double getOverlapMs() {
            return mOverlapMs;
        }

        public double getSequenceMs() {
            return mSequenceMs;
        }

        public double getSeekWindowMs() {
            return mSeekWindowMs;
        }

        public double getSampleRate() {
            return mSampleRate;
        }

        public double getTempo(){
            return mTempo;
        }
    }
}
