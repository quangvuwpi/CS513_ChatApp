/**
 * 
 */
package edu.wpi.quangvu.app.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousServerSocketChannel;

import edu.wpi.quangvu.app.manager.QueueManager;
import edu.wpi.quangvu.app.resource.ClientQueue;
import edu.wpi.quangvu.app.session.ServerSession;
import edu.wpi.quangvu.app.ui.server.ChatAppServerFrame;
import edu.wpi.quangvu.app.utility.AbstractRunnableTask;

/**
 * ChatApp server class
 * 
 * @author quangvu
 *
 */
public class Server extends AbstractRunnableTask {
	
	/** Server channel **/
	protected final AsynchronousServerSocketChannel channel;

	/** Server listening mode **/
	private boolean listening = true;
	
	/** Queue of client to be serviced **/
	protected final ClientQueue queue;	
	
	public Server(int port) throws IOException {
		InetSocketAddress address = new InetSocketAddress(port);
		
		channel = AsynchronousServerSocketChannel.open();
		channel.bind(address);
		
		queue = QueueManager.getInstance().getClientQueue();
	}
	
	@Override
	public void run() {
		println("Starting the server...");
		
		/** Client count **/
		int num = 0;
		
		while (listening) {
			try {				
				channel.accept("Anonymous" + num, new ConnectionHandler(queue));
				num++;
			} catch (AcceptPendingException e) {
				// Already pending, just skip
			}
			
			// Service the next client
			if (queue.hasNext()) {				
				ServerSession.service(queue.next());
			}
			
			// Allow the thread to be stopped
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				listening = false;
			}
		}
		
		println("Stopping the server...");
	}
	
	protected void println(String s) {
		ChatAppServerFrame.getInstance().println(s);
	}
	
	@Override
	public void setup() {
		listening = true;
	}

	@Override
	public void teardown() {	
		listening = false;
		try {
			queue.teardown();			
			channel.close();
		} catch (IOException e) {
			// Some error occured
			e.printStackTrace();
		}		
	}

}
