package com.zaki.text.to.speech.bg.generator;

import com.zaki.text.to.speech.bg.utils.Utils;
import resources.ResourceLoader;

import java.util.List;

public class SpecialWordsGenerator {
    public static void main(String[] args) {
        List<String> vowels = Utils.getFileLines(ResourceLoader.class, "letters/vowels");
        List<String> consonants = Utils.getFileLines(ResourceLoader.class, "letters/consonants");

        for (String consonant : consonants) {
            for (String vowel : vowels) {
                System.out.println(consonant + vowel);
            }
        }
    }
}
