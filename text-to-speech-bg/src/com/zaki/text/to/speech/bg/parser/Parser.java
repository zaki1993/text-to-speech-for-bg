package com.zaki.text.to.speech.bg.parser;

import com.zaki.text.to.speech.bg.exception.InvalidInputException;

public interface Parser {
    Token parse(String input) throws InvalidInputException;
}
