package test;

import java.util.Properties;
import java.util.Date;

import javax.swing.*;
import java.awt.GridLayout;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.URLName;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

public class EmailTest {

//	private String serverName = "outlook.office365.com";
//	private int port = 993;
	
	private String serverName = "pod51013.outlook.com";
	private int port = 587;
	
	private String fromAddress; // must be k-----@kcl.ac.uk
	private String password;
	
	private String toAddress = "kenneth.barker@mailinator.com";
	
	
	public EmailTest() {
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", serverName);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		
		int i = getAuth();
		
		if (i == 0) {

			try {
				PRAauthenticator pa = new PRAauthenticator(fromAddress, password);
				Session session = Session.getInstance(props, pa);
				session.setDebug(true);
				
				// set up message
				Message msg = new MimeMessage(session);

				msg.setFrom(new InternetAddress(fromAddress));

				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

				msg.setSubject("test message");

				String msgText = "Hello there,\nThis is a test message. Good day";
				msg.setText(msgText);

				// does it matter what this is?
				msg.setHeader("X-Mailer", "Plague Rat 666");
				msg.setSentDate(new Date());

				Transport transport = session.getTransport();
				transport.connect();
				transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
				transport.close();

				System.out.println("\nGREAT SUCCESS!");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private int getAuth() {
		JPanel panel = new JPanel();
		JLabel nameLabel = new JLabel("Email: ");
		JLabel passLabel = new JLabel("Password: ");
		JTextField text = new JTextField(20);
		JPasswordField passwd = new JPasswordField(15);

		panel.setLayout(new GridLayout(2, 2));

		panel.add(nameLabel); panel.add(text);
		panel.add(passLabel); panel.add(passwd);
		
		String[] options = new String[]{"OK", "Cancel"};
		
		int option = JOptionPane.showOptionDialog(null, panel, "Enter info",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, options[0]);
		
		if (option == 0) {
			fromAddress = text.getText();
			password = new String(passwd.getPassword());
		}
		
		return option;
	}
	
	public static void main(String[] args) {
		EmailTest et = new EmailTest();
	}
	
	class PRAauthenticator extends Authenticator {
		String email;
		String password;
		
		public PRAauthenticator(String email, String password) {
			this.email = email;
			this.password = password;
		}
		
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(email, password);
		}
	}
}
