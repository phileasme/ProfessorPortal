package gui;

import javax.swing.table.DefaultTableModel;


/**
 * Package-private table model that extends DefaultTableModel, with the sole
 * purpose of making a JTable that has cells which cannot be edited.
 * 
 * @see DefaultTableModel
 * 
 * @author Max Karasinski
 * 
 */
class ResultsTableModel extends DefaultTableModel {

	/**
	 * Constructs a DefaultTableModel and initialises the table by passing data
	 *  and columnNames to the setDataVector method.
	 *  
	 * @param data the data of the table
	 * @param columns - the names of the columns
	 */
	public ResultsTableModel(Object[][] data, String[] columns) {
		super(data, columns);
	}
	
	/**
	 * Returns false.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
