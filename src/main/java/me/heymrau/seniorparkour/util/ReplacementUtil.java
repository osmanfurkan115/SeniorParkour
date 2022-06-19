package me.heymrau.seniorparkour.util;

import java.util.List;
import java.util.Map;

public final class ReplacementUtil {
    private ReplacementUtil() {
    }

    public static String replaceValues(String textToReplace, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            textToReplace = textToReplace.replace('%' + entry.getKey() + '%', entry.getValue());
        }
        return textToReplace;
    }

    public static List<String> replaceValues(List<String> textToReplace, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            textToReplace.replaceAll(s -> s.replace('%' + entry.getKey() + '%', entry.getValue()));
        }
        return textToReplace;
    }
}
