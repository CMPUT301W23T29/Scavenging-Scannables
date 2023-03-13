package com.example.scavengingscannables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates scores for the scanned qr codes
 */
public class ScoringSystem {
    /**
     * This method generates the score
     * @param hash
     *      The hash that the system uses to generate a score
     * @return score
     *      The score assigned to the hash
     */
    public int generateScore(String hash) {
        int score = 0;

        // Set up bases HashMap
        HashMap<String, Integer> bases = new HashMap<>();
        bases.put("0", 20);
        bases.put("1", 1);
        bases.put("2", 2);
        bases.put("3", 3);
        bases.put("4", 4);
        bases.put("5", 5);
        bases.put("6", 6);
        bases.put("7", 7);
        bases.put("8", 8);
        bases.put("9", 9);
        bases.put("a", 10);
        bases.put("b", 11);
        bases.put("c", 12);
        bases.put("d", 13);
        bases.put("e", 14);
        bases.put("f", 15);

        // Count the number of single zeros in the hash
        Pattern zeroPattern = Pattern.compile("(?<!0)0(?!0)");
        Matcher zeroMatcher = zeroPattern.matcher(hash);
        int zeroCount = 0;
        while (zeroMatcher.find()) {
            zeroCount++;
        }
        score += zeroCount;

        // Find all the repeated characters and store them
        Pattern pattern = Pattern.compile("([0-9a-f])(\\1+)");
        Matcher matcher = pattern.matcher(hash);
        ArrayList<String> repeats = new ArrayList<>();

        while (matcher.find()) {
            repeats.add(matcher.group(0));
        }

        for (int i = 0; i < repeats.size(); i++) {
            int base = bases.get(String.valueOf(repeats.get(i).charAt(0)));
            score += Math.pow(base, repeats.get(i).length() - 1);
        }

        return score;
    }




}
