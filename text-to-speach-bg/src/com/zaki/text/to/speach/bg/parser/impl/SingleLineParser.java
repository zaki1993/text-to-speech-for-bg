package com.zaki.text.to.speach.bg.parser.impl;

import com.zaki.text.to.speach.bg.exception.InvalidInputException;
import com.zaki.text.to.speach.bg.parser.Parser;
import com.zaki.text.to.speach.bg.parser.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleLineParser implements Parser {
    @Override
    public List<Token> parse(String input) throws InvalidInputException {
        if (input.contains("/n")) {
            throw new InvalidInputException(input);
        }
        // For process only single characters
        String[] chars = input.split("");
        return Stream.of(chars)
                     .map(Token::get)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }
}
