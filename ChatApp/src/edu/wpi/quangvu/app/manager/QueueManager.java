/**
 * 
 */
package edu.wpi.quangvu.app.manager;

import edu.wpi.quangvu.app.resource.ClientQueue;
import edu.wpi.quangvu.app.utility.AbstractManageableResource;

/**
 * Manager of request queues
 * 
 * @author quangvu
 *
 */
public class QueueManager extends AbstractManageableResource {

	private static QueueManager instance = null;

	private ClientQueue clientQueue = new ClientQueue();
	
	protected QueueManager() {
	}

	public static QueueManager getInstance() {
		if (instance == null) {
			instance = new QueueManager();
		}
		return instance;
	}
	
	public ClientQueue getClientQueue() {
		return clientQueue;
	}

}
