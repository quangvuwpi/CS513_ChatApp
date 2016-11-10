/**
 * 
 */
package edu.wpi.quangvu.app.manager;

import java.util.ArrayList;

import edu.wpi.quangvu.app.utility.IManageableResource;

/**
 * Provides easy access to various resources for ChatApp
 * 
 * @author quangvu
 *
 */
public class ResourceManager implements IManageableResource {

	private static ResourceManager instance = null;
	
	private ArrayList<IManageableResource> list = new ArrayList<IManageableResource>();

	protected ResourceManager() {}

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
	
	public void register(IManageableResource r) {
		list.add(r);
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void teardown() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).teardown();
		}
		list.clear();	
	} 

}
