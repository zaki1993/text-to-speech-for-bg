package com.zaki.text.to.speech.bg.audio;

import com.zaki.text.to.speech.bg.audio.stream.SequenceAudioInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class AudioCombiner {

    private boolean isStart = false;

    public void start() {
        isStart = true;
    }

    public void stop() {
        isStart = false;
    }

    public void clear() {
        sequence = null;
        stop();
    }

    private SequenceAudioInputStream sequence;

    public void addAudioInputStream(AudioInputStream stream, AudioFormat af) {
        if (isStart) {
            if (sequence == null) {
                sequence = new SequenceAudioInputStream(af);
            }
            sequence.addAudioInputStream(stream);
        }
    }

    public void combine() {
        if (isStart) {
            try {
                if (sequence != null) {
                    AudioSystem.write(sequence, AudioFileFormat.Type.WAVE, new File("bullshit.wav"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
