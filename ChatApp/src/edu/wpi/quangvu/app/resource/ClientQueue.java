/**
 * 
 */
package edu.wpi.quangvu.app.resource;

import edu.wpi.quangvu.app.utility.IManageableResource;
import edu.wpi.quangvu.app.utility.RoundRobinQueue;

/**
 * Round-robin queue of clients
 * 
 * @author quangvu
 *
 */
public class ClientQueue extends RoundRobinQueue<Client> implements IManageableResource {

	@Override
	public void setup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void teardown() {
		// Disconnect and remove each client
		while (hasNext()) {
			next().disconnect();			
			remove();
		}		
	}

}
