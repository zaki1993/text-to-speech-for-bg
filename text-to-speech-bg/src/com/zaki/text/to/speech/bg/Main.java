package com.zaki.text.to.speech.bg;

import com.zaki.text.to.speech.bg.audio.AudioPlayer;
import com.zaki.text.to.speech.bg.exception.InvalidInputException;
import com.zaki.text.to.speech.bg.exception.InvalidReaderException;
import com.zaki.text.to.speech.bg.parser.Parser;
import com.zaki.text.to.speech.bg.parser.Token;
import com.zaki.text.to.speech.bg.parser.TokenManager;
import com.zaki.text.to.speech.bg.parser.impl.SingleLineParser;
import com.zaki.text.to.speech.bg.reader.Reader;
import com.zaki.text.to.speech.bg.reader.impl.ConsoleReader;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InvalidReaderException, InvalidInputException {
        // Init main classes
        TokenManager.init();

        Reader reader = new ConsoleReader();
        Parser parser = new SingleLineParser();
        AudioPlayer player = new AudioPlayer();
        do {
            String input = reader.readString();
            if (!input.isEmpty()) {
                List<Token> tokens = parser.parse(input.toLowerCase());
                if (!tokens.isEmpty()) {
                    tokens.forEach(player::play);
                }
            }
        } while (true);
    }
}
