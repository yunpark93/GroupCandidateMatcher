import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Matching Program Class
 * @author Yun Park
 * @copyright September 2013
 */
public class MatchingProgram {
	
	static int ROUNDS_PER_FINALIZATION = 0;
	
	public static void main(String[] args) throws IOException {
		
		if (args.length < 4 || args.length > 5) {
			System.out.println("Error, usage: java ClassName GroupFile CandidateFile GroupPreferenceFile OutputFile (RoundsPerFinalization)");
			System.exit(1);
		}
		
		if (args.length == 5) {
			try {
				ROUNDS_PER_FINALIZATION = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				System.out.println("Error, NumberFormatException: not valid integer for rounds per finalization");
				System.exit(1);
			}
		}
		
		// PARSE GROUPS_FILE
		Scanner reader = new Scanner(new FileInputStream(args[0]));
		// Get number of groups total
		int numberOfGroups = 0;
		while (reader.hasNextLine()) {
			String currentLine = reader.nextLine().trim();
			if (currentLine.length() > 0) {
				numberOfGroups++;
			}
		}
		
		Group[] groups = new Group[numberOfGroups];
		HashMap<String, Group> groupSet = new HashMap<String, Group>();
		
		reader = new Scanner(new FileInputStream(args[0]));
		int currentGroupIndex = 0;
		// group_name, maxSize
		while (reader.hasNextLine()) {
			String currentLine = reader.nextLine().trim();
			if (currentLine.length() > 0) {
				String[] current = currentLine.split(",");
				if (current.length != 2) {
					System.out.println("Error, GroupFile Format: group_name, maxSize");
					System.out.println(currentLine);
					System.exit(1);
				}
				int maxSize = 0;
				try {
					maxSize = Integer.parseInt(current[1].trim());
				} catch (NumberFormatException e) {
					System.out.println("Error, GroupFile Format: second argument is not an integer");
					System.out.println(currentLine);
					System.exit(1);
				}
				String name = current[0].trim();
				if (groupSet.containsKey(name)) {
					System.out.println("Error, GroupFile Format: duplicate name found");
					System.out.println(currentLine);
					System.exit(1);
				}
				Group currentGroup = new Group(name, currentGroupIndex + 1, maxSize, null);
				groups[currentGroupIndex] = currentGroup;
				groupSet.put(name, currentGroup);
				currentGroupIndex++;
			}
		}
		
//		System.out.println("Number Of Groups: " + numberOfGroups);
//		for (int i = 0; i < numberOfGroups; i++) {
//			System.out.println(groups[i]);
//		}
//		System.out.println(); // Blank Space
		
		// PARSE CANDIDATES_FILE
		reader = new Scanner(new FileInputStream(args[1]));
		int numberOfCandidates = 0;
		while (reader.hasNextLine()) {
			String currentLine = reader.nextLine().trim();
			if (currentLine.length() > 0) {
				numberOfCandidates++;
			}
		}
		
		Candidate[] candidates = new Candidate[numberOfCandidates];
		HashMap<String, Candidate> candidateSet = new HashMap<String, Candidate>();
		
		reader = new Scanner(new FileInputStream(args[1]));
		int currentCandidateIndex = 0;
		while (reader.hasNextLine()) {
			String currentLine = reader.nextLine().trim();
			if (currentLine.length() > 0) {
				String[] current = currentLine.split(",");
				if (current.length < 2) {
					System.out.println("Error, CandidatesFile Format: candidate_name, group_name1, group_name2, ...");
					System.out.println(currentLine);
					System.exit(1);
				}
				String name = current[0].trim();
				if (candidateSet.containsKey(name)) {
					System.out.println("Error, CandidatesFile Format: duplicate name found");
					System.out.println(currentLine);
					System.exit(1);
				}
				int[] ranking = new int[current.length - 1];
				for (int i = 0; i < ranking.length; i++) {
					if (!groupSet.containsKey(current[i + 1].trim())) {
						System.out.println("Error, CandidatesFile Format: unknown group name found: " + current[i + 1].trim());
						System.out.println(currentLine);
						System.exit(1);
					}
					ranking[i] = groupSet.get(current[i + 1].trim()).groupId;
				}
				Candidate currentCandidate = new Candidate(name, currentCandidateIndex + 1, ranking);
				candidates[currentCandidateIndex] = currentCandidate;
				candidateSet.put(name, currentCandidate);
				currentCandidateIndex++;
			}
		}
		
//		System.out.println("Number Of Candidates: " + numberOfCandidates);
//		for (int i = 0; i < numberOfCandidates; i++) {
//			System.out.println(candidates[i]);
//		}
//		System.out.println(); // Blank Space
		
		// PARSE GROUP_PREFERENCE_FILE
		HashSet<String> groupPreferenceSet = new HashSet<String>();
		reader = new Scanner(new FileInputStream(args[2]));
		while (reader.hasNextLine()) {
			String currentLine = reader.nextLine().trim();
			if (currentLine.length() > 0) {
				String[] current = currentLine.split(",");
				if (current.length < 2) {
					System.out.println("Error, GroupPreferenceFile Format: group_name, candidate_name1, candidate_name2, ...");
					System.out.println(currentLine);
					System.exit(1);
				}
				String name = current[0].trim();
				if (groupPreferenceSet.contains(name)) {
					System.out.println("Error, GroupPreferenceFile Format: duplicate name found");
					System.out.println(currentLine);
					System.exit(1);
				}
				int[] preference = new int[current.length - 1];
				for (int i = 0; i < preference.length; i++) {
					if (!candidateSet.containsKey(current[i + 1].trim())) {
						System.out.println("Error, GroupPreferenceFile Format: unknown candidate name found: " + current[i + 1].trim());
						System.out.println(currentLine);
						System.exit(1);
					}
					preference[i] = candidateSet.get(current[i + 1].trim()).id;
				}
				Group currentGroup = groupSet.get(name);
				currentGroup.setPreference(preference);
				groupPreferenceSet.add(name);
			}
		}
		
		reader.close();
		
//		System.out.println("--");
//		for (int i = 0; i < numberOfGroups; i++) {
//			Group group = groups[i];
//			System.out.println(group);
//			System.out.println("--");
//		}
//		System.out.println(); // Blank Space

		Matcher matcher = new Matcher(groups, candidates, ROUNDS_PER_FINALIZATION);
		matcher.match();

//		for (int i = 0; i < numberOfGroups; i++) {
//			Group group = groups[i];
//			System.out.print(group.name + " ");
//			for (int j = 0; j < group.maxSize; j++) {
//				if (group.groupMembers[j] != 0) {
//					System.out.print(group.groupMembers[j] + " ");
//				}
//			}
//			System.out.println();
//		}

		FileWriter f = new FileWriter(new File(args[3]));
		for (int i = 0; i < numberOfGroups; i++) {
			Group group = groups[i];
			f.write(group.name + ": ");
			for (int j = 0; j < group.groupMembers.length; j++) {
				if (group.groupMembers[j] > 0) {
					f.write(candidates[group.groupMembers[j] - 1].name);
					if (j != group.groupMembers.length - 1) {
						f.write(", ");
					}
				}
			}
			f.write("\n");
		}
		f.close();
	}
}
