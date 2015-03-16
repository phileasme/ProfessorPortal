package gui;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Font;

import io.Settings;

/**
 * Opens a dialog box with options to change some email and general program
 * settings.
 * 
 * @author Max Karasinski
 *
 */
public class SettingsWindow extends JTabbedPane {

	JPanel generalPanel;
	JPanel emailPanel;
	Settings settings;
	
	// fields which get saved to settings
	JCheckBox rememberLoaded;
	JTextField jtServer;
	JSpinner jsPort;
	JComboBox<String> jcConn;
	JTextField jtUser;
	
	/**
	 * Opens the settings dialog box and loads the settings stored on the 
	 * provided {@link Settings} instance.
	 * 
	 * @param settings program settings provided from MainInterface
	 */
	public SettingsWindow(Settings settings) {
		this.settings = settings;
		
		setTabPlacement(JTabbedPane.LEFT);
		setPreferredSize(new Dimension(600, 300));
		
		createEmailSettings();
		createGeneralSettings();
		setSelectedIndex(0);
		
		String[] options = new String[]{"Cancel", "OK"};
		
		int option = JOptionPane.showOptionDialog(null, this, "Settings",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, options[0]);
		
		if (option == 1) saveSettings();
	}
	
	/**
	 * Fetches values from the various components in the dialog box and stores
	 * their values on the Settings object.
	 */
	private void saveSettings() {
		settings.set("mail.smtp.host", jtServer.getText().trim());
		
		String port = String.valueOf(jsPort.getValue());
		settings.set("mail.smtp.port", port);
		
		if (!(jcConn.getSelectedItem().equals("None"))) {
			settings.set("mail.smtp.starttls.enable", "true");
		} else {
			settings.set("mail.smtp.starttls.enable", "false");
		}
		
		String checked = String.valueOf(rememberLoaded.isSelected());
		settings.set("save_data", checked);
		
		settings.set("username", jtUser.getText().trim());
	}
	
	/**
	 * Creates a tab for general program settings.
	 */
	private void createGeneralSettings() {
		generalPanel = new JPanel();
		generalPanel.setLayout(null);
		
		rememberLoaded = new JCheckBox(" Remember loaded CSV files");
		if (Boolean.parseBoolean(settings.get("save_data"))) rememberLoaded.setSelected(true);
		
		Insets insets = generalPanel.getInsets();
		Dimension size = rememberLoaded.getPreferredSize();
		rememberLoaded.setBounds(30 + insets.left, 30 + insets.top, size.width, size.height);
		
		generalPanel.add(rememberLoaded);
		
		addTab("General", generalPanel);
	}
	
	/**
	 * Creates a tab for email settings.
	 */
	private void createEmailSettings() {
		emailPanel = new JPanel();
		emailPanel.setLayout(null);
		
		JLabel jlSettings = new JLabel("Settings");
		JLabel jlServer = new JLabel("Server Name:");
		JLabel jlPort = new JLabel("Port:");
		jtServer = new JTextField();
		jtServer.setText(settings.get("mail.smtp.host"));
		
		jsPort = new JSpinner();
		try {
			int port = Integer.valueOf(settings.get("mail.smtp.port"));
			jsPort.setValue(port);
		} catch (NumberFormatException e) {
			jsPort.setValue(587);
		}
		
		JLabel jlPortMsg = new JLabel("Default:  587");
		JLabel jlSec = new JLabel("Security and Authentication");
		JLabel jlConn = new JLabel("Connection Security:");
//		JLabel jlAuth = new JLabel("Authentication Method:");
		
		jcConn = new JComboBox<String>(new String[] {"None", "STARTTLS"});
		
		boolean b = Boolean.parseBoolean(settings.get("mail.smtp.starttls.enable"));
		if (b) {
			jcConn.setSelectedIndex(1);
		} else {
			jcConn.setSelectedIndex(0);
		}
		JLabel jlUser = new JLabel("User Name:");
		jtUser = new JTextField();
		jtUser.setText(settings.get("username"));
		
		Font f = jlSettings.getFont();
		Font plain = new Font(f.getFamily() + ".plain", Font.PLAIN, f.getSize());
		jlServer.setFont(plain); jlPort.setFont(plain); jlPortMsg.setFont(plain);
		jlConn.setFont(plain); jlUser.setFont(plain);
		
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
}
