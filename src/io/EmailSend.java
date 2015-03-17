package io;

import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class to send an email using an SMTP Server and Port using the JavaMail API
 * 
 * @author Nikita Vorontsov
 * @author Max Karasinski
 */
public class EmailSend {
	
	private String serverName;
	private int port;
	
	private String fromAddress; // must be k-----@kcl.ac.uk
	private String password;
	
	private String toAddress;
	private String messageToSend;
	
	private Settings settings;
	
	/**
	 * Constructs the email to send using a ToAddress, FromAddress(Received either from .properties or the EmailWindow getAuth() call)
	 * Password, a Message made in EmailWindow, authorisation and Email Settings from Settings
	 * @param to The Email of the Recepient
	 * @param from The Email of the Sender
	 * @param password The password relating to the Sender's Email
	 * @param message The message to send in the Email
	 * @param i The getAuth() option
	 * @param set The current settings stored in .properties
	 */
	public EmailSend(String to, String from, String password, String message, int i, Settings set) {
		toAddress = to;
		messageToSend = message;
		fromAddress = from;
		this.password = password;
		settings = set;
		serverName = settings.get("mail.smtp.host");
		port = Integer.parseInt(settings.get("mail.smtp.port"));
		
		if (i == 0) {

			try {
				long start = System.currentTimeMillis();
				PRAauthenticator pa = new PRAauthenticator(fromAddress, password);
				Session session = Session.getInstance(set.getProps(), pa);
				session.setDebug(true);
				
				// set up message
				Message msg = new MimeMessage(session);

				msg.setFrom(new InternetAddress(fromAddress));

				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

				msg.setSubject("Exam Results");

				msg.setText(messageToSend);

				// does it matter what this is?
				msg.setHeader("X-Mailer", "some guy");
				msg.setSentDate(new Date());

				Transport transport = session.getTransport();
				transport.connect();
				transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
				transport.close();

				System.out.println("\nGREAT SUCCESS!");
				long end = System.currentTimeMillis();
				System.out.println(end-start);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Authenticates a password using an email and password.
	 *
	 */
	class PRAauthenticator extends Authenticator {
		String email;
		String password;
		
		/**
		 * Constructs an Authenticator using an Email and Password
		 * @param email Sender's Email
		 * @param password Sender's Password
		 */
		public PRAauthenticator(String email, String password) {
			this.email = email;
			this.password = password;
		}
		
		/**
		 * Authenticates Password.
		 */
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(email, password);
		}
	}
}
