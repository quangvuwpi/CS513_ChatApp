/**
 * 
 */
package edu.wpi.quangvu.app;

import java.io.IOException;

import edu.wpi.quangvu.app.manager.TaskManager;
import edu.wpi.quangvu.app.net.transport.TransportTask;
import edu.wpi.quangvu.app.server.Server;
import edu.wpi.quangvu.app.ui.server.ChatAppServerFrame;

/**
 * Main server application
 * 
 * @author quangvu
 *
 */
public class ChatAppServer {

	public static final int SERVER_PORT = 8080;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChatAppServer server = new ChatAppServer(SERVER_PORT);
		
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ChatAppServer(int port) throws IOException {
		ChatAppServerFrame.getInstance();
		
		TaskManager.getInstance().register(new Server(port));
		TaskManager.getInstance().register(TransportTask.getInstance());
	}
	
	public void start() {
		ChatAppServerFrame.getInstance().start();
		TaskManager.getInstance().start();
	}
	
	public void stop() {
		ChatAppServerFrame.getInstance().stop();
		TaskManager.getInstance().stop();
	}
	
	public void shutdown() {
		ChatAppServerFrame.getInstance().teardown();
		TaskManager.getInstance().teardown();
	}

}
