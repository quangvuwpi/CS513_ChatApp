/**
 * 
 */
package edu.wpi.quangvu.app.ui.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import edu.wpi.quangvu.app.ChatAppClient;
import edu.wpi.quangvu.app.net.protocol.NewUserRequest;
import edu.wpi.quangvu.app.net.protocol.RequestDispatcher;
import edu.wpi.quangvu.app.ui.AbstractController;

/**
 * Controller class for login panel
 * 
 * @author quangvu
 *
 */
public class LoginPanelController extends AbstractController {

	LoginPanel panel;

	public LoginPanelController(LoginPanel panel) {
		this.panel = panel;
	}

	void sendUserName() {
		String username = panel.getInput();

		// Don't process empty or invalid string
		if (username.length() == 0) {
			ChatAppClientFrame.getInstance().printSystemErrorMessage(
					"Empty username!!!");
			return;
		} else if (!username.matches("^\\w+$")) {
			ChatAppClientFrame.getInstance().printSystemErrorMessage(
					"Invalid username!!!");
			return;
		}

		ChatAppClient.name = username;
		RequestDispatcher.sendRequest(new NewUserRequest(username));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		sendUserName();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendUserName();
		}
	}

}
