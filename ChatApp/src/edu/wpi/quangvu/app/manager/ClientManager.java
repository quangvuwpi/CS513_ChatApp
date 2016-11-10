/**
 * 
 */
package edu.wpi.quangvu.app.manager;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.utility.IManageableResource;

/**
 * Manages the list of current users
 * 
 * @author quangvu
 *
 */
public class ClientManager implements IManageableResource {

	private static ClientManager instance = null;

	private HashMap<String, Client> list = new HashMap<String, Client>();

	protected ClientManager() {
	}

	public static ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	/**
	 * @param client
	 *            new client to register
	 * @return TRUE if client was registered, FALSE if name is taken
	 */
	public boolean register(Client client) {
		if (!list.containsValue(client)) {
			String name = client.getName();

			if (!list.containsKey(name)) {
				list.put(name, client);
				
				return true;
			}
		}
		return false;
	}

	/**
	 * @param client
	 *            the client to unregister
	 * @return TRUE if client was unregistered, FALSE if not
	 */
	public boolean unregister(Client client) {
		return list.remove(client.getName(), client);
	}

	/**
	 * @param name
	 *            name of the client
	 * @return the client
	 */
	public Client getClient(String name) {
		if (list.containsKey(name)) {
			return list.get(name);
		}
		return null;
	}

	public ArrayList<String> getAllName() {
		return new ArrayList<String>(list.keySet());
	}

	public ArrayList<Client> getAllClients() {
		return new ArrayList<Client>(list.values());
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void teardown() {
		list.clear();
	}

}
