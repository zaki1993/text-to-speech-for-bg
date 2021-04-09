package com.zaki.text.to.speech.bg.audio.impl;

import com.zaki.text.to.speech.bg.audio.AudioPlayer;
import com.zaki.text.to.speech.bg.audio.rules.LanguageRule;
import com.zaki.text.to.speech.bg.audio.rules.impl.BulgarianLanguageRule;
import com.zaki.text.to.speech.bg.utils.Utils;
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

public class BulgarianAudioPlayer implements AudioPlayer {

    private LanguageRule languageRule;

    public BulgarianAudioPlayer() {
        this.languageRule = new BulgarianLanguageRule();
    }

    private final static String AUDIO_FOLDER = "audio" + File.separator + "short";

    @Override
    public void play(Token t) {
        CountDownLatch syncLatch = new CountDownLatch(1);

        // For now load the audio every time and just play it
        // TODO: load the audio in the memory and play it, it would be faster
        try {
            // TODO apply some rules
            String urlFromLanguageRule = languageRule.getAudioUrlForToken(t);
            if (urlFromLanguageRule != null) {
                String url = AUDIO_FOLDER + File.separator + urlFromLanguageRule;
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
                //Thread.sleep(250);
            }
        } catch (Exception e) {
            Utils.logSilently(e);
        }
    }
}
