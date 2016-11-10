/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

import java.util.ArrayList;
import java.util.LinkedList;

import edu.wpi.quangvu.app.manager.ClientManager;
import edu.wpi.quangvu.app.resource.Client;

/**
 * Common interface to dispatch requests
 * 
 * @author quangvu
 *
 */
public class RequestDispatcher {

	/**
	 * Send a message request, can be broken up into multiple requests
	 * 
	 * @param recipient
	 *            the destination
	 * @param sender
	 *            the source
	 * @param message
	 *            message to be sent
	 */
	public static void sendMesageRequest(String recipient, String sender,
			String message) {
		LinkedList<MessageRequest> fifo = new LinkedList<MessageRequest>();

		// Check if string needs to be sent in multiple requests
		int overhead = new MessageRequest(recipient, sender, "").toString()
				.length();
		int length = message.length();
		if (overhead + length > ChatAppProtocol.MAX_REQUEST_SIZE) {
			int start = 0;
			int chunk;

			while (length > 0) {
				chunk = Math.min(length, ChatAppProtocol.MAX_REQUEST_SIZE
						- overhead);
				MessageRequest r = new MessageRequest(recipient, sender,
						message.substring(start, start + chunk - 1));

				fifo.addFirst(r);
				length -= chunk;
				start += chunk;
			}
		} else {
			fifo.add(new MessageRequest(recipient, sender, message));
		}

		// Send the messages
		while (!fifo.isEmpty()) {
			sendRequest(fifo.removeFirst());
		}
	}

	/**
	 * Send a request to a connected client
	 * 
	 * @param name
	 *            name of the recipient
	 * @param request
	 *            the request to send
	 */
	public static void sendRequest(String name, ChatAppRequest request) {
		// if recipient is empty, then broadcast
		if (name.equals("")) {
			sendRequest(request);
		}

		Client recipient = ClientManager.getInstance().getClient(name);
		if (recipient != null) {
			recipient.sendRequest(request);
		}
	}

	/**
	 * Broadcast a request to every connected client
	 * 
	 * @param request
	 *            the request to send
	 */
	public static void sendRequest(ChatAppRequest request) {
		ArrayList<String> names = ClientManager.getInstance().getAllName();
		for (int i = 0; i < names.size(); i++) {
			sendRequest(names.get(i), request);
		}
	}

}
