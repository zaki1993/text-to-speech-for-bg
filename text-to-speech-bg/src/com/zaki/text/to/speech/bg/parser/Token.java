package com.zaki.text.to.speech.bg.parser;

public class Token {

    private final String symbols;

    private final String audioUrl;

    public Token(String symbols, String audioUrl) {
        this.symbols = symbols;
        this.audioUrl = audioUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getSymbols() {
        return symbols;
    }
}
