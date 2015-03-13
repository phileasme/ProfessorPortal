package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import records.Student;
import records.StudentRecords;

public class EmailWindow extends JFrame {

	private JPanel studentPanel;
	private JPanel studentButtonPanel;
	private JPanel headerFooterPanel;
	private JPanel bottomButtonPanel;
	
	private StudentRecords sr;
	
	private JButton selectAll;
	private JButton selectNone;
	
	private Map<JCheckBox, Student> studentCheck;
	private ArrayList<Student> studentsToEmail;
	private JCheckBox student;
	
	public EmailWindow(StudentRecords sr) {
		super("Send Email");
		this.sr = sr;
		setLayout(new BorderLayout());
		createStudentPanel();
		createTextPanel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(540,300);
		setResizable(false);
		setVisible(true);
	}
	
	public void createStudentPanel() {
		studentPanel = new JPanel();
		studentPanel.setLayout(new BorderLayout());
		
		studentButtonPanel = new JPanel(new BorderLayout());
		
		selectAll = new JButton("Select All");
		selectNone = new JButton("Select None");
		
		studentButtonPanel.add(selectAll, BorderLayout.WEST);
		studentButtonPanel.add(selectNone, BorderLayout.EAST);
		
		studentsToEmail = new ArrayList<Student>();
		studentCheck = new HashMap<JCheckBox, Student>();
		
		Box box = Box.createVerticalBox();
		
		for(Student st: sr.returnStudents().values()) {
			student = new JCheckBox(st.toString());
			studentCheck.put(student, st);
			student.addActionListener(new CheckListener());
			box.add(student);
		}
		
		
		JScrollPane studentScroll = new JScrollPane(box);
		studentPanel.add(studentScroll, BorderLayout.CENTER);
		studentPanel.add(studentButtonPanel, BorderLayout.NORTH);
		this.add(studentPanel, BorderLayout.WEST);
	}
	
	public void createTextPanel() {
		
	}
	
	class CheckListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox check = (JCheckBox) e.getSource();
			if(check.isSelected() == true) {
				studentsToEmail.remove(check);
				check.setSelected(false);
			} else {
			check.setSelected(true);
			studentsToEmail.add(studentCheck.get(check));
			}
			for(Student st: studentsToEmail) {
				System.out.println(st.toString());
			}
		}
	}
}
