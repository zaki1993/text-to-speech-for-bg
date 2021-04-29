package com.zaki.text.to.speech.bg.parser.impl;

import com.zaki.text.to.speech.bg.exception.InvalidInputException;
import com.zaki.text.to.speech.bg.parser.Parser;
import com.zaki.text.to.speech.bg.parser.Token;
import com.zaki.text.to.speech.bg.parser.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleLineParser implements Parser {
    /**
     * Parses only single lines. In case the input is not a single line, an exception is thrown.
     * Splits the input letter by letter. All unknown or unsupported characters will be ignored
     * and will not be part of the final result.
     *
     * @param input single line string
     * @return list of tokens to play
     * @throws InvalidInputException
     */
    @Override
    public Token parse(String input) throws InvalidInputException {
        if (input.contains("/n")) {
            throw new InvalidInputException(input);
        }
        String filteredInput = input.replaceAll("[^" + TokenManager.getSupportedTokensRegex() + "]+", " ");
        System.out.println("Filtered input: " + filteredInput);

        // For process only single characters
        List<Token> tokens = new ArrayList<>();
        for (String word : filteredInput.split("((?<= )|(?= ))")) {
            if (word.length() == 1) {
                tokens.add(TokenManager.get(word));
            } else {
                tokens.addAll(Stream.of(word.split(TokenManager.getSupportedTokensRegex()))
                        .map(TokenManager::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            }
        }
        // Chain all tokens. This will create linked list of tokens.
        // This is required, because there some specific language rules which must be applied,
        // depending on the type of the letter and its position.
        Token result = null;
        if (tokens.size() > 0) {
            result = tokens.get(0);
            Token lastProcessed = null;
            for (int i = 1; i < tokens.size(); i++) {
                Token current = tokens.get(i);
                if (current != null) {
                    if (lastProcessed != null) {
                        lastProcessed.setNext(current);
                        current.setPrevious(lastProcessed);
                    } else {
                        result.setNext(current);
                        current.setPrevious(result);
                    }
                }

                lastProcessed = current;
            }
        }

        return result;
    }
}
