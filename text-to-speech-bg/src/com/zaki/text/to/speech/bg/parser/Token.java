package com.zaki.text.to.speech.bg.parser;

import com.zaki.text.to.speech.bg.lang.LetterProperty;
import com.zaki.text.to.speech.bg.lang.LetterType;

import java.util.List;

public class Token {

    private final String symbols;

    private final String originalAudio;

    private final String silentAudio;

    private final String loudAudio;

    private final LetterType type;

    private final List<LetterProperty> properties;

    public Token(String symbols, String originalAudio, String silentAudio, String loudAudio, LetterType type, List<LetterProperty> properties) {
        this.symbols = symbols;
        this.originalAudio = originalAudio;
        this.silentAudio = silentAudio;
        this.loudAudio = loudAudio;
        this.type = type;
        this.properties = properties;
    }

    public String getLoudAudio() {
        return loudAudio;
    }

    public String getOriginalAudio() {
        return originalAudio;
    }

    public String getSilentAudio() {
        return silentAudio;
    }

    public String getSymbols() {
        return symbols;
    }

    public LetterType getType() {
        return type;
    }

    public List<LetterProperty> getProperties() {
        return properties;
    }
}
