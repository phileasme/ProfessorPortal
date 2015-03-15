package gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class SettingsWindow extends JFrame {

	JTabbedPane tabbedPane;
	JPanel generalPanel;
	JPanel emailPanel;
	
	public SettingsWindow() {
		super("Settings");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 400));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		
		createEmailSettings();
		createGeneralSettings();
		add(tabbedPane);
		tabbedPane.setSelectedIndex(0);
	}
	
	private void createGeneralSettings() {
		generalPanel = new JPanel();
		generalPanel.add(new JLabel("poop!"));
		
		tabbedPane.addTab("General", generalPanel);
	}
	
	private void createEmailSettings() {
		emailPanel = new JPanel();
		emailPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel jlSettings = new JLabel("Settings");
		c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
		emailPanel.add(jlSettings, c);
		
		JLabel jlServer = new JLabel("Server Name:");
		c.gridx = 0; c.gridy = 1;
		emailPanel.add(jlServer, c);
		
		JLabel jlPort = new JLabel("Port:");
		c.gridx = 0; c.gridy = 2;
		emailPanel.add(jlPort, c);
		
		
		tabbedPane.addTab("Email", emailPanel);
	}
	
	public static void main(String[] args) {
		SettingsWindow sw = new SettingsWindow();
		sw.setVisible(true);
	}
}
