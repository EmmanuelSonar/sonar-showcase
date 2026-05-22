package com.sonarshowcase.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with dead code and poor practices
 * 
 * REL-06: Dead code - unreachable statements
 * MNT-06: Poor naming conventions
 */
public class Utils {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private Utils() {
        // Utility class - prevent instantiation
    }

    // MNT: Poor naming
    /** Public static data field 1 */
    private static String data1;
    /** Public static data field 2 */
    private static String data2;
    /** Temporary variable */
    private static int temp;
    /** Generic object variable */
    private static Object x;

    public static String getData1() { return data1; }
    public static void setData1(String data1) { Utils.data1 = data1; }

    public static String getData2() { return data2; }
    public static void setData2(String data2) { Utils.data2 = data2; }

    public static int getTemp() { return temp; }
    public static void setTemp(int temp) { Utils.temp = temp; }

    public static Object getX() { return x; }
    public static void setX(Object x) { Utils.x = x; }
    
    /**
     * MNT: Poor method implementation
     * 
     * @param input The input string to process
     * @return The processed string in uppercase
     */
    public static String doStuff(String input) {
        if (input == null) {
            return "null input";
        }
        
        String result = input.toUpperCase();
        
        // MNT: Unnecessary variable reassignment
        String temp = result;
        result = temp;
        
        // MNT: Debug print left in code
        System.out.println("Processing: " + result);
        
        return result;
    }
    
    /**
     * MNT: Tautological condition - always true
     * 
     * @param a First number
     * @param b Second number
     * @return Sum of a and b
     */
    public static int calculate(int a, int b) {
        int result = a + b;
        
        // MNT: Condition is always true - SonarQube will flag this
        boolean alwaysTrue = true;
        if (alwaysTrue) {
            return result;
        }
        
        // MNT: This code is effectively dead but compiles
        result = a * b;
        return result;
    }
    
    /**
     * MNT: Poor variable naming
     * 
     * @param s Input string
     * @param n Number of iterations
     * @param f Flag to control processing
     * @return Processed string
     */
    public static String processData(String s, int n, boolean f) {
        String temp1 = s;
        String temp2 = "";
        int i = 0;
        
        // MNT: Single letter variables, unclear logic
        for (int j = 0; j < n; j++) {
            if (f) {
                temp2 += temp1.charAt(i % temp1.length());
                i++;
            } else {
                temp2 += temp1.charAt(n - j - 1);
            }
        }
        
        return temp2;
    }
    
    /**
     * MNT: Confusing method that does nothing useful
     * 
     * @param obj Object to process
     */
    public static void doSomething(Object obj) {
        if (obj != null) {
            Object temp = obj;
            obj = null;
            temp = temp.toString();
            System.out.println(temp);
        }
    }
    
    /**
     * MNT: Method with misleading name
     * 
     * @return Empty list of users
     */
    public static List<String> getUsers() {
        // MNT: Returns empty list, name suggests it gets users
        return new ArrayList<>();
    }
    
    /**
     * MNT: Verbose loop implementation
     * 
     * @param items List of items to process
     */
    public static void processItems(List<String> items) {
        // MNT: Could use forEach or streams
        int i = 0;
        while (i < items.size()) {
            String item = items.get(i);
            System.out.println(item); // MNT: Debug output
            i++; // MNT: Manual increment, error-prone
        }
    }
    
    /**
     * MNT: Overly complex for simple task
     * 
     * @param number Number to check
     * @return true if number is positive, false otherwise
     */
    public static boolean isPositive(int number) {
        if (number > 0) {
            if (number != 0) {
                if (number >= 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * MNT: Duplicate code
     * 
     * @param first First name
     * @param last Last name
     * @return Formatted full name
     */
    public static String formatName1(String first, String last) {
        if (first == null) first = "";
        if (last == null) last = "";
        return first.trim() + " " + last.trim();
    }
    
    /**
     * MNT: Duplicate of above
     * 
     * @param first First name
     * @param last Last name
     * @return Formatted full name
     */
    public static String formatName2(String first, String last) {
        if (first == null) first = "";
        if (last == null) last = "";
        return first.trim() + " " + last.trim();
    }
    
    /**
     * MNT: Another duplicate
     * 
     * @param firstName First name
     * @param lastName Last name
     * @return Formatted full name
     */
    public static String formatFullName(String firstName, String lastName) {
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
        return firstName.trim() + " " + lastName.trim();
    }
    
    // MNT: Unused private method
    private static void helperMethod() {
        System.out.println("Never called");
    }
    
    // MNT: Another unused method
    private static int anotherHelper(int x) {
        return x * 2;
    }
    
    // ==================== CODE DUPLICATION (Intentional Maintainability Issue) ====================
    
    /**
     * MNT: Duplicated method - same logic as formatName1, formatName2, formatFullName
     * 
     * @param first First name
     * @param last Last name
     * @return Formatted full name
     */
    public static String combineNames(String first, String last) {
        if (first == null) first = "";
        if (last == null) last = "";
        return first.trim() + " " + last.trim();
    }
    
    /**
     * MNT: Another duplicated name formatting method
     * 
     * @param firstName First name
     * @param lastName Last name
     * @return Formatted full name
     */
    public static String createFullName(String firstName, String lastName) {
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
        return firstName.trim() + " " + lastName.trim();
    }
    
    /**
     * MNT: Duplicated method - same as isPositive but with different name
     * 
     * @param number Number to check
     * @return true if number is positive, false otherwise
     */
    public static boolean checkIfPositive(int number) {
        if (number > 0) {
            if (number != 0) {
                if (number >= 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * MNT: Another duplicated positive check
     * 
     * @param value Number to check
     * @return true if number is positive, false otherwise
     */
    public static boolean isValuePositive(int value) {
        return value > 0;
    }
    
    /**
     * MNT: Duplicated method - same as calculate but with different name
     * 
     * @param a First number
     * @param b Second number
     * @return Sum of a and b
     */
    public static int addNumbers(int a, int b) {
        int result = a + b;
        
        // MNT: Condition is always true - SonarQube will flag this (duplicated)
        boolean alwaysTrue = true;
        if (alwaysTrue) {
            return result;
        }
        
        // MNT: This code is effectively dead but compiles (duplicated)
        result = a * b;
        return result;
    }
    
    /**
     * MNT: Another duplicated calculation method
     * 
     * @param x First number
     * @param y Second number
     * @return Sum of x and y
     */
    public static int sum(int x, int y) {
        int result = x + y;
        
        boolean alwaysTrue = true;
        if (alwaysTrue) {
            return result;
        }
        
        result = x * y;
        return result;
    }
    
    /**
     * MNT: Duplicated method - same as doStuff but with different name
     * 
     * @param input The input string to process
     * @return The processed string in uppercase
     */
    public static String processString(String input) {
        if (input == null) {
            return "null input";
        }
        
        String result = input.toUpperCase();
        
        // MNT: Unnecessary variable reassignment (duplicated)
        String temp = result;
        result = temp;
        
        // MNT: Debug print left in code (duplicated)
        System.out.println("Processing: " + result);
        
        return result;
    }
    
    /**
     * MNT: Another duplicated string processing method
     * 
     * @param text The input string to process
     * @return The processed string in uppercase
     */
    public static String transformText(String text) {
        if (text == null) {
            return "null input";
        }
        
        String result = text.toUpperCase();
        
        String temp = result;
        result = temp;
        
        System.out.println("Processing: " + result);
        
        return result;
    }
}

