package gui;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Insets;

public class SettingsWindow extends JTabbedPane {

	JPanel generalPanel;
	JPanel emailPanel;
	
	public SettingsWindow() {
		setTabPlacement(JTabbedPane.LEFT);
		setPreferredSize(new Dimension(600, 300));
		
		createEmailSettings();
		createGeneralSettings();
		setSelectedIndex(0);
		
		String[] options = new String[]{"OK", "Cancel"};
		
		int option = JOptionPane.showOptionDialog(null, this, "Settings",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, options[0]);
		
	}
	
	private void createGeneralSettings() {
		generalPanel = new JPanel();
		generalPanel.setLayout(null);
		
		JCheckBox rememberLoaded = new JCheckBox(" Remember loaded CSV files");
		
		Insets insets = generalPanel.getInsets();
		Dimension size = rememberLoaded.getPreferredSize();
		rememberLoaded.setBounds(30 + insets.left, 30 + insets.top, size.width, size.height);
		
		generalPanel.add(rememberLoaded);
		
		addTab("General", generalPanel);
	}
	
	private void createEmailSettings() {
		emailPanel = new JPanel();
		emailPanel.setLayout(null);
		
		JLabel jlSettings = new JLabel("Settings");
		JLabel jlServer = new JLabel("Server Name:");
		JLabel jlPort = new JLabel("Port:");
		JTextField jtServer = new JTextField();
		JSpinner jsPort = new JSpinner();
		jsPort.setValue(587);
		JLabel jlPortMsg = new JLabel("Default:  587");
		JLabel jlSec = new JLabel("Security and Authentication");
		JLabel jlConn = new JLabel("Connection Security:");
//		JLabel jlAuth = new JLabel("Authentication Method:");
		JComboBox<String> jcConn = new JComboBox<String>(new String[] {"None", "STARTTLS"});
		jcConn.setSelectedIndex(1);
		JLabel jlUser = new JLabel("User Name:");
		JTextField jtUser = new JTextField();
//		JButton
		
		emailPanel.add(jlSettings);
		emailPanel.add(jlServer);
		emailPanel.add(jlPort);
		emailPanel.add(jtServer);
		emailPanel.add(jsPort);
		emailPanel.add(jlPortMsg);
		emailPanel.add(jlSec);
		emailPanel.add(jlConn);
//		emailPanel.add(jlAuth);
		emailPanel.add(jcConn);
		emailPanel.add(jlUser);
		emailPanel.add(jtUser);

		Insets insets = emailPanel.getInsets();
		
		Dimension size = jlSettings.getPreferredSize();
		jlSettings.setBounds(15 + insets.left, 25 + insets.top, size.width, size.height);
		
		size = jlServer.getPreferredSize();
		jlServer.setBounds(30 + insets.left, 59 + insets.top, size.width, size.height);
		
		size = jlPort.getPreferredSize();
		jlPort.setBounds(30 + insets.left, 86 + insets.top, size.width, size.height);
		
		size = jtServer.getPreferredSize();
		jtServer.setBounds(140 + insets.left, 55 + insets.top, 350, size.height + 5);
		
		size = jsPort.getPreferredSize();
		jsPort.setBounds(140 + insets.left, 82 + insets.top, 80, size.height + 5);
		
		size = jlPortMsg.getPreferredSize();
		jlPortMsg.setBounds(230 + insets.left, 86 + insets.top, size.width, size.height);
		
		size = jlSec.getPreferredSize();
		jlSec.setBounds(15 + insets.left, 130 + insets.top, size.width, size.height);
		
		size = jlConn.getPreferredSize();
		jlConn.setBounds(30 + insets.left, 164 + insets.top, size.width, size.height);
		
//		size = jlAuth.getPreferredSize();
//		jlAuth.setBounds(30 + insets.left, 194 + insets.top, size.width, size.height);
		
		size = jcConn.getPreferredSize();
		jcConn.setBounds(220 + insets.left, 160 + insets.top, 270, size.height);
		
		size = jlUser.getPreferredSize();
		jlUser.setBounds(30 + insets.left, 192 + insets.top, size.width, size.height);
		
		size = jtUser.getPreferredSize();
		jtUser.setBounds(220 + insets.left, 190 + insets.top, 270, size.height + 5);
		
		
		addTab("Email", emailPanel);
	}
	
	public static void main(String[] args) {
		SettingsWindow sw = new SettingsWindow();
	}
}
