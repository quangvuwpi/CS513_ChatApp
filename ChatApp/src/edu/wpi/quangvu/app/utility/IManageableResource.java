/**
 * 
 */
package edu.wpi.quangvu.app.utility;

/**
 * Manageable resources for easy setup and tear down
 * 
 * @author quangvu
 *
 */
public interface IManageableResource {
	
	/**
	 * Setup the resource
	 */
	public void setup();

	/**
	 * Teardown the resource
	 */
	public void teardown();
}
