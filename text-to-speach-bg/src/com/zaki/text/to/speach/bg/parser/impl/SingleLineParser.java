package com.zaki.text.to.speach.bg.parser.impl;

import com.zaki.text.to.speach.bg.exception.InvalidInputException;
import com.zaki.text.to.speach.bg.parser.Parser;
import com.zaki.text.to.speach.bg.parser.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SingleLineParser implements Parser {
    @Override
    public List<Token> parse(String input) throws InvalidInputException {
        if (input.contains("/n")) {
            throw new InvalidInputException(input);
        }
        List tokens = new ArrayList();
        // For process only single characters
        String[] chars = input.split("");
        Stream.of(chars).forEach(s -> tokens.add(Token.get(s)));

        return tokens;
    }
}
