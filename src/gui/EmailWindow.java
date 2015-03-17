package gui;

import io.EmailSend;
import io.Settings;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import records.Result;
import records.Student;
import records.StudentRecords;

import com.jidesoft.swing.CheckBoxList;

/**
 * Class to create a window for sending a customizable e-mail to a number of students and to send their respectable marks
 * 
 * @author Nikita Vorontsov
 */

public class EmailWindow extends JFrame {

	private Settings settings;
	//For the First Panel
	private JPanel firstPanel;

	private JPanel studentPanel;
	private JPanel studentButtonPanel;
	private JPanel headerFooterPanel;
	private JPanel bottomButtonPanel;

	private StudentRecords sr;
	private CheckBoxList studentCheck;

	private JButton selectAll;
	private JButton selectNone;
	private JButton next;

	private Student[] studentsToEmail;
	private ArrayList<Student> studentArray;
	private Student[] selectedStudents;

	private JLabel headerLabel;
	private JLabel footerLabel;

	private JTextArea headerArea;
	private JTextArea footerArea;

	private String headerText;
	private String footerText;

	//For the second panel
	private JPanel secondPanel;
	private JPanel previewPanel;
	private JPanel prevSendPanel;

	private JButton send;
	private JButton prev;

	private JLabel header;
	private JLabel footer;
	private JLabel examMarks;

	//Authorisation
	private String emailFrom;
	private String password;

	private int auth;

	/**
	 * Constructs the Email Window using a current student records database and the current .properties file
	 * @param sr The current Students on Record
	 * @param set the current settings stored in the .properties file
	 */
	public EmailWindow(StudentRecords sr, Settings set) {
		super("Send Email");
		this.sr = sr;
		createFirstPanel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(540,300);
		setResizable(false);
		setVisible(true);
		settings = set;
	}

	/**
	 * Constructs the first panel of the Emailwindow. 
	 */
	public void createFirstPanel() {
		firstPanel = new JPanel();
		firstPanel.setLayout(new BorderLayout());
		createStudentPanel();
		createTextPanel();
		createNextPanel();
		this.add(firstPanel);
	}

