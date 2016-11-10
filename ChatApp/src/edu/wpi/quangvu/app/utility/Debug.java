/**
 * 
 */
package edu.wpi.quangvu.app.utility;

/**
 * Debug functionality
 * 
 * @author quangvu
 *
 */
public final class Debug {

	public static boolean debug = true;
	
	public static void println(String s) {
		if (debug) {
			System.out.println(s);
		}
	}
}
