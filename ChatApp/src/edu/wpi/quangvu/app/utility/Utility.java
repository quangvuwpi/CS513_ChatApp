/**
 * 
 */
package edu.wpi.quangvu.app.utility;

/**
 * Various convenient buffer transformation
 * 
 * @author quangvu
 *
 */
public class Utility {
	
	public static char[] toCharArray(byte[] b) {
		char[] c = new char[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) b[i];
		}
		return c;
	}
	
	public static String toString(byte[] b) {
		return String.copyValueOf(toCharArray(b));
	}

}
