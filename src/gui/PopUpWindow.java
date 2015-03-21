package gui;
import java.util.Collection;
import java.util.TreeMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import records.Student;
import records.Result;
import records.Logs;

/**
 * Class to create a Pop Up Window containing the student details.
 * 
 * @author Nikita Vorontsov
 * @author Phileas Hocquard
 * @author Max Karasinski
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
	 * Creates a frame displaying the student's contact information, exam
	 * results (if loaded) and participation data (if loaded).
	 * 
	 * @param s a {@link Student} object
	 */
	public PopUpWindow(Student s) {
		stu = s;
		studentName = stu.getName();
		studentEmail = stu.getEmail();
		studentNumber = stu.getNumber();
		studentTutor = stu.getTutorEmail();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		//New JPanel using GridBagConstraints to set Labels in
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//Creates student name label using the student's name
		JLabel studentNameLabel = new JLabel(studentName);
		studentNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(studentNameLabel, c);

		//Creates students email label using the student's email
		JLabel studentEmailLabel = new JLabel(studentEmail);
		studentEmailLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.05;
		panel.add(studentEmailLabel, c);

		//Creates label: "Student No: "
		JLabel lblStudentNo = new JLabel("Student No: ");
		lblStudentNo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(lblStudentNo, c);

		//Creates a student's ID Number label
		JLabel studentNumberLabel = new JLabel(studentNumber);
		studentNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		panel.add(studentNumberLabel, c);

		//Creates a "Tutor: " label
		JLabel lblTutor = new JLabel("Tutor: ");
		lblTutor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.05;
		panel.add(lblTutor, c);

		//Creates a tutor's email label
		JLabel tutorEmailLabel = new JLabel(studentTutor);
		tutorEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.001;
		panel.add(tutorEmailLabel, c);

		// panel where exam results and participation data tables will go
		JPanel dataPanel = new JPanel(new BorderLayout());

		// make a table with all of the student's results
		JScrollPane scrollResults;
		Collection<Result> results = stu.getAllResults();

		if (!results.isEmpty()) {
			String[] columns = {"Assessment", "Mark", "Grade"};
			Object[][] data = new Object[results.size()][3];
			int i = 0;

			for (Result r : results) {
				data[i][0] = r.module + "-" + r.assessment;
				data[i][1] = r.mark;
				data[i][2] = r.grade;
				++i;
			}

			JTable resultTable = new JTable(new ResultsTableModel(data, columns));
			resultTable.setBackground(new Color(230, 230, 230));
			scrollResults = new JScrollPane(resultTable);
		} else {
			JLabel noResults = new JLabel(String.format("%67s", "No results to display"));
			scrollResults = new JScrollPane(noResults);
		}

		scrollResults.setPreferredSize(new Dimension(500,120));
		dataPanel.add(scrollResults, BorderLayout.NORTH);

		// make a table with participation data
		JScrollPane scrollData;
		TreeMap<String, String> partData = Logs.getStudentData(studentEmail);
		if (partData != null) {
			
			String[] columns = {"Module", "Time last accessed"};
			Object[][] data = new Object[partData.size()][2];
			int i = 0;

			for (String module : partData.keySet()) {
				data[i][0] = module;
				data[i][1] = partData.get(module);
				i++;
			}

			JTable dataTable = new JTable(new ResultsTableModel(data, columns));
			dataTable.setBackground(new Color(230, 230, 230));
			scrollData = new JScrollPane(dataTable);
		} else {
			JLabel noParticipation = new JLabel(String.format("%66s", "No participation data to show"));
			scrollData = new JScrollPane(noParticipation);
		}

		scrollData.setPreferredSize(new Dimension(500, 100));
		dataPanel.add(scrollData, BorderLayout.SOUTH);
		
		add(dataPanel, BorderLayout.SOUTH);

		setSize(540, 480);
	}
}

