package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import records.Student;
import records.StudentRecords;

import com.jidesoft.swing.CheckBoxList;

public class EmailWindow extends JFrame {

	private JPanel studentPanel;
	private JPanel studentButtonPanel;
	private JPanel headerFooterPanel;
	private JPanel bottomButtonPanel;
	
	private StudentRecords sr;
	CheckBoxList studentCheck;
	
	private JButton selectAll;
	private JButton selectNone;
	private JButton next;
	
	private Student[] studentsToEmail;
	private ArrayList<Student> studentArray;
	
	private JLabel headerLabel;
	private JLabel footerLabel;
	
	private JTextArea headerArea;
	private JTextArea footerArea;
	
	public EmailWindow(StudentRecords sr) {
		super("Send Email");
		this.sr = sr;
		setLayout(new BorderLayout());
		createStudentPanel();
		createTextPanel();
		createNextPanel();
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
		this.add(studentPanel, BorderLayout.WEST);
	}
	
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
		
		JTextArea textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		headerFooterPanel.add(textArea, gbc_textArea);
		
		JLabel lblFooter = new JLabel("Footer: ");
		GridBagConstraints gbc_lblFooter = new GridBagConstraints();
		gbc_lblFooter.insets = new Insets(0, 0, 5, 0);
		gbc_lblFooter.gridx = 0;
		gbc_lblFooter.gridy = 2;
		headerFooterPanel.add(lblFooter, gbc_lblFooter);
		
		JTextArea textArea_1 = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 3;
		headerFooterPanel.add(textArea_1, gbc_textArea_1);

		this.add(headerFooterPanel, BorderLayout.CENTER);
	}
	
	public void createNextPanel() {
		bottomButtonPanel = new JPanel();
		next = new JButton("Next:");
		bottomButtonPanel.add(next);
		this.add(bottomButtonPanel, BorderLayout.SOUTH);
	}
	
}
