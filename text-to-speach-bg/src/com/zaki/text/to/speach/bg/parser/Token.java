package com.zaki.text.to.speach.bg.parser;

import com.zaki.text.to.speach.bg.exception.InvalidInputException;
import com.zaki.text.to.speach.bg.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Token {

    private static Map<String, Token> tokens = new HashMap<>();

    static {
        // TODO load tokens
        tokens.put("a", new Token("a", ""));
        tokens.put("b", new Token("b", ""));
        tokens.put("c", new Token("c", ""));
        tokens.put("d", new Token("d", ""));
        tokens.put("e", new Token("e", ""));
        tokens.put("f", new Token("f", ""));
    }

    private final String symbols;

    private final String audio;

    private Token(String symbols, String audio) {
        this.symbols = symbols;
        this.audio = audio;
    }
    public static Token get(String c) {
        Token result = null;
        if (tokens.containsKey(c)) {
            result = tokens.get(c);
        }
        if (result == null) {
            result = tokens.get("NOT_EXISTING");
        }

        return result;
    }
}
