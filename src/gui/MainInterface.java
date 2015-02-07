package gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
public class MainInterface {

	public static void main (String[] args){
JFrame window = new JFrame();
window.setTitle ("Pra Coursework - MNP");
window.setSize(500,400);
window.setVisible(true);
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

JMenuBar MenuBar =new JMenuBar();
window.setJMenuBar(MenuBar);

	}
}
