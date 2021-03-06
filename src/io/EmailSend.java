package io;

import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.AuthenticationFailedException;

/**
 * Class to send an email using an SMTP Server and Port using the JavaMail API.
 * 
 * @author Nikita Vorontsov
 * @author Max Karasinski
 */
public class EmailSend {
	
	private String fromAddress; // must be k-----@kcl.ac.uk
	
	private String toAddress;
	
	boolean debug = false;
	
	/**
	 * Constructs the email to send using a ToAddress, FromAddress(Received either from .properties or the EmailWindow getAuth() call)
	 * Password, a Message made in EmailWindow, authorisation and Email Settings from Settings
	 * 
	 * @param to the Email of the Recepient
	 * @param from the Email of the Sender
	 * @param password the password relating to the Sender's Email
	 * @param message the message to send in the Email
	 * @param i the getAuth() option
	 * @param settings the current settings stored in .properties
	 * 
	 * @throws AuthenticationFailedException if the provided password is incorrect
	 */
	public EmailSend(String to, String from, String password, String message, int i, Settings settings) throws AuthenticationFailedException {
		toAddress = to;
		fromAddress = from;
		
		if (i == 0) {

			try {
				PRAauthenticator pa = new PRAauthenticator(fromAddress, password);
				Session session = Session.getInstance(settings.getProps(), pa);
				session.setDebug(debug);
				
				// set up message
				Message msg = new MimeMessage(session);

				msg.setFrom(new InternetAddress(fromAddress));

				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

				msg.setSubject("Exam Results");

				msg.setContent(message, "text/html; charset=utf-8");

				msg.setSentDate(new Date());

				Transport transport = session.getTransport();
				transport.connect();
				transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
				transport.close();

			} catch (AuthenticationFailedException afEx) {
				throw afEx;
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
