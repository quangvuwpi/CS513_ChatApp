package edu.wpi.quangvu.app.ui.client;

import javax.swing.JPanel;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginPanel extends JPanel {
	
	private JTextField userName;
	private JLabel     message;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		LoginPanelController c = new LoginPanelController(this);
		
		userName = new JTextField();
		userName.addKeyListener(c);
		userName.setColumns(10);
		
		JButton btnNewButton = new JButton("Log In");
		btnNewButton.addMouseListener(c);
		
		message = new JLabel("Select Username");
		message.setLabelFor(userName);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(150)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(message, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(userName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(150, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(85)
					.addComponent(message, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(userName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnNewButton)
					.addContainerGap(120, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	
	public void setMessage(String s) {
		message.setForeground(Color.BLACK);
		message.setText(s);
	}
	
	public void setErrorMessage(String s) {
		message.setForeground(Color.RED);
		message.setText(s);
	}
	
	public String getInput() {
		return userName.getText();
	}
}
