package com.example.scavengingscannables;

import java.util.HashMap;

/**
 * Generates the readable name for scanned qr codes
 */
public class NamingSystem {
    /**
     * This method generates the name
     * @param hash
     *      The hash of the QR code
     * @return name
     *      The generated name of the QR code
     */
    public String generateName(String hash) {

        if (hash.length() < 6) {
            System.err.println("Hash length should be greater than or equal to 6.");

        }

        String firstSixChars = hash.substring(0, 6);
        System.out.println(firstSixChars);

        try {
            HashMap<String, String> opinion = new HashMap<>();
            opinion.put("0", "Edgy");
            opinion.put("1", "Clever");
            opinion.put("2", "Honest");
            opinion.put("3", "Mature");
            opinion.put("4", "Kind");
            opinion.put("5", "Understanding");
            opinion.put("6", "Sweet");
            opinion.put("7", "Trustworthy");
            opinion.put("8", "Classy");
            opinion.put("9", "Wise");
            opinion.put("a", "Popular");
            opinion.put("b", "Friendly");
            opinion.put("c", "Confident");
            opinion.put("d", "Courageous");
            opinion.put("e", "Mischievous");
            opinion.put("f", "Bold");

            HashMap<String, String> color = new HashMap<>();
            color.put("0", "Blue");
            color.put("1", "Red");
            color.put("2", "Yellow");
            color.put("3", "Green");
            color.put("4", "Navy");
            color.put("5", "Maroon");
            color.put("6", "White");
            color.put("7", "Black");
            color.put("8", "Brown");
            color.put("9", "Pink");
            color.put("a", "Beige");
            color.put("b", "Silver");
            color.put("c", "Gold");
            color.put("d", "Orange");
            color.put("e", "Gray");
            color.put("f", "Purple");

            HashMap<String, String> size = new HashMap<>();
            size.put("0", "Small");
            size.put("1", "Tiny");
            size.put("2", "Large");
            size.put("3", "Gigantic");
            size.put("4", "Huge");
            size.put("5", "Tall");
            size.put("6", "Short");
            size.put("7", "Big");
            size.put("8", "Petite");
            size.put("9", "Giant");
            size.put("a", "Vast");
            size.put("b", "Massive");
            size.put("c", "Long");
            size.put("d", "Average");
            size.put("e", "Colossal");
            size.put("f", "Cosmic");

            HashMap<String, String> origin = new HashMap<>();
            origin.put("0", "American");
            origin.put("1", "British");
            origin.put("2", "Canadian");
            origin.put("3", "Danish");
            origin.put("4", "Egyptian");
            origin.put("5", "French");
            origin.put("6", "German");
            origin.put("7", "Hawaiian");
            origin.put("8", "Italian");
            origin.put("9", "Japanese");
            origin.put("a", "Korean");
            origin.put("b", "Lebanese");
            origin.put("c", "Mexican");
            origin.put("d", "Norwegian");
            origin.put("e", "Omani");
            origin.put("f", "Polish");

            HashMap<String, String> noun = new HashMap<String, String>();

            // Adding entries to the hashmap
            noun.put("0", "Penguin");
            noun.put("1", "Elephant");
            noun.put("2", "Giraffe");
            noun.put("3", "Lion");
            noun.put("4", "Tiger");
            noun.put("5", "Kangaroo");
            noun.put("6", "Gorilla");
            noun.put("7", "Dolphin");
            noun.put("8", "Horse");
            noun.put("9", "Koala");
            noun.put("a", "Crocodile");
            noun.put("b", "Cheetah");
            noun.put("c", "Flamingo");
            noun.put("d", "Rhino");
            noun.put("e", "Panda");
            noun.put("f", "Shark");


            HashMap<String, String> material = new HashMap<String, String>();

            // Adding entries to the hashmap
            material.put("0", "Plastic");
            material.put("1", "Glass");
            material.put("2", "Silver");
            material.put("3", "Gold");
            material.put("4", "Wood");
            material.put("5", "Polymer");
            material.put("6", "Concrete");
            material.put("7", "Paper");
            material.put("8", "Cotton");
            material.put("9", "Silk");
            material.put("a", "Water");
            material.put("b", "Cardboard");
            material.put("c", "Metal");
            material.put("d", "Leather");
            material.put("e", "Brick");
            material.put("f", "Cement");



            String firstCharValue = opinion.get(Character.toString(firstSixChars.charAt(0)));
            String secondCharValue = size.get(Character.toString(firstSixChars.charAt(1)));
            String thirdCharValue = color.get(Character.toString(firstSixChars.charAt(2)));
            String fourthCharValue = origin.get(Character.toString(firstSixChars.charAt(3)));
            String fifthCharValue = material.get(Character.toString(firstSixChars.charAt(4)));
            String sixthCharValue = noun.get(Character.toString(firstSixChars.charAt(5)));

            String name = firstCharValue + " " + secondCharValue + " " + thirdCharValue + " " + fourthCharValue + " " + fifthCharValue + " " + sixthCharValue;
            return name;
        } catch (Exception e) {
            System.err.println("Error generating name: " + e.getMessage());
            return "";
        }
    }
}
