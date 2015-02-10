package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.Font;
import records.*;

/**
 * Class to create a Pop Up Window containing the student details
 * @author Nikita Vorontsov
 * @author Max Karasinski
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel studentNameLabel = new JLabel(studentName);
		studentNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(studentNameLabel, "8, 4");
		
		JLabel studentEmailLabel = new JLabel(studentEmail);
		studentEmailLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel.add(studentEmailLabel, "8, 6");
		
		JLabel lblStudentNo = new JLabel("Student No: ");
		lblStudentNo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblStudentNo, "4, 10");
		
		JLabel studentNumberLabel = new JLabel(studentNumber);
		studentNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(studentNumberLabel, "8, 10");
		
		JLabel lblTutor = new JLabel("Tutor: ");
		lblTutor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblTutor, "4, 12");
		
		JLabel tutorEmailLabel = new JLabel(studentTutor);
		tutorEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(tutorEmailLabel, "8, 12");
	}

}
