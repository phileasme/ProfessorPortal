package gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import records.Student;
import records.StudentRecords;

/**
 * Class to create an interface containing Scrollable list of students
 * Student Search function and Information Pop Up window of each student
 * 
 * @author Phileas Hocquard
 * @author Nikita Vorontsov
 * @author Max Karasinski
 *
 */
public class MainInterface extends JFrame {
	
	private DefaultListModel<String> studentListModel;
	private JList<String> studentList;
	private StudentRecords sr = new StudentRecords();
	private JPanel studentPanel;
	private JScrollPane studentScroll;
	private JMenuBar menuBar;
	
	/**
	 * Constructor for the Interface, setting a size, visibility, exit function and creating the widgets
	 * 
	 */
	public MainInterface(){
		super("PRA Coursework - MNP");
		createStudentList();
		createMenuBar();
		setSize(500,400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Creates the scrollable list of students by name and ID Number from StudentRecords
	 * 
	 */
	private void createStudentList(){
		setLayout(new BorderLayout());
		studentPanel = new JPanel();
		
		studentListModel = new DefaultListModel<String>();

		//Adds all students from StudentRecords into the studentListModel
		for (Student student : sr.returnStudents().values()) {
			studentListModel.addElement(student.getName() + " (" + student.getNumber() + ")");
		}
				
		studentList = new JList<String>(studentListModel);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentScroll = new JScrollPane(studentList);
		studentPanel.add(studentScroll);
		add(studentScroll, BorderLayout.WEST);
		
	}
	/**
	 * Creates the MenuBar - Sets to Frame
	 */
	private void createMenuBar(){
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu data = new JMenu("Data");
		
		// menu items
		JMenuItem loadCodes = new JMenuItem("Load anonymous marking codes");
		JMenuItem loadResults = new JMenuItem("Load exam results");
		
		// add menus to bar, and items to menus
		menuBar.add(file);
		menuBar.add(data);
		
		file.add(loadCodes);
		file.add(loadResults);
		
		setJMenuBar(menuBar);
	}
		
	public static void main (String[] args){
		MainInterface mi = new MainInterface();
	}
}

/**
JButton buttonTest = new JButton("Test");
window.add(buttonTest);

class StudentPopUpListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(window, "Test Student Dialog");
	}
}

buttonTest.addActionListener(new StudentPopUpListener());
 */