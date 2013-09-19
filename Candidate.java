/**
 * Candidate Class
 * @author Yun Park
 * @copyright September 2013
 */
public class Candidate {

	// Name of the candidate
	String name;
	// Candidate ID
	int id;
	// Group ID
	int groupId;
	// Candidate's ranking of groups
	int[] ranking;
	// Finalized flag
	boolean finalized;
	
	/**
	 * Constructor for a Candidate Object.
	 * @param name
	 * @param id
	 * @param ranking
	 */
	public Candidate(String name, int id, int[] ranking) {
		this.name = name;
		this.id = id;
		this.groupId = 0;
		this.ranking = ranking;
		this.finalized = false;
	}
	
	/**
	 * toString() for debugging purposes.
	 */
	public String toString() {
		String debug = "Name: " + name + " id: " + id + " ranking: { ";
		for (int i = 0; i < ranking.length; i++) {
			debug += ranking[i] + " ";
		}
		debug += "}\n";
		return debug;
	}
	
}
