import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
//        FlowNetwork fn = new FlowNetwork(null)
//        FordFulkerson ff = new FordFulkerson()
        In file = new In(filename);
        int n = file.readInt();
    }

    // number of teams
    public int numberOfTeams() {

    }

    // all teams
    public Iterable<String> teams() {

    }

    // number of wins for given team
    public int wins(String team) {

    }

    // number of losses for given team
    public int losses(String team) {

    }

    // number of remaining games for given team
    public int remaining(String team) {

    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {

    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

    }

    public Iterable<String> certificateOfElimination(String team) {

//        if (ids[0]<=ids[2] && ids[3]<=ids[1]) || (ids[0]>=ids[2] && ids[3]>=ids[1]) c++;
    }
}