	/**
	 * Creates the CheckBoxList of Students and places into ScrollPane.
	 * Implements API from  
	 * <a href="http://www.jidesoft.com/javadoc/com/jidesoft/swing/CheckBoxList.html">Jidesoft API</a>.
	 */
	public void createStudentPanel() {
		studentPanel = new JPanel();
		studentPanel.setLayout(new BorderLayout());

		studentButtonPanel = new JPanel(new BorderLayout());

		studentsToEmail = new Student[sr.numOfStudents()];
		studentArray = new ArrayList<Student>(sr.returnStudents().values());

		for(int i = 0; i < sr.numOfStudents(); i++) {
			studentsToEmail[i] = studentArray.get(i);
		}

		studentCheck = new CheckBoxList(studentsToEmail);
		JScrollPane studentScroll = new JScrollPane(studentCheck);

		selectAll = new JButton("Select All");
		selectNone = new JButton("Select None");

		studentButtonPanel.add(selectAll, BorderLayout.WEST);
		studentButtonPanel.add(selectNone, BorderLayout.EAST);

		selectAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				studentCheck.selectAll();
			}
		});

		selectNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentCheck.selectNone();
			}
		});

		studentPanel.add(studentScroll, BorderLayout.CENTER);
		studentPanel.add(studentButtonPanel, BorderLayout.NORTH);
		firstPanel.add(studentPanel, BorderLayout.WEST);
	}

	/**
	 * Creates a Panel containing a Header and Footer Text Areas, with their respective JLabels
	 */
	public void createTextPanel() {
		headerFooterPanel = new JPanel();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{354, 0};
		gridBagLayout.rowHeights = new int[]{47, 79, 41, 73, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		headerFooterPanel.setLayout(gridBagLayout);

		JLabel lblHeader = new JLabel("Header: ");
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.insets = new Insets(0, 0, 5, 0);
		gbc_lblHeader.gridx = 0;
		gbc_lblHeader.gridy = 0;
		headerFooterPanel.add(lblHeader, gbc_lblHeader);

		headerArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		headerFooterPanel.add(headerArea, gbc_textArea);

		JLabel lblFooter = new JLabel("Footer: ");
		GridBagConstraints gbc_lblFooter = new GridBagConstraints();
		gbc_lblFooter.insets = new Insets(0, 0, 5, 0);
		gbc_lblFooter.gridx = 0;
		gbc_lblFooter.gridy = 2;
		headerFooterPanel.add(lblFooter, gbc_lblFooter);

		footerArea = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 3;
		headerFooterPanel.add(footerArea, gbc_textArea_1);

		firstPanel.add(headerFooterPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates a panel containing the "Next" Button.
	 * Implements an actionListener which on (Next) 
	 * click changes the Panel to the Second Panel
	 */
	public void createNextPanel() {
		bottomButtonPanel = new JPanel();
		next = new JButton("Next");
		headerText = "";
		footerText = "";
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headerText = headerArea.getText();
				footerText = footerArea.getText();
				int size = studentCheck.getCheckBoxListSelectedValues().length;
				Object[] studentObj = studentCheck.getCheckBoxListSelectedValues();
				selectedStudents = new Student[size];
				for(int i = 0; i<size; i++) {
					selectedStudents[i] = (Student) studentObj[i];
				}
				remove(firstPanel);
				createSecondPanel();
			}
		});
		bottomButtonPanel.add(next);
		firstPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Creates the Second Panel to be opened on (Next) click
	 */
	public void createSecondPanel() {
		secondPanel = new JPanel();
		secondPanel.setLayout(new BorderLayout());
		createPreview();
		createPrevSend();
		prevSendPanel = new JPanel();
		this.add(secondPanel);
		pack();
	}

	/**
	 * Creates the panel containing the preview of the email to be sent. 
	 * Takes all results currently on database for the students selected
	 * and adds to an overall preview panel
	 */
	public void createPreview() {
		previewPanel = new JPanel();
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));

		JLabel previewLabel = new JLabel("This is the preview of the e-mail to be sent: ");
		header = new JLabel(headerText);
		footer = new JLabel(footerText);
		examMarks = new JLabel();
		Student stu = selectedStudents[0];
		Collection<Result> results = stu.getAllResults();
		examMarks.setText(createPreviewText(results));
		previewPanel.add(previewLabel);
		previewPanel.add(Box.createVerticalStrut(25));
		previewPanel.add(header);
		previewPanel.add(examMarks);
		previewPanel.add(footer);
		secondPanel.add(previewPanel, BorderLayout.WEST);
	}

	/**
	 * Creates a new Email Progress Monitor with a Background Thread that takes care of sending emails
	 * and updating the Progress Monitor. 
	 */
	public void doEmailMonitor() {
		final ProgressMonitor emailMonitor = new ProgressMonitor(EmailWindow.this, "Sending emails...", "", 0, 100);
		new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				int sleepTime = 3000;
				boolean interrupt = false;
				double completion = 0;
				double studentsize = (double) selectedStudents.length;
				int completed = (int) (completion);
				while(!interrupt) {
					for(double i = 0.0; i < studentsize; i++) {
						try {
							emailMonitor.setNote("Completed...." + completed + "%");
							emailMonitor.setProgress(completed);
						if (emailMonitor.isCanceled()) {	
							emailMonitor.close();
							interrupt = true;
							break;		
						}
						String email = selectedStudents[(int) i].getEmail();
						String textToSend = createEmailText(selectedStudents[(int) i].getAllResults(), headerText, footerText);
						EmailSend send = new EmailSend(email, emailFrom, password, textToSend, auth, settings);
						completion = (i+1.0)/studentsize*100;
						completed = (int) (completion);
						Thread.sleep(sleepTime);
						}
						catch(InterruptedException e) { interrupt = true;}
					}
					emailMonitor.close();
					interrupt = true;
				}
				return null;
			}
	 	   // this is called when the SwingWorker's doInBackground finishes
			protected void done() {
				emailMonitor.close();
			};
		}.execute();
	}
	/**
	 * Creates the Previous and Send button panels.
	 * Creates a Progress Monitor and a new Thread to monitor
	 * progress of the Emails being sent if "SEND" is pressed.
	 * On (Previous) click, opens the previous panel.
	 */
	public void createPrevSend(){
		prevSendPanel = new JPanel();
		prev = new JButton("Previous");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(secondPanel);
				createFirstPanel();
				pack();
			}
		});
		send = new JButton("Send");

		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				auth = getAuth();
				doEmailMonitor();	
			}
		});

		prevSendPanel.add(prev);
		prevSendPanel.add(send);
		secondPanel.add(prevSendPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Creates the preview text using a string builder, a student's results and basic HTML text.
	 * @param results The first student's collection of results
	 * @return The String containing a preview
	 */
	public String createPreviewText(Collection<Result> results){
		StringBuilder previewString = new StringBuilder();
		String outputString;
		previewString.append("<html>");
		previewString.append("Assessment&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + " Mark " + " Grade<br>");
		for(Result r: results) {
			previewString.append(r.module + "-" + r.assessment + " " + r.mark + " " + r.grade + "<br>");
		}
		previewString.append("</html>");
		outputString = previewString.toString();
		return outputString;
	}

	/**
	 * Creates the text to be sent in the email by combining the header, footer and results of a certain student.
	 * @param results The collection of results of a student.
	 * @param header The text to come before the results taken from the Header Text Area.
	 * @param footer The text to come after the results, taken from the Footer Text Area.
	 * @return The Text String for the Email Message.
	 */
	public String createEmailText(Collection<Result> results, String header, String footer) {
		StringBuilder previewString = new StringBuilder();
		String outputString;
		String newLine = System.lineSeparator();
		previewString.append(header);
		previewString.append(newLine);
		previewString.append("Assessment\t" + "Mark\t" + "Grade");
		previewString.append(newLine);	
		for(Result r: results) {
			previewString.append(r.module + "-" + r.assessment + "\t" + r.mark + "\t" + r.grade);
			previewString.append(newLine);
		}
		previewString.append(footer);
		outputString = previewString.toString();
		return outputString;
	}

	/**
	 * Prompts the user for a username and password and saves as a variable.
	 * Returns the option selected (0 = OK (UserName+ Password saved), 1 = CANCEL)
	 * @return the option selected (0 = OK (UserName+ Password saved), 1 = CANCEL)
	 */
	private int getAuth() {
		JPanel panel = new JPanel();
		JLabel nameLabel = new JLabel("Email: ");
		JLabel passLabel = new JLabel("Password: ");
		JTextField text = new JTextField(settings.get("username"));
		JPasswordField passwd = new JPasswordField(15);

		panel.setLayout(new GridLayout(2, 2));

		panel.add(nameLabel); panel.add(text);
		panel.add(passLabel); panel.add(passwd);

		String[] options = new String[]{"OK", "Cancel"};

		int option = JOptionPane.showOptionDialog(null, panel, "Enter info",
				JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);

		if (option == 0) {
			emailFrom = text.getText();
			password = new String(passwd.getPassword());
		}

		return option;
	}
}
