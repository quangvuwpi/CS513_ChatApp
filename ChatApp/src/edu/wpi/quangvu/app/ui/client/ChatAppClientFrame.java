package edu.wpi.quangvu.app.ui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.wpi.quangvu.app.ChatAppClient;
import edu.wpi.quangvu.app.manager.TaskManager;
import edu.wpi.quangvu.app.net.protocol.DisconnectRequest;
import edu.wpi.quangvu.app.net.protocol.RequestDispatcher;

public class ChatAppClientFrame extends JFrame {

	private static ChatAppClientFrame instance = null;

	final JPanel loginPanel;
	final LoginPanel login;

	final JPanel conversationPanel;
	final ConversationPanel conversation;

	public static ChatAppClientFrame getInstance() {
		if (instance == null) {
			instance = new ChatAppClientFrame(ChatAppClient.APP_BOUNDS);
		}
		return instance;
	}

	/**
	 * Create the frame.
	 */
	protected ChatAppClientFrame(Dimension size) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(new Rectangle(size));

		login = new LoginPanel();
		loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout(5, 5));
		loginPanel.add(login, BorderLayout.CENTER);

		conversation = new ConversationPanel(size);
		conversationPanel = new JPanel();
		conversationPanel.setLayout(new BorderLayout(5, 5));
		conversationPanel.add(conversation, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				RequestDispatcher.sendRequest(new DisconnectRequest());

				teardown();
				TaskManager.getInstance().teardown();
			}
		});
	}

	public ConversationPanel getConversationPanel() {
		return conversation;
	}

	public void displayLoginView() {
		setContentPane(loginPanel);
		pack();
	}

	public void displayConversationView() {
		setTitle(ChatAppClient.name);

		setContentPane(conversationPanel);
		conversationPanel.requestFocus();
		pack();
	}

	public void printSystemMessage(String s) {
		if (getContentPane() == loginPanel) {
			login.setMessage(s);
		} else {
			conversation.addDialog(String.format("- ChatApp - %s -", s));
		}
	}
	
	public void printSystemErrorMessage(String s) {
		if (getContentPane() == loginPanel) {
			login.setErrorMessage(s);
		} else {			
			conversation.addDialog(String.format("- ChatApp - %s -", s));
		}
	}
	
	public void printDialog(String sender, String message) {
		conversation.addDialog(sender, message);
	}

	public LoginPanel getLoginPanel() {
		return login;
	}

	public void start() {
		displayLoginView();
		setVisible(true);
	}

	public void stop() {

	}

	public void teardown() {
		dispose();
	}

}
