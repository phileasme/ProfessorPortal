package records;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

//import records.Result;

/**
 * Storage for instances of Result, all of which are from the same module from
 * the same assessment.
 * 
 * @author Max Karasinski
 *
 */
public class Assessment {

	private List<Result> results = new ArrayList<Result>();
	
	/**
	 * Create an empty Assessment with an initial capacity of ten.
	 */
	public Assessment() {}
	
	/**
	 * Append a result to the list for this module/assessment group.
	 * 
	 * @param result the result to be added to the list
	 */
	public void addResult(Result result) {
		results.add(result);
	}
	
	/**
	 * Returns the number of results in the list.
	 * 
	 * @return the number of Result instances in the list
	 */
	public int size() {
		return results.size();
	}
	
	public ListIterator<Result> listIterator() {
		return results.listIterator();
	}
	
	public String toString() {
		if (size() > 0) {
			return results.get(0).module + "-" + results.get(0).assessment;
		} else {
			return "Empty assessment";
		}
	}
}
