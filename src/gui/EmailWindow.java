package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import records.Student;
import records.StudentRecords;
import com.jidesoft.swing.CheckBoxList;

public class EmailWindow extends JFrame {

	private JPanel studentPanel;
	private JPanel studentButtonPanel;
	private JPanel headerFooterPanel;
	private JPanel bottomButtonPanel;
	
	private StudentRecords sr;
	
	private JButton selectAll;
	private JButton selectNone;
	
	private Student[] studentsToEmail;
	private ArrayList<Student> studentArray;
	
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
		
		studentsToEmail = new Student[sr.numOfStudents()];
		studentArray = new ArrayList<Student>(sr.returnStudents().values());
	
		for(int i = 0; i < sr.numOfStudents(); i++) {
			studentsToEmail[i] = studentArray.get(i);
		}
		
		CheckBoxList studentCheck = new CheckBoxList(studentsToEmail);
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
		this.add(studentPanel, BorderLayout.WEST);
	}
	
	public void createTextPanel() {
		
	}
	
}
