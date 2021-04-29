package com.zaki.text.to.speech.bg.audio.rules.impl;

import com.zaki.text.to.speech.bg.EnvironmentMode;
import com.zaki.text.to.speech.bg.audio.rules.LanguageRule;
import com.zaki.text.to.speech.bg.lang.LetterProperty;
import com.zaki.text.to.speech.bg.lang.LetterType;
import com.zaki.text.to.speech.bg.parser.Token;
import com.zaki.text.to.speech.bg.utils.Pair;
import com.zaki.text.to.speech.bg.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulgarianLanguageRule implements LanguageRule {

    /**
     * Middle letter is the key. Values are adjacent of the key.
     */
    private static Map<String, List> rule4cases = new HashMap<>();

    static {
        try {
            List<String> rule4Lines = Utils.getFileLines("resources" + File.separator + "rules" + File.separator + "rule_4");
            for (String line : rule4Lines) {
                String[] parts = line.split("");
                // Skip invalid rule definitions
                if (parts.length != 3) {
                    continue;
                }
                List casePairs = null;
                if (rule4cases.containsKey(parts[1])) {
                    casePairs = rule4cases.get(parts[1]);
                } else {
                    casePairs = new ArrayList();
                }
                casePairs.add(new Pair<>(parts[0], parts[2]));
                rule4cases.put(parts[1], casePairs);
            }
        } catch (IOException e) {
            Utils.logSilently(e);
        }
    }

    /**
     * Rule 1 - If the letter is silent consonant and is before loud consonant then, the current letter must became loud
     * Rule 2 - If the letter is loud consonant and is before silent consonant, the current letter must become silent
     * Rule 3 - If the letter is loud consonant and is at the end of the word, the current letter must become silent
     * Rule 4 - Removing the sound in case we have the following combination of letters:
     *
     * <p>
     * &#x437;&#x434;&#x43D;
     * &#x441;&#x442;&#x43D;
     * &#x441;&#x442;&#x432;
     * &#x442;&#x441;&#x43A;
     * </p>
     *
     * @param t token which will be played
     * @return the audio file url which will be played
     */
    @Override
    public String getAudioUrlForToken(Token t) {
        if (EnvironmentMode.debug()) {
            System.out.println("Applying rule for token=" + t);
        }
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
                    if (EnvironmentMode.debug()) {
                        System.out.println("Applying rule 1");
                    }
                    return t.getLoudAudio();
                }
            }
            // Rule 2
            if (t.getProperties().contains(LetterProperty.LOUD_CONSONANT)) {
                if (t.hasNext() && t.getNext().getProperties().contains(LetterProperty.SILENT_CONSONANT)) {
                    if (EnvironmentMode.debug()) {
                        System.out.println("Applying rule 2");
                    }
                    return t.getSilentAudio();
                }
                // Rule 3
                if (!t.hasNext()) {
                    if (EnvironmentMode.debug()) {
                        System.out.println("Applying rule 3");
                    }
                    return t.getSilentAudio();
                }
                // Rule 4
                if (t.hasPrevious() && t.hasNext() && rule4cases.containsKey(t.getSymbols())) {
                    List<Pair> rule4casesForLetter = rule4cases.get(t.getSymbols());
                    if (rule4casesForLetter != null) {
                        for (Pair p : rule4casesForLetter) {
                            String leftAdjacentSymbol = t.getPrevious().getSymbols();
                            String rightAdjacentSymbol = t.getNext().getSymbols();
                            if (p.getK().equals(leftAdjacentSymbol) && p.getV().equals(rightAdjacentSymbol)) {
                                if (EnvironmentMode.debug()) {
                                    System.out.println("Applying rule 4 for symbol " + t.getSymbols());
                                }
                                return null;
                            }
                        }
                    }
                }
            }
        }

        return t.getOriginalAudio();
    }
}