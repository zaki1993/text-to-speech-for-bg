package com.zaki.text.to.speech.bg;

public final class EnvironmentMode {
    private enum Mode {
        DEBUG, GENERATE
    }

    private final static Mode mode = Mode.GENERATE;

    public static boolean debug() {
        return mode == Mode.DEBUG;
    }
}
