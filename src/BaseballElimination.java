import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private int numberOfTeams;
    private HashMap<String, Integer> index; 
    private int[][] g;
    private int[] w,l,r;
    public BaseballElimination(String filename) {
        In file = new In(filename);
        int numberOfTeams = file.readInt();
        index = new HashMap<>();
        g = new int[numberOfTeams][numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        for(int i=0;i<numberOfTeams;i++) {
            index.put(file.readString(), i);
            w[i] = file.readInt();
            l[i] = file.readInt();
            r[i] = file.readInt();
            for(int j=0;j<numberOfTeams;j++) {
                g[i][j] = file.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return index.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if(team==null || !index.containsKey(team)) error();
        return w[index.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if(team==null || !index.containsKey(team)) error();
        return l[index.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if(team==null || !index.containsKey(team)) error();
        return r[index.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if(team1==null || !index.containsKey(team1)) error();
        if(team2==null || !index.containsKey(team2)) error();
        return g[index.get(team1)][index.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        return new ArrayList<String>();
    }
    
    private void error() {
        throw new IllegalArgumentException();
    }
}