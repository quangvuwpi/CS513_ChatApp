/**
 * 
 */
package edu.wpi.quangvu.app.net.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;

import edu.wpi.quangvu.app.net.protocol.ChatAppProtocol;
import edu.wpi.quangvu.app.net.protocol.ChatAppRequest;
import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.session.ChatAppSession.STATE;
import edu.wpi.quangvu.app.utility.Debug;
import edu.wpi.quangvu.app.utility.RoundRobinQueue;
import edu.wpi.quangvu.app.utility.Utility;

/**
 * Network I/O handler
 * 
 * @author quangvu
 *
 */
public class NetConnection {

	/** The underlying socket **/
	protected final AsynchronousSocketChannel channel;
	/** The client class instance **/
	protected final Client client;

	/** Request queues **/
	protected final RoundRobinQueue<ChatAppRequest> incoming;
	protected final RoundRobinQueue<ChatAppRequest> outgoing;

	private boolean readPending = false;

	private boolean acceptWrite = true;
	private boolean writePending = false;

	public NetConnection(AsynchronousSocketChannel channel, Client client) {
		this.channel = channel;
		this.client = client;
		
		incoming = new RoundRobinQueue<ChatAppRequest>();
		outgoing = new RoundRobinQueue<ChatAppRequest>();
	}

	/**
	 * @return the next request from the queue
	 */
	public ChatAppRequest getNext() {
		ChatAppRequest request = null;
		if (incoming.hasNext()) {
			request = incoming.next();
			incoming.remove();
		}
		return request;
	}

	/**
	 * @param request
	 *            the request to send
	 */
	public void send(ChatAppRequest request) {
		//if (acceptWrite) {
			outgoing.add(request);
		//}
	}

	public void close() {
		incoming.clear();
		outgoing.clear();

		try {
			channel.close();
		} catch (IOException e) {
			// Ignore
		}
	}

	/**
	 * Process requests in queues
	 */
	public void update() {
		// Send outgoing message
		if (!writePending && outgoing.hasNext()) {
			ChatAppRequest toSend = outgoing.next();
			ByteBuffer out = ByteBuffer.wrap(toSend.toString().getBytes());
			try {
				channel.write(out, out, new SendRequestHandler());
				outgoing.remove();
			} catch (WritePendingException e) {
				// Skip
			}
			writePending = true;
		}

		// Listen for incoming message
		if (!readPending) {
			ByteBuffer in = ByteBuffer
					.allocate(ChatAppProtocol.MAX_REQUEST_SIZE);
			try {
				channel.read(in, in, new ReadRequestHandler());
			} catch (ReadPendingException e) {
				// Skip
			}
			readPending = true;
		}
	}

	private class ReadRequestHandler implements
			CompletionHandler<Integer, ByteBuffer> {

		@Override
		public void completed(Integer result, ByteBuffer attachment) {
			// Client disconnected will result in a -1 byte read, don't read
			// anymore
			if (result != -1) {
				String data = Utility.toString(attachment.array());
				Debug.println("Received: " + data);

				ChatAppRequest request = ChatAppProtocol.getInstance().parse(
						data);
				incoming.add(request);
				readPending = false;
			} else {
				readPending = true;
				writePending = true;
				acceptWrite = false;
				
				client.setState(STATE.UNREGISTER);
			}
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			// readPending = false;
		}

	}

	private class SendRequestHandler implements
			CompletionHandler<Integer, ByteBuffer> {

		@Override
		public void completed(Integer result, ByteBuffer attachment) {
			if (result != -1) {
				writePending = false;
			} else {
				readPending = true;
				writePending = true;
				acceptWrite = false;
				
				client.setState(STATE.UNREGISTER);
			}
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			// writePending = false;
		}

	}
}
