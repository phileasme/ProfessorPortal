package records;

import java.util.List;
import java.util.ArrayList;

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
}
