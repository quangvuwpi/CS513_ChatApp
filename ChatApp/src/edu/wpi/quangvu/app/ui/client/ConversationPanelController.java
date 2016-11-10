/**
 * 
 */
package edu.wpi.quangvu.app.ui.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wpi.quangvu.app.ChatAppClient;
import edu.wpi.quangvu.app.net.protocol.RequestDispatcher;
import edu.wpi.quangvu.app.ui.AbstractController;

/**
 * Controller class for conversation panel
 * 
 * @author quangvu
 *
 */
public class ConversationPanelController extends AbstractController {

	final ConversationPanel panel;

	public ConversationPanelController(ConversationPanel panel) {
		this.panel = panel;
	}

	public void sendToUser(String user) {
		panel.setUserInput(String.format("@%s ", user));
	}

	/**
	 * Send user input to the server
	 */
	void sendUserInput() {
		String dialog = panel.getUserInput();
		panel.clearUserInput();

		// Don't process empty string
		if (dialog.length() == 0) {
			return;
		}

		String recipient = "";
		String message = dialog;
		String sender = ChatAppClient.name;

		// See if the user wants to whisper
		Pattern p = Pattern.compile("@(\\w+)\\s+(.*)");
		Matcher m = p.matcher(dialog.trim());
		if (m.matches()) {
			if (panel.isPresent(m.group(1))) {
				recipient = m.group(1);
				message = m.group(2);

				// If whispering, then add our own dialog without going through
				// the server
				if (!recipient.equals(sender)) {
					ChatAppClientFrame.getInstance().printDialog(
							"(whispered to) " + recipient, message);
				}
			}
		}

		RequestDispatcher.sendMesageRequest(recipient, sender, message);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		sendUserInput();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Send text when ENTER is pressed
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendUserInput();
		}
	}

}
