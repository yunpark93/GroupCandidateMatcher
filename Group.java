
public class Group {
	
	// Name of the group
	String name;
	// Group ID
	int groupId;
	// Maximum size of the group (just candidates)
	int maxSize;
	// Current list of candidates
	int[] groupMembers;
	// Current Preference pointer
	int currentPreference;
	// Preference of all possible candidates
	int[] preference;
	
	/**
	 * Constructor for a Group Object.
	 * @param name
	 * @param groupId
	 * @param maxSize
	 * @param preference
	 */
	public Group(String name, int groupId, int maxSize, int[] preference) {
		this.name = name;
		this.groupId = groupId;
		this.maxSize = maxSize;
		this.groupMembers = new int[maxSize];
		this.currentPreference = 0;
		this.preference = preference;
	}
	
	/**
	 * Sets preference array
	 * @param preference
	 */
	void setPreference(int[] preference) {
		this.preference = preference;
	}
	
	/**
	 * Returns true if group members array is full, false otherwise.
	 * @return boolean
	 */
	boolean groupMembersFull() {
		for (int i = 0; i < groupMembers.length; i++) {
			if (groupMembers[i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	int numberOfMembers() {
		int count = 0;
		for (int i = 0; i < groupMembers.length; i++) {
			if (groupMembers[i] != 0) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Adds the candidate to the members array, if possible.
	 * @param candidate
	 * @return true if candidate has been added, false otherwise.
	 */
	boolean addCandidate(int candidate) {
		for (int i = 0; i < groupMembers.length; i++) {
			if (groupMembers[i] == 0) {
				groupMembers[i] = candidate;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the candidate from the members array, if possible.
	 * @param candidate
	 * @return true if candidate has been removed, false otherwise.
	 */
	boolean removeCandidate(int candidate) {
		for (int i = 0; i < groupMembers.length; i++) {
			if (groupMembers[i] == candidate) {
				groupMembers[i] = 0;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * toString() for debugging purposes.
	 */
	public String toString() {
		String debug = "Group: " + name + " groupId: " + groupId + " preference: { ";
		if (preference != null) {
			for (int i = 0; i < preference.length; i++) {
				debug += preference[i] + " ";
			}
		}
		debug += "}\n";
		return debug;
	}
	
}
