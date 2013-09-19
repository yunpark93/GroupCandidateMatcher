Group-Candidate Matching
Developed by: Yun Park
Copyright: September 2013

Use: java MatchingProgram GroupFile CandidateFile GroupPreferenceFile OutputFile [RoundsPerFinalization]

Arguments:
Group File - a .txt file with each line formatted as follows:
GroupName, MaximumNumberOfPeopleInThisGroup

Candidate File - a .txt file with each line formatted as follows:
CandidateName, GroupPreference1 [, GroupPreference2, GroupPreference3, ...]

Group Preference File - a .txt file with each line formatted as follows:
GroupName, CandidatePreference1 [, CandidatePreference2, CandidatePreference3, ...]

Output File - name of the file that will output in the following format:
GroupName, Candidate1, Candidate2, Candidate3, ...

Rounds Per Finalization - optional integer argument:
Number of rounds before fixing the current pairings

Output:
Along with the output file, the program will print any errors and any unstable pairs (if
RoundsPerFinalization is not equal to 0).

Algorithm:
Each iteration, each group asks a candidate to be part of their group based on their preference "IF" the
group is not full. If the candidate is unmatched, the two will be matched. If the candidate is matched,
the two will be matched if candidate prefers the group over the candidate's current group. This process
is repeated. If RoundsPerFinalization (k) > 0, then after every k iterations, the candidate is fixed to
the group that the candidate is matched to at the end of the k iterations.

Guarantees:
1) If RoundsPerFinalization is equal to 0 (default = 0), then the matching is stable (there is no
group-candidate pair in which the group prefers this candidate over any candidate that it is matched 
to AND the candidate prefers this group over the current group that it is matched to.).
2) If RoundsPerFinalization is equal to k > 0, then every k rounds, any pairing at that point is
fixed. So if the committee prefers a candidate in the k rounds and the candidate prefers that committee
over any other committee in that same k rounds, then the two are guaranteed to be paired together.

Note:
1) Make sure all candidate names match with candidates and all group names match with groups (output error).
2) The argument "RoundsPerFinalization" is a parameter that can be used to balance the influence of the
committee's preferences and the candidate's preferences. If RoundsPerFinalization is equal to 0 or k that
is large, then the algorithm values the candidate's preferences more. If RoundsPerFinalization > 0 but
small, then the algorithm values the committee's preferences more.
