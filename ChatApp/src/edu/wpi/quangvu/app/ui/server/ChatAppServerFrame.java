package edu.wpi.quangvu.app.ui.server;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.quangvu.app.utility.IManageableResource;

public class ChatAppServerFrame extends JFrame implements IManageableResource {
	
	private static ChatAppServerFrame instance = null;

	JList<String> list;

	public static ChatAppServerFrame getInstance() {
		if (instance == null) {
			instance = new ChatAppServerFrame();
		}
		return instance;
	}
	
	/**
	 * Create the frame.
	 */
	protected ChatAppServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		list = new JList<String>(new DefaultListModel<String>());
		list.setEnabled(false);
		list.setFocusTraversalKeysEnabled(false);
		list.setFocusable(false);
		JScrollPane scroll = new JScrollPane(list);
		
		panel.add(scroll, BorderLayout.CENTER);
		setContentPane(panel);
		
		addWindowListener(new WindowListener());
	}
	
	public void start() {
		setVisible(true);
	}
	
	public void stop() {
		setVisible(false);
	}
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void teardown() {
		dispose();
	}
	
	public void println(String s) {
		DefaultListModel<String> m = (DefaultListModel<String>) list.getModel();
		
		m.addElement(s);
		list.ensureIndexIsVisible(m.size() - 1);
	}
	
	private class WindowListener extends WindowAdapter {		
		@Override
		public void windowClosing(WindowEvent e) {			
			teardown();
			
			super.windowClosing(e);
		}
	}	

}
