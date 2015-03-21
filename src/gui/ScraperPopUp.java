package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import records.Logs;
import io.Scraper;

/**
 *	Displays a interface where the user enters
 * 	the partipation data URL of the students and module it corresponds to.
 *	Displays the request of the K-number and password,
 * 	if the user is not already logged in within the App.
 * 
 * @author Phileas Hocquard
 */
public class ScraperPopUp extends JFrame{

	private JPanel jpField; 
	private JPanel jpConfirmation;

	private JLabel jlUrl =  new JLabel("Url of the participant data: ") ;
	private JLabel jlModuleName= new JLabel("Module name: ");
	private JLabel jlKnumb = new JLabel("K-number: ");
	private JLabel jlPwd = new JLabel("Password: "); 


	private JTextField jturl= new JTextField();
	private JTextField jtModuleName= new JTextField();
	private JTextField jtKnumb = new JTextField();

	private JPasswordField jtpassword = new JPasswordField(20);

	private JButton jbconfirm = new JButton("Confirm");

	/** The log. */
	Logs log = new Logs();


	/**
	 * Instantiates a new scrapper pop up.
	 * This will display the JFrame with content.
	 */
	public ScraperPopUp(){
		super(" Participant Data ");
		new JFrame("Participations");
		setSize(250,200);
		if(Logs.loggedin == false){
			GridLayout myGrid = new GridLayout(4, 2);
			jpField= new JPanel (myGrid); 
			jpConfirmation =new JPanel(new FlowLayout());
			jlKnumb = new JLabel ("K-number:");
			jlPwd = new JLabel ("Password:"); 

			jpField.add(jlUrl);jpField.add(jturl);
			jpField.add(jlModuleName);jpField.add(jtModuleName);
			jpField.add(jlKnumb);jpField.add(jtKnumb);
			jpField.add(jlPwd);jpField.add(jtpassword);

			jpField.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

			jpConfirmation.add(jbconfirm);

			add(jpField,BorderLayout.CENTER);
			add(jpConfirmation,BorderLayout.SOUTH);
		} else {
			GridLayout myGrid = new GridLayout(2, 2);
			jpField= new JPanel (myGrid); 
			jpConfirmation =new JPanel(new FlowLayout());

			jpField.add(jlUrl);jpField.add(jturl);
			jpField.add(jlModuleName);jpField.add(jtModuleName);


			jpField.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

			jpConfirmation.add(jbconfirm);

			add(jpField,BorderLayout.CENTER);
			add(jpConfirmation,BorderLayout.SOUTH);
		}

		pack();
		setVisible(true);
		
		ActionListener ConfirmationListener = new ActionListener() {

			/**
			 * ActionListener which grabs the user input from the TextFields
			 *If the user is already logged in, the credentials are took
			 *from the object Logs.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getSource() == jbconfirm){
					String url = jturl.getText();
					String mname = jtModuleName.getText();
					String knumb = jtKnumb.getText();
					String pass = String.copyValueOf(jtpassword.getPassword());


					try {
						if(Logs.loggedin == true){
							knumb = Logs.user;
							pass = Logs.pass;
						}
						
						Scraper scrap = new Scraper(url,mname,knumb,pass);
						if(Logs.loggedin == true) {
							ScraperPopUp.this.setVisible(false);
						}
					} catch (FailingHttpStatusCodeException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		jbconfirm.addActionListener(ConfirmationListener);
	}
}
