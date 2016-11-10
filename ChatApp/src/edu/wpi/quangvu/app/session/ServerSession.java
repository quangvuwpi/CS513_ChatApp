/**
 * 
 */
package edu.wpi.quangvu.app.session;

import java.util.ArrayList;

import edu.wpi.quangvu.app.manager.ClientManager;
import edu.wpi.quangvu.app.manager.QueueManager;
import edu.wpi.quangvu.app.net.protocol.AckRequest;
import edu.wpi.quangvu.app.net.protocol.ChatAppRequest;
import edu.wpi.quangvu.app.net.protocol.DisconnectRequest;
import edu.wpi.quangvu.app.net.protocol.ErrorRequest;
import edu.wpi.quangvu.app.net.protocol.GetUserListRequest;
import edu.wpi.quangvu.app.net.protocol.MessageRequest;
import edu.wpi.quangvu.app.net.protocol.NewUserRequest;
import edu.wpi.quangvu.app.net.protocol.RequestDispatcher;
import edu.wpi.quangvu.app.net.protocol.UpdateUserListRequest;
import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.session.ChatAppSession.STATE;
import edu.wpi.quangvu.app.ui.server.ChatAppServerFrame;

/**
 * State machine to manage the life cycle of a client connection
 * 
 * @author quangvu
 *
 */
public class ServerSession {

	public static void service(Client client) {
		ChatAppRequest request;

		ChatAppSession.STATE s = client.getState();

		switch (s) {
		case CONNECT:
			println(String.format("Connected to %s", client.getName()));
			client.setState(STATE.REGISTER);
			break;
		case REGISTER:
			request = client.getNextRequest();
			if (request != null) {
				if (request instanceof NewUserRequest) {
					String old = client.getName();
					NewUserRequest ur = (NewUserRequest) request;

					// Register client
					client.setName(ur.name);
					if (ClientManager.getInstance().register(client)) {
						println(String.format("Registered %s as %s", old,
								client.getName()));
						client.setState(STATE.ACTIVE);
						client.sendRequest(new AckRequest());

						// Broadcast to other users to update client list
						RequestDispatcher.sendRequest(getUpdateUserRequest());
					} else { // Name is already taken, send error request
						client.setName(old);
						client.sendRequest(new ErrorRequest());
					}
				} else {
					// Invalid request, disconnect client
					client.setState(STATE.DISCONNECT);
				}
			}
			break;
		case ACTIVE:
			request = client.getNextRequest();
			if (request != null) {
				if (request instanceof GetUserListRequest) {					
					client.sendRequest(getUpdateUserRequest());
				} else if (request instanceof MessageRequest) {
					MessageRequest r = (MessageRequest) request;
					RequestDispatcher.sendRequest(r.getRecipient(), request);
				} else if (request instanceof DisconnectRequest) {
					client.setState(STATE.UNREGISTER);
				}
			}
			break;
		case UNREGISTER:
			println(String.format("Unregister %s", client.getName()));
			unregister(client);
			client.setState(STATE.DISCONNECT);
			break;
		case DISCONNECT:
			println(String.format("Disconnect %s", client.getName()));
			client.disconnect();
			remove(client);
			break;
		case PENDING:
			// Nothing to do, just skip
			break;
		default:
			client.setState(STATE.UNREGISTER);
			break;
		}
	}
	
	public static void unregister(Client client) {
		ClientManager.getInstance().unregister(client);
		
		UpdateUserListRequest request = new UpdateUserListRequest();
		request.remove(client.getName());		
		
		RequestDispatcher.sendRequest(request);
	}

	public static ChatAppRequest getUpdateUserRequest() {
		ArrayList<String> list = ClientManager.getInstance().getAllName();
		UpdateUserListRequest request = new UpdateUserListRequest();
		
		request.add(list);
		return request;
	}

	public static void remove(Client client) {
		QueueManager.getInstance().getClientQueue().remove(client);
	}

	public static void println(String s) {
		ChatAppServerFrame.getInstance().println(s);
	}

}
