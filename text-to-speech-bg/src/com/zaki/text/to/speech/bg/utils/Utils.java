package com.zaki.text.to.speech.bg.utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Utils {

    private Utils() {
        noop();
    }

    public static void noop() {
        // nothing to do
    }

    public static List<String> getFileLines(String fileName) throws IOException {
        List result = new ArrayList<>();
        try (Scanner sc = new Scanner(new FileInputStream(new File(fileName)))) {
            while (sc.hasNextLine()) {
                result.add(sc.nextLine().trim());
            }
        }
        return result;
    }

    public static void logSilently(Throwable t) {
        System.out.println(t.getMessage());
        t.printStackTrace(System.out);
    }

    public static void logAndThrow(Throwable t) throws Throwable {
        logSilently(t);
        throw t;
    }

    public static AudioInputStream cutStream(AudioFormat format, AudioInputStream original, float startSecond, float secondsToCopy) throws IOException {
        int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
        long bytesSkipped = original.skip((int) (startSecond * bytesPerSecond));
        System.out.println("Skipping " + bytesSkipped + " bytes.");
        long framesOfAudioToCopy = (int) (secondsToCopy * (int) format.getFrameRate());
        return new AudioInputStream(original, format, framesOfAudioToCopy);
    }

    public static float getDurationInSeconds(AudioFormat format, File f) {
        long audioFileLength = f.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        return (audioFileLength / (frameSize * frameRate));
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                logSilently(e);
            }
        }
    }

    public static List<AudioInputStream> cloneAudioInputStream(AudioInputStream original, AudioFormat f, int copies) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = original.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        List l = new ArrayList<>();
        for (int i = 0; i < copies; i++) {
            l.add(new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()), f, baos.size()));
        }
        return l;
    }
}
