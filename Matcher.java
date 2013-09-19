/**
 * Matcher Class
 * @author Yun Park
 * @copyright September 2013
 */
public class Matcher {
	
	Group[] groups;
	Candidate[] candidates;
	int roundsPerFinalization;
	
	/**
	 * Constructor for a Matcher Object
	 * @param groups
	 * @param candidates
	 */
	public Matcher(Group[] groups, Candidate[] candidates, int roundsPerFinalization) {
		this.groups = groups;
		this.candidates = candidates;
		this.roundsPerFinalization = roundsPerFinalization;
	}
	
	/**
	 * Runs the stable marriage algorithm.
	 */
	public void match() {

		// Find maximum size of a group preference
		int maximum = 0;
		for (int i = 0; i < groups.length; i++) {
			maximum = Math.max(maximum, groups[i].preference.length);
		}
		
		// Run for "maximum times the number of group members" period of time
		// This guarantees termination of algorithm.
		for (int i = 0; i < maximum * groups.length; i++) {

			// For each group
			for (int j = 0; j < groups.length; j++) {
				
				Group group = groups[j];
				
				if (!group.groupMembersFull() &&
						group.currentPreference < group.preference.length) {
					
					int id = group.preference[group.currentPreference];
					Candidate candidate = candidates[id - 1];
					
					if (candidate.groupId == 0) {
						candidate.groupId = group.groupId;
						group.addCandidate(candidate.id);
						group.currentPreference++;
					}
					else if (elementPrecedes(candidate.ranking, group.groupId, candidate.groupId) &&
							!candidate.finalized) {
						groups[candidate.groupId - 1].removeCandidate(candidate.id);
						candidate.groupId = group.groupId;
						group.addCandidate(candidate.id);
						group.currentPreference++;
					}
					else {
						group.currentPreference++;
					}
				}
			}
			
			if (roundsPerFinalization != 0) {
				if ((i + 1) % roundsPerFinalization == 0) {
					for (int k = 0; k < candidates.length; k++) {
						Candidate currentCandidate = candidates[k];
						if (currentCandidate.groupId != 0) {
							currentCandidate.finalized = true;
						}
					}
				}
			}
		}
		
		check();
	}
	
	/**
	 * Checks if elem1 precedes elem2 in the array; returns false if either element is not present in array
	 * @param array
	 * @param elem1
	 * @param elem2
	 * @return boolean
	 */
	private static boolean elementPrecedes(int[] array, int elem1, int elem2) {
		int index1 = -1;
		int index2 = -1;
		
		for (int i = 0; i < array.length; i++) {
			if (array[i] == elem1) {
				index1 = i;
			}
			if (array[i] == elem2) {
				index2 = i;
			}
		}

		if (index1 == -1 || index2 == -1) {
			return false;
		}

		if (index1 < index2) {
			return true;
		}
		return false;
	}
	/**
	 * Check if element is in the array
	 * @param array
	 * @param elem
	 * @return boolean
	 */
	private static boolean elementIn(int[] array, int elem) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == elem) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check to see if algorithm is stable.
	 * Print unstable pairs.
	 */
	private void check() {
		for (int i = 0; i < candidates.length; i++) {
			for (int j = 0; j < groups.length; j++) {
				Candidate candidate = candidates[i];
				Group group = groups[j];
				if (candidate.groupId != group.groupId &&
						!elementIn(group.groupMembers, candidate.id) &&
						elementIn(candidate.ranking, group.groupId) && 
						elementIn(group.preference, candidate.id)) {
					boolean rogueFlag = false;
					for (int k = 0; k < group.groupMembers.length; k++) {
						if (!elementPrecedes(candidate.ranking, candidate.groupId, group.groupId) &&
								group.groupMembers[k] != 0 &&
								!elementPrecedes(group.preference, group.groupMembers[k], candidate.id)) {
							rogueFlag = true;
						}
					}
					if (rogueFlag) {
						System.out.println("UNSTABLE COUPLE: " + group.name + ", " + candidate.name);
					}
				}
			}
		}
	}
}
