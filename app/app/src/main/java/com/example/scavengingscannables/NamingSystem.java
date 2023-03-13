package com.example.scavengingscannables;

import java.util.HashMap;

/**
 * Generates the readable name for scanned qr codes
 */
public class NamingSystem {

    public String generateName(String hash) {

        if (hash.length() < 6) {
            System.err.println("Hash length should be greater than or equal to 6.");

        }

        String firstSixChars = hash.substring(0, 6);
        System.out.println(firstSixChars);

        try {
            HashMap<String, String> firstCharacter = new HashMap<>();
            firstCharacter.put("0", "Edgy");
            firstCharacter.put("1", "Clever");
            firstCharacter.put("2", "Honest");
            firstCharacter.put("3", "Mature");
            firstCharacter.put("4", "Kind");
            firstCharacter.put("5", "Understanding");
            firstCharacter.put("6", "Sweet");
            firstCharacter.put("7", "Trustworthy");
            firstCharacter.put("8", "Classy");
            firstCharacter.put("9", "Wise");
            firstCharacter.put("a", "Popular");
            firstCharacter.put("b", "Friendly");
            firstCharacter.put("c", "Confident");
            firstCharacter.put("d", "Courageous");
            firstCharacter.put("e", "Mischievous");
            firstCharacter.put("f", "Bold");

            HashMap<String, String> secondCharacter = new HashMap<>();
            secondCharacter.put("0", "Blue");
            secondCharacter.put("1", "Red");
            secondCharacter.put("2", "Yellow");
            secondCharacter.put("3", "Green");
            secondCharacter.put("4", "Navy");
            secondCharacter.put("5", "Maroon");
            secondCharacter.put("6", "White");
            secondCharacter.put("7", "Black");
            secondCharacter.put("8", "Brown");
            secondCharacter.put("9", "Pink");
            secondCharacter.put("a", "Beige");
            secondCharacter.put("b", "Silver");
            secondCharacter.put("c", "Gold");
            secondCharacter.put("d", "Orange");
            secondCharacter.put("e", "Gray");
            secondCharacter.put("f", "Purple");

            HashMap<String, String> thirdCharacter = new HashMap<>();
            thirdCharacter.put("0", "American");
            thirdCharacter.put("1", "British");
            thirdCharacter.put("2", "Canadian");
            thirdCharacter.put("3", "Danish");
            thirdCharacter.put("4", "Egyptian");
            thirdCharacter.put("5", "French");
            thirdCharacter.put("6", "German");
            thirdCharacter.put("7", "Hawaiian");
            thirdCharacter.put("8", "Italian");
            thirdCharacter.put("9", "Japanese");
            thirdCharacter.put("a", "Korean");
            thirdCharacter.put("b", "Lebanese");
            thirdCharacter.put("c", "Mexican");
            thirdCharacter.put("d", "Norwegian");
            thirdCharacter.put("e", "Omani");
            thirdCharacter.put("f", "Polish");

            HashMap<String, String> fourthCharacter = new HashMap<String, String>();

            // Adding entries to the hashmap
            fourthCharacter.put("0", "Penguins");
            fourthCharacter.put("1", "Elephants");
            fourthCharacter.put("2", "Giraffes");
            fourthCharacter.put("3", "Lions");
            fourthCharacter.put("4", "Tigers");
            fourthCharacter.put("5", "Kangaroos");
            fourthCharacter.put("6", "Gorillas");
            fourthCharacter.put("7", "Dolphins");
            fourthCharacter.put("8", "Horses");
            fourthCharacter.put("9", "Koalas");
            fourthCharacter.put("a", "Crocodiles");
            fourthCharacter.put("b", "Cheetahs");
            fourthCharacter.put("c", "Flamingos");
            fourthCharacter.put("d", "Rhinos");
            fourthCharacter.put("e", "Pandas");
            fourthCharacter.put("f", "Sharks");


            HashMap<String, String> fifthCharacter = new HashMap<String, String>();

// Adding entries to the hashmap
            fifthCharacter.put("0", "Kicking");
            fifthCharacter.put("1", "Running");
            fifthCharacter.put("2", "Jumping");
            fifthCharacter.put("3", "Swimming");
            fifthCharacter.put("4", "Singing");
            fifthCharacter.put("5", "Dancing");
            fifthCharacter.put("6", "Laughing");
            fifthCharacter.put("7", "Playing");
            fifthCharacter.put("8", "Sleeping");
            fifthCharacter.put("9", "Eating");
            fifthCharacter.put("a", "Reading");
            fifthCharacter.put("b", "Writing");
            fifthCharacter.put("c", "Drawing");
            fifthCharacter.put("d", "Cooking");
            fifthCharacter.put("e", "Traveling");
            fifthCharacter.put("f", "Learning");


            HashMap<String, String> sixthCharacter = new HashMap<String, String>();

            sixthCharacter.put("0", "Volleyball");
            sixthCharacter.put("1", "Soccer");
            sixthCharacter.put("2", "Cricket");
            sixthCharacter.put("3", "Softball");
            sixthCharacter.put("4", "Golf");
            sixthCharacter.put("5", "Archery");
            sixthCharacter.put("6", "Rugby");
            sixthCharacter.put("7", "Baseball");
            sixthCharacter.put("8", "Hockey");
            sixthCharacter.put("9", "Track and Field");
            sixthCharacter.put("a", "Karate");
            sixthCharacter.put("b", "Gymnastics");
            sixthCharacter.put("c", "Basketball");
            sixthCharacter.put("d", "Football");
            sixthCharacter.put("e", "Table Tennis");
            sixthCharacter.put("f", "Lawn Tennis");


            String firstCharValue = firstCharacter.get(Character.toString(firstSixChars.charAt(0)));
            String secondCharValue = secondCharacter.get(Character.toString(firstSixChars.charAt(1)));
            String thirdCharValue = thirdCharacter.get(Character.toString(firstSixChars.charAt(2)));
            String fourthCharValue = fourthCharacter.get(Character.toString(firstSixChars.charAt(3)));
            String fifthCharValue = fifthCharacter.get(Character.toString(firstSixChars.charAt(4)));
            String sixthCharValue = sixthCharacter.get(Character.toString(firstSixChars.charAt(5)));

            String name = firstCharValue + " " + secondCharValue + " " + thirdCharValue + " " + fourthCharValue + " " + fifthCharValue + " " + sixthCharValue;
            return name;
        } catch (Exception e) {
            System.err.println("Error generating name: " + e.getMessage());
            return "";
        }
    }
}
