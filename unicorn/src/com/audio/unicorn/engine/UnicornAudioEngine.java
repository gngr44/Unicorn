package com.audio.unicorn.engine;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.audio.unicorn.engine.exception.InvalidMediaTypeException;
import com.audio.unicorn.processor.UnicornAudioProcessor;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jsaund on 5/30/13.
 */
public class UnicornAudioEngine {
    private static final String TAG = "UnicornAudioEngine";

    private static long TIMEOUT_MS = 5000;

    private List<UnicornAudioProcessor> mAudioProcessors;
    private File mFile;

    private AudioTrack mTrack;

    private MediaExtractor mMediaExtractor;
    private MediaCodec mMediaCodec;

    private boolean mIsPlaying = false;

    private Thread mAudioProcessorThread;

    public UnicornAudioEngine(String filename) {
        if (filename == null) throw new IllegalArgumentException("Filename cannot be null. Must be a valid file " +
                "using an absolute path");

        File file = new File(filename);

        if (!file.exists()) throw new IllegalArgumentException("Invalid file name or file path. File does not exist");

        mFile = file;

        mAudioProcessors = new CopyOnWriteArrayList<UnicornAudioProcessor>();
    }

    public void addProcessor(UnicornAudioProcessor processor) {
        mAudioProcessors.add(processor);
    }
    
    public synchronized boolean isPlaying() {
        return mIsPlaying;
    }

    private synchronized void setPlaying(boolean playing) {
        mIsPlaying = playing;
    }

    public byte[] start() throws InvalidMediaTypeException {
        if (isPlaying()) {
            stop();
        }

        setPlaying(true);

        initDecoder();

        mAudioProcessorThread = new Thread(new AudioDecoder());
        mAudioProcessorThread.start();

        return null;
    }

    public void stop() {
        if (isPlaying()) {
            mAudioProcessorThread.interrupt();

            try {
                mAudioProcessorThread.join();
            } catch (InterruptedException e) {
                Log.w(TAG, "stop: interrupted", e);
            }
        }
    }

    public void destroy() {
        mAudioProcessors.clear();
        mAudioProcessors = null;
    }

    public void setSamplingRate(float samplingRateScale) {
        if (mTrack != null && mTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mTrack.setPlaybackRate((int) ((samplingRateScale + 0.5f) * mTrack.getSampleRate()));
        }
    }

    private void initDecoder() throws InvalidMediaTypeException {
        mMediaExtractor = new MediaExtractor();
        mMediaExtractor.setDataSource(mFile.getPath());

        int trackCount = mMediaExtractor.getTrackCount();

        if (trackCount == 0) throw new InvalidMediaTypeException("No track found.");

        MediaFormat audioFormat = mMediaExtractor.getTrackFormat(0);
        String mime = audioFormat.getString(MediaFormat.KEY_MIME);

        mTrack = new AudioTrack(
                AudioTrack.MODE_STREAM,
                audioFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE),
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                1024,
                AudioTrack.MODE_STREAM);

        mMediaCodec = MediaCodec.createDecoderByType(mime);
        mMediaCodec.configure(audioFormat, null, null, 0);
    }

    private void readChunk(ByteBuffer[] buffer) {
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_MS);

        if (inputBufferIndex >= 0) {
            ByteBuffer pcmBuffer = buffer[inputBufferIndex];

            int sampleSize = mMediaExtractor.readSampleData(pcmBuffer, 0);

            long presentationTimeUs = -1;

            if (sampleSize > 0) {
                presentationTimeUs = mMediaExtractor.getSampleTime();
            }

            mMediaCodec.queueInputBuffer(inputBufferIndex, 0, sampleSize, presentationTimeUs, sampleSize >= 0 ? 0 : MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            mMediaExtractor.advance();
        }
    }

    private byte[] writeChunk(ByteBuffer[] buffer, MediaCodec.BufferInfo bufferInfo) {
        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_MS);

        switch(outputBufferIndex) {
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                return new byte[1];
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return null;
            default:
                ByteBuffer output = buffer[outputBufferIndex];

                byte[] chunk = new byte[bufferInfo.size];
                output.get(chunk);
                output.clear();

                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);

                return chunk;
        }
    }

    private byte[] processAudio(byte[] chunk) {
        Iterator<UnicornAudioProcessor> iterator = mAudioProcessors.iterator();

        byte[] result = chunk;

        while (iterator.hasNext()) {
            result = iterator.next().processAudio(result);
        }

        return result;
    }

    private class AudioDecoder implements Runnable {
        @Override
        public void run() {
            try {
                mMediaCodec.start();

                final ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
                final ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();

                mMediaExtractor.selectTrack(0);

                final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

                mTrack.play();

                while (!Thread.interrupted()) {
                    readChunk(inputBuffers);

                    byte[] chunk = writeChunk(outputBuffers, bufferInfo);

                    if (chunk == null) break;

                    byte[] processedChunk = processAudio(chunk);

                    mTrack.write(processedChunk, 0, processedChunk.length);
                }
            } finally {
                mMediaCodec.stop();
                mMediaCodec.release();
                mMediaExtractor.release();

                mTrack.stop();
                mTrack.release();

                mMediaCodec = null;
                mMediaExtractor = null;

                setPlaying(false);
            }
        }
    }
}
