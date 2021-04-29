package com.zaki.text.to.speech.bg.audio.stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SequenceAudioInputStream extends AudioInputStream {

    private List<AudioInputStream> audioInputStreamsList;
    private int currentAudioInputStream;

    public SequenceAudioInputStream(AudioFormat audioFormat) {
        super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
        this.audioInputStreamsList = new ArrayList<>();
        this.currentAudioInputStream = 0;
    }

    public boolean addAudioInputStream(AudioInputStream audioStream) {
        if (audioStream == null) {
            return false;
        }
        if (getFormat().matches(audioStream.getFormat())) {
            audioInputStreamsList.add(AudioSystem.getAudioInputStream(getFormat(), audioStream));
        }
        return true;
    }

    private AudioInputStream getCurrentStream() {
        return audioInputStreamsList.get(currentAudioInputStream);
    }

    private boolean advanceStream() {
        return (++currentAudioInputStream) < audioInputStreamsList.size();
    }

    public long getFrameLength() {
        long lLengthInFrames = 0;
        Iterator<AudioInputStream> streamIterator = audioInputStreamsList.iterator();
        while (streamIterator.hasNext()) {
            AudioInputStream stream = streamIterator.next();
            long lLength = stream.getFrameLength();
            if (lLength == AudioSystem.NOT_SPECIFIED) {
                return AudioSystem.NOT_SPECIFIED;
            } else {
                lLengthInFrames += lLength;
            }
        }
        return lLengthInFrames;
    }

    public int read()
            throws IOException {
        AudioInputStream stream = getCurrentStream();
        int nByte = stream.read();
        if (nByte == -1) {
            /*
             The end of the current stream has been signaled.
             We try to advance to the next stream.
             */
            boolean bAnotherStreamAvailable = advanceStream();
            if (bAnotherStreamAvailable) {
                /*
                 There is another stream. We recurse into this method
                 to read from it.
                 */
                return read();
            } else {
                /*
                 No more data. We signal EOF.
                 */
                return -1;
            }
        } else {
            /*
             The most common case: We return the byte.
             */
            return nByte;
        }
    }

    public int read(byte[] data, int nOffset, int nLength)
            throws IOException {
        int nBytesRead = readCurrentStream(data, nOffset, nLength);

        if (nBytesRead == -1) {
            return readNextStream(data, nOffset, nLength);
        }

        return nBytesRead;
    }

    private int readCurrentStream(byte[] abData, int nOffset, int nLength) throws IOException {
        AudioInputStream stream = getCurrentStream();
        return stream.read(abData, nOffset, nLength);
    }

    private int readNextStream(byte[] abData, int nOffset, int nLength) throws IOException {
        boolean bAnotherStreamAvailable = advanceStream();
        if (bAnotherStreamAvailable) {
            return read(abData, nOffset, nLength);
        } else {
            return -1;
        }
    }
}
