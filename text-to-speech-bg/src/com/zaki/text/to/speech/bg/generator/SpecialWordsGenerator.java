package com.zaki.text.to.speech.bg.generator;

import com.zaki.text.to.speech.bg.utils.Utils;

import java.io.IOException;
import java.util.List;

public class SpecialWordsGenerator {
    public static void main(String[] args) throws IOException {
        List<String> vowels = Utils.getFileLines("letters/vowels");
        List<String> consonants = Utils.getFileLines("letters/consonants");

        for (String consonant : consonants) {
            for (String vowel : vowels) {
                System.out.println(consonant + vowel);
            }
        }
    }
}
