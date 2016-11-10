/**
 * 
 */
package edu.wpi.quangvu.app.session;

import java.util.Iterator;

import edu.wpi.quangvu.app.ChatAppClient;
import edu.wpi.quangvu.app.manager.ClientManager;
import edu.wpi.quangvu.app.manager.QueueManager;
import edu.wpi.quangvu.app.net.protocol.AckRequest;
import edu.wpi.quangvu.app.net.protocol.ChatAppRequest;
import edu.wpi.quangvu.app.net.protocol.ErrorRequest;
import edu.wpi.quangvu.app.net.protocol.GetUserListRequest;
import edu.wpi.quangvu.app.net.protocol.MessageRequest;
import edu.wpi.quangvu.app.net.protocol.UpdateUserListRequest;
import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.session.ChatAppSession.STATE;
import edu.wpi.quangvu.app.ui.client.ChatAppClientFrame;
import edu.wpi.quangvu.app.ui.client.ConversationPanel;

/**
 * @author quangvu
 *
 */
public class ClientSession {

	public static void service(Client client) {
		ChatAppRequest request;

		ChatAppSession.STATE s = client.getState();

		switch (s) {
		case CONNECT:
			client.setState(STATE.REGISTER);
			break;
		case REGISTER:
			request = client.getNextRequest();
			if (request != null) {
				if (request instanceof AckRequest) {
					client.setState(STATE.ACTIVE);

					client.sendRequest(new GetUserListRequest());
					ChatAppClientFrame.getInstance().displayConversationView();
				} else if (request instanceof ErrorRequest) {
					// Name already taken, get another name
					ChatAppClientFrame.getInstance().printSystemErrorMessage(
							"Name taken!");
				}
			}
			break;
		case ACTIVE:
			request = client.getNextRequest();
			if (request != null) {
				if (request instanceof UpdateUserListRequest) {
					ConversationPanel panel = ChatAppClientFrame.getInstance()
							.getConversationPanel();
					UpdateUserListRequest r = (UpdateUserListRequest) request;

					Iterator<String> list = r.getAddList().iterator();
					while (list.hasNext()) {
						String c = list.next();
						if (panel.addUser(c)) {
							ChatAppClientFrame.getInstance()
									.printSystemMessage(
											String.format(
													"User %s has joined!", c));
						}
						list.remove();
					}

					list = r.getRemoveList().iterator();
					while (list.hasNext()) {
						String c = list.next();
						if (panel.removeUser(c)) {
							ChatAppClientFrame.getInstance()
									.printSystemErrorMessage(
											String.format("User %s has left!",
													c));
						}
						list.remove();
					}
				} else if (request instanceof MessageRequest) {
					MessageRequest r = (MessageRequest) request;

					String sender = r.getSender();
					// If message was sent to a particular recipient, indicate
					// whisper
					if (r.getRecipient().equals(ChatAppClient.name)) {
						sender += " (whispered)";
					}
					ChatAppClientFrame.getInstance().printDialog(sender,
							r.getMessage());
				}
			}
			break;
		case UNREGISTER:
			ClientManager.getInstance().unregister(client);
			client.setState(STATE.DISCONNECT);
			break;
		case DISCONNECT:
			ChatAppClientFrame.getInstance().printSystemErrorMessage(
					"Server disconnected!");
			client.disconnect();
			QueueManager.getInstance().getClientQueue().remove(client);
			ChatAppClientFrame.getInstance().getConversationPanel()
					.clearUserList();
			break;
		case PENDING:
			// Nothing to do, just skip
			break;
		default:
			client.setState(STATE.UNREGISTER);
			break;
		}
	}

}
