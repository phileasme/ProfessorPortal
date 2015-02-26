package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import records.Student;

/**
 * Class to create a Pop Up Window containing the student details
 * @author Nikita Vorontsov
 * @author Phileas Hocquard
 *
 */
public class PopUpWindow extends JFrame {

	private JPanel contentPane;
	private Student stu;
	private String studentName;
	private String studentEmail;
	private String studentNumber;
	private String studentTutor;
	private JScrollPane scrollResults;
	/**
	 * Create the frame using a parameter Student
	 * 
	 * @param s The student containing the details for Name, Number, Email, Tutor Email
	 */
	public PopUpWindow(Student s) {
		stu = s;
		studentName = stu.getName();
		studentEmail = stu.getEmail();
		studentNumber = stu.getNumber();
		studentTutor = stu.getTutorEmail();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(540,300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//New JPanel using GridBagConstraints to set Labels in
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		JPanel resultPanel = new JPanel();
		scrollResults = new JScrollPane(resultPanel);
		scrollResults.setPreferredSize(new Dimension(100,40));
		contentPane.add(scrollResults, BorderLayout.SOUTH);
		JLabel results = new JLabel();
		String oldAllResults = ""+stu.getAllResults();
		String newAllResults = ""+oldAllResults.substring(0,oldAllResults.length()-1)+" % ]";
		newAllResults =newAllResults.replace(""+stu.getNumber(),"");
		newAllResults =newAllResults.replace(",","% , ");
		if(stu.getAllResults().isEmpty()){

			results = new JLabel("<html><h1 style='font-size:10px'>No exam results loaded.</h1></html>");
		}
		else{
			
			results = new JLabel("Results : "+newAllResults);
		}
		resultPanel.add(results,TOP_ALIGNMENT);
		
		
		JLabel studentNameLabel = new JLabel(studentName);
		studentNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(studentNameLabel, c);
		
		JLabel studentEmailLabel = new JLabel(studentEmail);
		studentEmailLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.05;
		panel.add(studentEmailLabel, c);
		
		JLabel lblStudentNo = new JLabel("Student No: ");
		lblStudentNo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(lblStudentNo, c);
		
		
		JLabel studentNumberLabel = new JLabel(studentNumber);
		studentNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(studentNumberLabel, c);
		
		JLabel lblTutor = new JLabel("Tutor: ");
		lblTutor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.05;
		panel.add(lblTutor, c);
		
		JLabel tutorEmailLabel = new JLabel(studentTutor);
		tutorEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.001;
		panel.add(tutorEmailLabel, c);
	}

}
