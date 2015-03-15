package gui;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

/**
 * A panel which will replace the default component used in the tab of a tabbed
 * pane in {@link ResultsTabManager}.
 * 
 * @author Max Karasinski
 *
 */
public class ResultsTabPanel extends JPanel {

	private JButton closeButton;
	
	private ImageIcon hoverImg;
	private ImageIcon noHoverImg;
	
	/**
	 * Creates a new tab panel with links to images used for the "close tab" button.
	 * 
	 * @param tabName the string representation of an {@link records.Assessment}.
	 */
	public ResultsTabPanel(String tabName) {
		setOpaque(false);
		JLabel tabText = new JLabel(tabName);
		
		hoverImg = new ImageIcon("img" + System.getProperty("file.separator") + "close_tab_hover.png");
		noHoverImg = new ImageIcon("img" + System.getProperty("file.separator") + "close_tab_nohover.png");
		
		closeButton = new JButton(noHoverImg);
		closeButton.setBorder(null);
		closeButton.setPreferredSize(new Dimension(16, 16));
		closeButton.addMouseListener(new CloseButtonListener());
		
		add(tabText);
		add(closeButton);
	}
	
	/**
	 * Adds an ActionListener to the "close tab" button.
	 * 
	 * @param l a pre-defined ActionListener with instructions for closing a tab
	 */
	public void addActionListener(ActionListener l) {
		closeButton.addActionListener(l);
	}
	
	/**
	 * Turns the "close tab" button red when the mouse is over it; black 
	 * otherwise.
	 */
	class CloseButtonListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			closeButton.setIcon(hoverImg);
		}
		
		public void mouseExited(MouseEvent e) {
			closeButton.setIcon(noHoverImg);
		}
	}
}
