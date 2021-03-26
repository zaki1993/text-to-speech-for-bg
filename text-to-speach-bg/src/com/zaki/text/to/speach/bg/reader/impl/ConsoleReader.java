package com.zaki.text.to.speach.bg.reader.impl;

import com.zaki.text.to.speach.bg.exception.InvalidReaderException;
import com.zaki.text.to.speach.bg.reader.AbstractReader;
import com.zaki.text.to.speach.bg.utils.Utils;

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
