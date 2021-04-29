package com.zaki.text.to.speech.bg.parser;

import com.zaki.text.to.speech.bg.exception.GeneralException;
import com.zaki.text.to.speech.bg.lang.LetterProperty;
import com.zaki.text.to.speech.bg.lang.LetterType;
import com.zaki.text.to.speech.bg.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenManager {
    private TokenManager() {
        Utils.noop();
    }

    private static final String SPECIAL_SYMBOLS = " .,!?-";

    private static Map<String, Token> tokens = new HashMap<>();

    private static boolean isInitialized;

    private static String supportedTokensRegex = "";

    public static void init() {
        try {
            List<String> lines = Utils.getFileLines("resources/sounds");
            for (String line : lines) {
                System.out.println("Loading token: " + line);
                String[] parts = line.split("=", 2);

                // Validate token definition
                if (parts.length == 2) {
                    String symbol = parts[0];
                    String sound = parts[1];

                    // Do not include tokens which sounds are not provided
                    if (!sound.isEmpty()) {
                        tokens.put(symbol, createToken(symbol, parts[1]));
                    }
                }
            }
        } catch (Exception e) {
            throw new GeneralException(e, "There was a problem while loading the sounds configuration");
        }
        if (tokens.isEmpty()) {
            throw new GeneralException("No tokens loaded..!");
        }
        TokenManager.isInitialized = true;

        // Create supported tokens regex. For some special characters we must escape them before adding them to the regex
        supportedTokensRegex = "((?<=" + tokens.keySet().stream()
                .map(s -> SPECIAL_SYMBOLS.contains(s) ? "\\" + s : s)
                .collect(Collectors.joining("|")) + "))";
        System.out.println("Supported tokens: " + supportedTokensRegex);
    }

    /**
     * Creates a token from configuration. The token must have the following syntax. letter=[originalUrl=X, silentUrl=Y, loudUrl=Z]
     * Where X, Y and Z are valid relative paths to .wav files
     *
     * @param symbol the letter of the token
     * @param audios String with the following format [originalUrl=X, silentUrl=Y, loudUrl=Z]
     * @return Token instance
     */
    private static Token createToken(String symbol, String audios) {
        if (!audios.startsWith("[") || !audios.endsWith("]")) {
            throw new GeneralException(String.format("Audios string %s is with invalid format", audios));
        }
        String audiosString = audios.substring(1, audios.indexOf("]"));
        System.out.println("Audios string: " + audiosString);
        String[] audioParts = audiosString.split(",");
        String originalUrl = null;
        String silentUrl = null;
        String loudUrl = null;
        for (String audioPart : audioParts) {
            String[] urlParts = audioPart.split("=");
            if (urlParts.length != 2) {
                throw new GeneralException(String.format("Audio part %s is not valid", audioPart));
            }
            switch (urlParts[0]) {
                case "original":
                    originalUrl = urlParts[1];
                    break;
                case "silent":
                    silentUrl = urlParts[1];
                    break;
                case "loud":
                    loudUrl = urlParts[1];
                    break;
                default:
                    throw new GeneralException(String.format("%s is not expected", urlParts[0]));
            }
        }
        if (originalUrl == null) {
            throw new GeneralException("originalUrl is mandatory");
        }

        String propertiesString = audios.substring(audios.indexOf("]") + 2, audios.length() - 1);
        System.out.println("Properties string: " + propertiesString);
        String[] propertiesPart = propertiesString.split(",");
        LetterType type = null;
        List<LetterProperty> properties = new ArrayList<>();
        for (String property : propertiesPart) {
            if (!property.isEmpty()) {
                switch (property.trim()) {
                    case "vowel":
                        type = LetterType.VOWEL;
                        break;
                    case "consonant":
                        type = LetterType.CONSONANT;
                        break;
                    case "loud":
                        if (type != null && type == LetterType.CONSONANT) {
                            properties.add(LetterProperty.LOUD_CONSONANT);
                        } else {
                            throw new GeneralException("Property loud can be provided only to consonants");
                        }
                        break;
                    case "silent":
                        if (type != null && type == LetterType.CONSONANT) {
                            properties.add(LetterProperty.SILENT_CONSONANT);
                        } else {
                            throw new GeneralException("Property silent can be provided only to consonants");
                        }
                        break;
                    default:
                        throw new GeneralException("Unknown property " + property);
                }
            }
        }

        if (type == null) {
            type = LetterType.NONE;
        }

        return new Token(symbol, originalUrl, silentUrl, loudUrl, type, properties);
    }

    public static Token get(String symbols) {
        if (!TokenManager.isInitialized) {
            init();
        }
        Token result = null;
        if (tokens.containsKey(symbols)) {
            result = tokens.get(symbols);
        }
        // weird bug when the word is empty word (space), but the list of letters in the file did not recognise the space
        if (result == null && tokens.containsKey(symbols.trim())) {
            result = tokens.get(symbols.trim());
        }

        return result != null ? new Token(result) : result;
    }

    public static String getSupportedTokensRegex() {
        if (!TokenManager.isInitialized) {
            init();
        }
        return TokenManager.supportedTokensRegex;
    }
}
