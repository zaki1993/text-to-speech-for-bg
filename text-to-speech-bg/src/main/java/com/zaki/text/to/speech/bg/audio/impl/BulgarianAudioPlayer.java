package com.zaki.text.to.speech.bg.audio.impl;

import com.zaki.text.to.speech.bg.audio.AudioPlayer;
import com.zaki.text.to.speech.bg.audio.rules.LanguageRule;
import com.zaki.text.to.speech.bg.audio.rules.impl.BulgarianLanguageRule;
import com.zaki.text.to.speech.bg.parser.Token;
import com.zaki.text.to.speech.bg.utils.Utils;
import resources.ResourceLoader;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
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

                File file = new File(ResourceLoader.class.getResource(url).toURI());
                AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
                AudioFormat format = fileFormat.getFormat();
                try (AudioInputStream in = AudioSystem.getAudioInputStream(file)) {

                    Clip clip = AudioSystem.getClip();
                    // Listener which allow method return once sound is completed
                    clip.addLineListener(e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            syncLatch.countDown();
                        }
                    });

                    AudioInputStream cutted = cutStreamForToken(t, format, file, in);

                    clip.open(cutted);
                    clip.start();

                    syncLatch.await();
                    //Utils.close(cutted);
                }
                //Thread.sleep(250);
            }
        } catch (Exception e) {
            Utils.logSilently(e);
        }
    }

    private AudioInputStream cutStreamForToken(Token t, AudioFormat format, File file, AudioInputStream originalAudioInputStream) throws IOException {

        // If there are 2 or more equally letters then follow these rules:
        // First letter played fist 2/3 of it
        // Middle letter played 1/3 in the middle of it
        // Last letter played last 2/3 of it
        boolean isFirst = false;
        boolean isMiddle = false;
        boolean isLast = false;
        if (t.hasNext() && t.equals(t.getNext())) {
            if (t.hasPrevious() && t.equals(t.getPrevious())) {
                isMiddle = true;
            } else {
                isFirst = true;
            }
        } else if (t.hasPrevious() && t.equals(t.getPrevious())) {
            isLast = true;
        } else {
            return originalAudioInputStream;
        }

        float startFrame;
        float framesToCut;
        if (isFirst) {
            startFrame = 0f;
            framesToCut = Utils.getDurationInSeconds(format, file) * 2 / 3;
        } else if (isMiddle) {
            startFrame = 0f;//1 / 3f;
            framesToCut = Utils.getDurationInSeconds(format, file) * 1 / 3;
        } else {
            startFrame = 0f;//1 / 3f;
            framesToCut = Utils.getDurationInSeconds(format, file);
        }

        return Utils.cutStream(format, originalAudioInputStream, startFrame, framesToCut);
    }
}
