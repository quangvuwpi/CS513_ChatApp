/**
 * 
 */
package edu.wpi.quangvu.app.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import edu.wpi.quangvu.app.manager.ClientManager;
import edu.wpi.quangvu.app.manager.QueueManager;
import edu.wpi.quangvu.app.net.protocol.ChatAppRequest;
import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.resource.ClientQueue;
import edu.wpi.quangvu.app.session.ClientSession;
import edu.wpi.quangvu.app.ui.client.ChatAppClientFrame;
import edu.wpi.quangvu.app.utility.AbstractRunnableTask;

/**
 * @author quangvu
 *
 */
public class ClientTask extends AbstractRunnableTask {

	private boolean running = true;

	public static Client client;

	private boolean isConnected = false;
	public static InetSocketAddress address;
	private AsynchronousSocketChannel channel;

	private final ClientQueue queue;

	public ClientTask(InetSocketAddress address) throws IOException {
		this.address = address;
		
		queue = QueueManager.getInstance().getClientQueue();
		
		connect();		
	}

	protected void connect() throws IOException {
		channel = AsynchronousSocketChannel.open();
		channel.connect(address, null, new ConnectionHandler());
	}

	@Override
	public void run() {
		while (running) {

			// If connected to server, start servicing
			if (isConnected) {
				if (queue.hasNext()) {
					ClientSession.service(queue.next());
				}
			} else { // Else attempt to connect
				try {
					connect();
				} catch (IOException e) {
					// Something's wrong, try again?
				}
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}

	public void start() {
		running = true;
	}

	public void stop() {
		running = false;
	}

	public synchronized void sendRequest(ChatAppRequest request) {
		client.sendRequest(request);
	}

	private class ConnectionHandler implements CompletionHandler<Void, Void> {

		@Override
		public void completed(Void result, Void attachment) {
			isConnected = true;

			client = new Client(channel);
			ClientManager.getInstance().register(client);
			queue.add(client);
			
			ChatAppClientFrame.getInstance().printSystemMessage(
					"Enter username");
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			isConnected = false;

			ChatAppClientFrame.getInstance().printSystemErrorMessage(
					"Server not available!");
		}

	}

}
