package com.zaki.text.to.speech.bg.reader.impl;

import com.zaki.text.to.speech.bg.exception.InvalidReaderException;
import com.zaki.text.to.speech.bg.reader.AbstractReader;
import com.zaki.text.to.speech.bg.utils.Utils;

import java.util.Scanner;

public class ConsoleReader extends AbstractReader {

    public ConsoleReader() throws InvalidReaderException {
        super(System.in);
    }

    @Override
    public String readString() {
        Scanner sc = new Scanner(getInputStream());
        return sc.nextLine().trim();
    }

    protected void init() {
        Utils.noop();
    }
}
