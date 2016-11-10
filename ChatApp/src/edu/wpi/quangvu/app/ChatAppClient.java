/**
 * 
 */
package edu.wpi.quangvu.app;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetSocketAddress;

import edu.wpi.quangvu.app.client.ClientTask;
import edu.wpi.quangvu.app.manager.TaskManager;
import edu.wpi.quangvu.app.net.transport.TransportTask;
import edu.wpi.quangvu.app.ui.client.ChatAppClientFrame;

/**
 * Main client application
 * 
 * @author quangvu
 *
 */
public class ChatAppClient {

	/**
	 * Size of the application window
	 */
	public static final Dimension APP_BOUNDS = new Dimension(450, 300);
	
	public static ClientTask task;
	
	public static String name = "";
	
	public static void main(String[] args) {
		try {			
			InetSocketAddress address = new InetSocketAddress(args[0], ChatAppServer.SERVER_PORT);
			
			ChatAppClient client = new ChatAppClient(address);
			
			client.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public ChatAppClient(InetSocketAddress address) throws IOException {
		ChatAppClientFrame.getInstance();
		
		task = new ClientTask(address);
		TaskManager.getInstance().register(task);
		
		TaskManager.getInstance().register(TransportTask.getInstance());
	}
	
	public void start() {
		ChatAppClientFrame.getInstance().start();
		TaskManager.getInstance().start();
	}

}
