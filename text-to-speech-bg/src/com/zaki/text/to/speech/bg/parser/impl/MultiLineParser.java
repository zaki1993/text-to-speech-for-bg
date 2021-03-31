package com.zaki.text.to.speech.bg.parser.impl;

import com.zaki.text.to.speech.bg.exception.InvalidInputException;
import com.zaki.text.to.speech.bg.parser.Token;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiLineParser extends SingleLineParser {
    /**
     * Removes all lines and puts .(dot) at the end of each line.
     *
     * @param input multiline string
     * @return list of tokens to paly
     * @throws InvalidInputException
     */
    @Override
    public Token parse(String input) throws InvalidInputException {
        return super.parse(
                Stream.of(input.split("\n"))
                        .collect(Collectors.joining("."))
        );
    }
}
