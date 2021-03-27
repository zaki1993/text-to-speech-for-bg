package com.zaki.text.to.speech.bg.audio;

import resources.ResourceLoader;
import com.zaki.text.to.speech.bg.exception.InvalidAudioException;
import com.zaki.text.to.speech.bg.parser.Token;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class AudioPlayer {

    private final static String AUDIO_FOLDER = "audio" + File.separator + "short";
    public void play(Token t) {
        CountDownLatch syncLatch = new CountDownLatch(0);

        // For now load the audio every time and just play it
        // TODO: load the audio in the memory and play it, it would be faster
        try {
            String url = AUDIO_FOLDER + File.separator + t.getAudioUrl();
            System.out.println("Playing: " + url);
            InputStream i = ResourceLoader.class.getResourceAsStream(url);
            AudioInputStream in = AudioSystem.getAudioInputStream(i);
            Clip clip = AudioSystem.getClip();
            // Listener which allow method return once sound is completed
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    syncLatch.countDown();
                }
            });
            clip.open(in);
            clip.start();

            syncLatch.await();
            Thread.sleep(250);
        } catch (Exception e) {
            throw new InvalidAudioException(e);
        }
    }
}
