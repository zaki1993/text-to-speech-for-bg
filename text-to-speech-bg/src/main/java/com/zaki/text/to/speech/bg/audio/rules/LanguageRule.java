package com.zaki.text.to.speech.bg.audio.rules;

import com.zaki.text.to.speech.bg.parser.Token;

public interface LanguageRule {
    /**
     * Returns the proper pronunciation for the token, depending on its position and type.
     *
     * @param t token which will be played
     * @return the audio file url which will be played
     */
    String getAudioUrlForToken(Token t);
}
