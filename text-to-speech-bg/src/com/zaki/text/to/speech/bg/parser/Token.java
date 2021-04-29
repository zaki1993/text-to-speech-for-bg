package com.zaki.text.to.speech.bg.parser;

import com.zaki.text.to.speech.bg.lang.LetterProperty;
import com.zaki.text.to.speech.bg.lang.LetterType;

import java.util.List;
import java.util.Objects;

public class Token {

    private Token previous;

    private Token next;

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

    public Token(final Token t) {
        this.symbols = t.symbols;
        this.originalAudio = t.originalAudio;
        this.silentAudio = t.silentAudio;
        this.loudAudio = t.loudAudio;
        this.type = t.type;
        this.properties = t.properties;
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

    public void setNext(Token next) {
        this.next = next;
    }

    public void setPrevious(Token previous) {
        this.previous = previous;
    }

    public Token getNext() {
        return next;
    }

    public Token getPrevious() {
        return previous;
    }

    @Override
    public String toString() {
        return symbols;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return symbols.equals(token.symbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbols);
    }

    public boolean isLetter() {
        return type != LetterType.NONE;
    }
}
