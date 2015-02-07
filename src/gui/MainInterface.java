 package gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import records.Student;

/**
 * Class to create an interface containing Scrollable list of students
 * Student Search function and Information Pop Up window of each student
 * 
 * @author Nikita Vorontsov
 *
 */
public class MainInterface extends JFrame {
	
	Student test = new Student(10, "tutor@tutormail.com", "Test", "studentemail@mail.com");
	private DefaultListModel studentListModel;
	private JList studentList;
	
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
	 * Creates the scrollable list of students by name and ID Number
	 * 
	 */
	private void createStudentList(){
		setLayout(new BorderLayout());
		JPanel studentPanel = new JPanel();
		studentListModel = new DefaultListModel();
		studentListModel.addElement(test.getName()+ "(" + test.getNumber() + ")");
		studentList = new JList(studentListModel);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane studentScroll = new JScrollPane(studentList);
		studentPanel.add(studentScroll);
		add(studentScroll, BorderLayout.WEST);
		
	}
	
	/**
	 * Creates the MenuBar - Sets to Frame
	 */
	private void createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu();
		menuBar.add(menu);
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