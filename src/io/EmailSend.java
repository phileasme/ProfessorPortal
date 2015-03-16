package io;

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

public class EmailSend {

//	private String serverName = "outlook.office365.com";
//	private int port = 993;
	
	private String serverName = "pod51013.outlook.com";
	private int port = 587;
	
	private String fromAddress; // must be k-----@kcl.ac.uk
	private String password;
	
	private String toAddress;
	private String messageToSend;
	
	public EmailSend(String to, String from, String password, String message, int i) {
		toAddress = to;
		messageToSend = message;
		fromAddress = from;
		this.password = password;
		
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", serverName);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		
		if (i == 0) {

			try {
				long start = System.currentTimeMillis();
				PRAauthenticator pa = new PRAauthenticator(fromAddress, password);
				Session session = Session.getInstance(props, pa);
				session.setDebug(true);
				
				// set up message
				Message msg = new MimeMessage(session);

				msg.setFrom(new InternetAddress(fromAddress));

				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

				msg.setSubject("Exam Results");

				msg.setText(messageToSend);

				// does it matter what this is?
				msg.setHeader("X-Mailer", "Plague Rat 666");
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
