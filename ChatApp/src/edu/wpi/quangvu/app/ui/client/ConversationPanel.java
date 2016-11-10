package edu.wpi.quangvu.app.ui.client;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;

import edu.wpi.quangvu.app.net.protocol.GetUserListRequest;
import edu.wpi.quangvu.app.net.protocol.RequestDispatcher;

public class ConversationPanel extends JPanel {

	JTextField userInput;

	JList<String> conversation;
	JList<String> userList;

	ConversationPanelController controller;

	Font myFont = new Font("Serif", Font.PLAIN, 16);

	/**
	 * Create the panel.
	 */
	public ConversationPanel(Dimension size) {
		setLayout(new BorderLayout(0, 0));

		// Set up conversation list
		Dimension d = new Dimension((int) (size.width * 0.70),
				(int) (size.height * 0.90));

		conversation = new JList<String>(new DefaultListModel<String>());
		conversation.setSelectionInterval(-1, -1); // not selectable
		conversation.setCellRenderer(new ListCellRenderer<String>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends String> list, String value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JTextArea area = new JTextArea(value);
				area.setFont(myFont);

				return area;
			}
		});
		JScrollPane conversationPane = new JScrollPane(conversation);
		conversationPane.setPreferredSize(d);
		conversationPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(conversationPane, BorderLayout.CENTER);

		// Setup the user list
		d = new Dimension((int) (size.width * 0.30), (int) (size.height * 0.90));

		userList = new JList<String>(new DefaultListModel<String>());
		userList.addMouseListener(new userListDoubleClicked());
		JScrollPane usersPane = new JScrollPane(userList);
		usersPane.setPreferredSize(d);
		add(usersPane, BorderLayout.EAST);

		JPanel inputPane = new JPanel();
		add(inputPane, BorderLayout.SOUTH);
		inputPane.setLayout(new BorderLayout(0, 0));

		controller = new ConversationPanelController(this);

		d = new Dimension((int) (size.width * 0.90), (int) (size.height * 0.10));

		userInput = new JTextField();
		userInput.addKeyListener(controller);
		inputPane.add(userInput, BorderLayout.CENTER);
		userInput.setPreferredSize(d);
		userInput.setColumns(10);
		userInput.setFocusable(true);
		userInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				if (userInput.getText().equals("")) {
					userInput.setText("Enter Message");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);

				if (userInput.getText().equals("Enter Message")) {
					userInput.setText("");
				}
			}
		});
		userInput.requestFocusInWindow();
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addMouseListener(controller);
		inputPane.add(btnNewButton, BorderLayout.EAST);
	}

	public boolean isPresent(String user) {
		DefaultListModel<String> m = (DefaultListModel<String>) userList
				.getModel();
		return m.contains(user);
	}

	public String getUserInput() {
		return userInput.getText();
	}

	public void setUserInput(String s) {
		userInput.setText(s);
	}

	public void clearUserInput() {
		userInput.setText("");
	}

	public void addDialog(String sender, String s) {
		addDialog(String.format("%s: %s", sender, s));
	}

	public void addDialog(String s) {
		DefaultListModel<String> m = (DefaultListModel<String>) conversation
				.getModel();

		m.addElement(s);
		conversation.ensureIndexIsVisible(m.size() - 1); // Auto scroll
	}

	public boolean addUser(String s) {
		DefaultListModel<String> m = (DefaultListModel<String>) userList
				.getModel();

		if (!m.contains(s)) {
			m.addElement(s);
			userList.ensureIndexIsVisible(m.size() - 1); // Auto scroll
			return true;
		}

		return false;
	}

	public void addUser(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			addUser(list.get(i));
		}
	}

	public boolean removeUser(String s) {
		DefaultListModel<String> m = (DefaultListModel<String>) userList
				.getModel();

		int index = m.lastIndexOf(s);
		if (index != -1) {
			m.remove(index);
			userList.ensureIndexIsVisible(m.size() - 1); // Auto scroll
			return true;
		}

		return false;
	}

	public void removeUser(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			removeUser(list.get(i));
		}
	}

	public void clearUserList() {
		DefaultListModel<String> m = (DefaultListModel<String>) userList
				.getModel();

		m.clear();
	}

	private class userListDoubleClicked extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				String user = userList.getSelectedValue();
				if (user == null && userList.getModel().getSize() == 0) {
					RequestDispatcher.sendRequest(new GetUserListRequest());
				} else if (user != null) {
					controller.sendToUser(userList.getSelectedValue());
				}
			}
		}

	}

}
