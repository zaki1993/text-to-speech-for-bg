package com.zaki.text.to.speech.bg.parser;

import com.zaki.text.to.speech.bg.exception.GeneralException;
import com.zaki.text.to.speech.bg.utils.Utils;
import resources.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        try (Scanner sc = new Scanner(ResourceLoader.class.getResourceAsStream("sounds"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println("Loading token: " + line);
                String[] parts = line.split("=");

                // Validate token definition
                if (parts.length == 2) {
                    String symbol = parts[0];
                    String sound = parts[1];

                    // Do not include tokens which sounds are not provided
                    if (!sound.isEmpty()) {
                        tokens.put(symbol, new Token(symbol, sound));
                    }
                }
            }
        } catch (Exception e) {
            throw new GeneralException(e, "Could not find sounds mapping file..!");
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

    public static Token get(String symbols) {
        if (!TokenManager.isInitialized) {
            init();
        }
        Token result = null;
        if (tokens.containsKey(symbols)) {
            result = tokens.get(symbols);
        }

        return result;
    }

    public static String getSupportedTokensRegex() {
        if (!TokenManager.isInitialized) {
            init();
        }
        return TokenManager.supportedTokensRegex;
    }
}
