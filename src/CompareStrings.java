/**
 * CompareStrings.java
 * 
 * Tiny class containing a function to compare two strings based on their ending 
 * characters.
 */

import java.util.Comparator;

public class CompareStrings implements Comparator<String> {

	/*
	 * Compares two strings based on its ending.
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String str1, String str2) {
       int num1 = Integer.parseInt(str1.substring(4, str1.length()));
       int num2 = Integer.parseInt(str2.substring(4, str2.length()));

       return num1 - num2;

    }

}
