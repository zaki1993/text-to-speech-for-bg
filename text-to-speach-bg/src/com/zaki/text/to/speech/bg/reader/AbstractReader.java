package com.zaki.text.to.speech.bg.reader;

import com.zaki.text.to.speech.bg.exception.InvalidReaderException;

import java.io.InputStream;

public abstract class AbstractReader implements Reader {
    private final InputStream inputStream;

    public AbstractReader(InputStream inputStream) throws InvalidReaderException {
        this.inputStream = inputStream;
        if (this.inputStream == null) {
            throw new InvalidReaderException();
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    protected abstract void init();
}
