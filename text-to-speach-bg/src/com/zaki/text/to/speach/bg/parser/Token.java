package com.zaki.text.to.speach.bg.parser;

import com.zaki.text.to.speach.bg.exception.GeneralException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Token {

    private static Map<String, Token> tokens = new HashMap<>();

    static {
        try (Scanner sc = new Scanner(Token.class.getClassLoader().getResourceAsStream("sounds"))) {
            if (sc != null) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    System.out.println("Load: " + line);
                    String[] parts = line.split("=");
                    String symbol = parts[0];
                    String sound = parts[1];
                    tokens.put(symbol, new Token(symbol, sound));
                }
            } else {
                throw new GeneralException("Could not find sounds mapping file..!");
            }
        }
        if (tokens.isEmpty()) {
            throw new GeneralException("Could not find sounds mapping file..!");
        }
    }

    private final String symbols;

    private final String audioUrl;

    private Token(String symbols, String audioUrl) {
        this.symbols = symbols;
        this.audioUrl = audioUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
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
