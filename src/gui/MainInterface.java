package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
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
	
	private DefaultListModel<Student> studentListModel;
	private JList<Student> studentList;
	private StudentRecords sr = new StudentRecords();
	private JPanel studentPanel;
	private JPanel searchPanel;
	private JScrollPane studentScroll;
	private JMenuBar menuBar;
	private JTextField searchText;
	
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
		searchText = new JTextField(25);
		studentPanel.add(searchText);
		studentListModel = new DefaultListModel<Student>();

		//Adds all students from StudentRecords into the studentListModel
		
		for (Student student : sr.returnStudents().values()) {
			studentListModel.addElement(student);
		}


		studentList = new JList<Student>(studentListModel);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentScroll = new JScrollPane(studentList);
		add(studentScroll, BorderLayout.WEST);
	
		add(studentPanel, BorderLayout.NORTH);
		studentList.addMouseListener(new StudentPressListener());

		SearchFilterListener sfl = new SearchFilterListener();
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
		
	/**
	 * MouseListener to make a pop up window of the Student Information appear
	 */
	
	class StudentPressListener implements MouseListener{
		public void mousePressed(MouseEvent e) {
			int index;
			Student stu;
			index = studentList.getSelectedIndex();
			stu = studentListModel.elementAt(index);
			PopUpWindow studentInfo = new PopUpWindow(stu);
			studentInfo.setVisible(true);
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
   class SearchFilterListener {
	   
	   public SearchFilterListener(){
	   searchText.getDocument().addDocumentListener(new DocumentListener() {
	        
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            filteringList();
	            
	        }
	        
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	 filteringList();
	            
	        }
	        
	        @Override
	        public void changedUpdate(DocumentEvent e) {
	        	 filteringList();
	            
	        }
	        
	    });
	   }
   }
   
public void filteringList(){
	  if (searchText.getText().equals("")){
		  for (Student student : sr.returnStudents().values()) {
				studentListModel.addElement(student);
			}
    
	}
	  else if (searchText.getText().equalsIgnoreCase("Clear")){
		 
		  studentListModel.removeAllElements();
	}
    
	}


	
	public static void main (String[] args){
		MainInterface mi = new MainInterface();
	}
}