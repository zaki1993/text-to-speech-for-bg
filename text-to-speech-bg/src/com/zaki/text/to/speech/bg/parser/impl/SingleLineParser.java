package com.zaki.text.to.speech.bg.parser.impl;

import com.zaki.text.to.speech.bg.exception.InvalidInputException;
import com.zaki.text.to.speech.bg.parser.Parser;
import com.zaki.text.to.speech.bg.parser.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleLineParser implements Parser {
    /**
     * Parses only single lines. In case the input is not a single line, an exception is thrown.
     * Splits the input letter by letter.
     * @param input single line string
     * @return list of tokens to play
     * @throws InvalidInputException
     */
    @Override
    public List<Token> parse(String input) throws InvalidInputException {
        if (input.contains("/n")) {
            throw new InvalidInputException(input);
        }
        // For process only single characters
        return Stream.of(input.split(""))
                     .map(Token::get)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }
}
