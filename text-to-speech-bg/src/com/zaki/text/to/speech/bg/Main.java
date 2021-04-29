package com.zaki.text.to.speech.bg;

import com.zaki.text.to.speech.bg.audio.impl.BulgarianAudioPlayer;
import com.zaki.text.to.speech.bg.exception.InvalidInputException;
import com.zaki.text.to.speech.bg.exception.InvalidReaderException;
import com.zaki.text.to.speech.bg.parser.Parser;
import com.zaki.text.to.speech.bg.parser.Token;
import com.zaki.text.to.speech.bg.parser.TokenManager;
import com.zaki.text.to.speech.bg.parser.impl.SingleLineParser;
import com.zaki.text.to.speech.bg.reader.Reader;
import com.zaki.text.to.speech.bg.reader.impl.ConsoleReader;

public class Main {
    public static void main(String[] args) throws InvalidReaderException, InvalidInputException {
        // Init main classes
        TokenManager.init();

        Reader reader = new ConsoleReader();
        Parser parser = new SingleLineParser();
        BulgarianAudioPlayer player = new BulgarianAudioPlayer();
        do {
            //player.getCombiner().start();

            String input = reader.readString();
            if (!input.isEmpty()) {
                Token token = parser.parse(input.toLowerCase());
                while (token != null) {
                    player.play(token);
                    token = token.getNext();
                }
            }

            //player.getCombiner().combine();
            //player.getCombiner().clear();
        } while (true);
    }
}
