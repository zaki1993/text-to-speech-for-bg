package com.zaki.text.to.speech.bg.audio.rules.impl;

import com.zaki.text.to.speech.bg.audio.rules.LanguageRule;
import com.zaki.text.to.speech.bg.lang.LetterProperty;
import com.zaki.text.to.speech.bg.lang.LetterType;
import com.zaki.text.to.speech.bg.parser.Token;

public class BulgarianLanguageRule implements LanguageRule {

    /**
     * Rule 1 - If the letter is silent consonant and is before loud consonant then, the current letter must became loud
     * Rule 2 - If the letter is loud consonant and is before silent consonant, the current letter must become silent
     * Rule 3 - If the letter is loud consonant and is at the end of the word, the current letter must become silent
     * Rule 4 - Removing the sound in case we have the following combination of letters <b>&#x441;&#x442;&#x43D;</b> or <b>&#x437;&#x434;&#x43D;</b>
     *
     * @param t token which will be played
     * @return the audio file url which will be played
     */
    @Override
    public String getAudioUrlForToken(Token t) {
        System.out.println("Applying rule for token=" + t);
        // In case the token has no adjacent tokens, just play its original audio
        if (!t.hasNext() && !t.hasPrevious()) {
            return t.getOriginalAudio();
        }
        // If it has previous, but not next(this is covered by the previous if) and the previous is non letter, then play its original audio.
        if (t.hasPrevious() && !t.getPrevious().isLetter()) {
            return t.getOriginalAudio();
        }

        if (t.getType() == LetterType.CONSONANT) {
            // Rule 1
            if (t.getProperties().contains(LetterProperty.SILENT_CONSONANT)) {
                if (t.hasNext() && t.getNext().getProperties().contains(LetterProperty.LOUD_CONSONANT)) {
                    return t.getLoudAudio();
                }
            }
            // Rule 2
            if (t.getProperties().contains(LetterProperty.LOUD_CONSONANT)) {
                if (t.hasNext() && t.getNext().getProperties().contains(LetterProperty.SILENT_CONSONANT)) {
                    return t.getSilentAudio();
                }
                // Rule 3
                if (!t.hasNext()) {
                    return t.getSilentAudio();
                }
                // Rule 4
                if (t.hasPrevious() && t.hasNext()) {
                    String lettersCombination = t.getPrevious().getSymbols() + t.getSymbols() + t.getNext().getSymbols();
                    System.out.println(lettersCombination);

                    return null;
                }
            }
        }

        return t.getOriginalAudio();
    }
}