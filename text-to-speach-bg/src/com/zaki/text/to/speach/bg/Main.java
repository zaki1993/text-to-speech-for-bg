package com.zaki.text.to.speach.bg;

import com.zaki.text.to.speach.bg.audio.AudioPlayer;
import com.zaki.text.to.speach.bg.exception.InvalidInputException;
import com.zaki.text.to.speach.bg.exception.InvalidReaderException;
import com.zaki.text.to.speach.bg.parser.Parser;
import com.zaki.text.to.speach.bg.parser.Token;
import com.zaki.text.to.speach.bg.parser.impl.SingleLineParser;
import com.zaki.text.to.speach.bg.reader.Reader;
import com.zaki.text.to.speach.bg.reader.impl.ConsoleReader;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InvalidReaderException, InvalidInputException {
        Reader reader = new ConsoleReader();
        Parser parser = new SingleLineParser();
        AudioPlayer player = new AudioPlayer();
	    do {
	        String input = reader.readString();
	        List<Token> tokens = parser.parse(input);
	        tokens.forEach(player::play);
        } while (true);
    }
}
