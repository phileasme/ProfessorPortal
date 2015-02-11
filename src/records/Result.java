package records;

/**
 * Represents the result achieved by an individual student for a specific
 * assessment in a specific module.
 * 
 * @author Max Karasinski
 *
 */
public class Result {
	/**
	 * The module code.
	 */
	public final String module;
	
	/**
	 * The assessment unit on the module (e.g. one for exam, one for CW).
	 */
	public final String assessment;
	
	/**
	 * The mark awarded.
	 */
	public final String mark;
	
	/**
	 * Note on result. Will be blank unless student was absent, withdrew, etc.
	 */
	public final String grade;
	
	/**
	 * The student's anonymous marking code, or number if available.
	 */
	private String candKey;
	
	public Result(String module, String assessment, String candKey, String mark, String grade) {
		this.module = module;
		this.assessment = assessment;
		this.candKey = candKey;
		this.mark = mark;
		this.grade = grade;
	}
	
	/**
	 * Allow the anonymised (for exam results) candidate key to be overwritten
	 * with the student's ID number.
	 * 
	 * @param key the new candidate key.
	 */
	public void setCandKey(String key) {
		candKey = key;
	}
	
	/**
	 * Returns the student's candidate key/ID number.
	 * 
	 * @return the student's candidate key/ID number
	 */
	public String getCandKey() {
		return candKey;
	}
	
	/**
	 * Returns a string identifying the module, and assessment within that
	 * module.
	 * 
	 * @return an identifying string
	 */
	public String getAssessment() {
		return module + " " + assessment;
	}
	
	public String toString() {
		return module + " " + assessment + " " + candKey + " " + mark;
	}
}
