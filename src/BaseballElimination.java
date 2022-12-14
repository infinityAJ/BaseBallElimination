import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private int numberOfTeams;
    private HashMap<String, Integer> index;
    private ArrayList<String> teams;
    private HashMap<Integer, ArrayList<Integer>> certificate;
    private int[][] g;
    private int[] w, l, r;
    private int flowSize;
    private int maxWin;
    private int[] eliminated;

    public BaseballElimination(String filename) {
        In file = new In(filename);
        numberOfTeams = file.readInt();
        index = new HashMap<>();
        teams = new ArrayList<>();
        certificate = new HashMap<>();
        g = new int[numberOfTeams][numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        eliminated = new int[numberOfTeams];
        flowSize = (numberOfTeams * (numberOfTeams - 1) / 2) + 2;
        maxWin = 0;
        for (int i = 0; i < numberOfTeams; i++) {
            String team = file.readString();
            index.put(team, i);
            teams.add(team);
            w[i] = file.readInt();
            l[i] = file.readInt();
            r[i] = file.readInt();
            maxWin = Math.max(maxWin, w[i]);
            for (int j = 0; j < numberOfTeams; j++) {
                g[i][j] = file.readInt();
            }
            eliminated[i] = -1;
        }
    }

    private FlowNetwork generateFlowNetworkOf(int x) {
        FlowNetwork fn = new FlowNetwork(flowSize);
        int t = 1, m = 0, n = 1, o = 0;
        int teamIndex = flowSize - numberOfTeams;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == x)
                continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == x)
                    continue;
                fn.addEdge(new FlowEdge(0, t, g[i][j]));
                fn.addEdge(new FlowEdge(t, teamIndex + m, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(t, teamIndex + n, Double.POSITIVE_INFINITY));
                n++;
                t++;
                if (n == numberOfTeams - 1) {
                    m++;
                    n = m + 1;
                }
            }
            fn.addEdge(new FlowEdge(o++ + teamIndex, flowSize - 1, w[x] + r[x] - w[i]));
        }
        return fn;
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !index.containsKey(team))
            error();
        return w[index.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !index.containsKey(team))
            error();
        return l[index.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !index.containsKey(team))
            error();
        return r[index.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || !index.containsKey(team1))
            error();
        if (team2 == null || !index.containsKey(team2))
            error();
        return g[index.get(team1)][index.get(team2)];
    }

    private void add(int x, int i) {
        if (certificate.get(x) == null)
            certificate.put(x, new ArrayList<>());
        certificate.get(x).add(i);
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !index.containsKey(team))
            error();
        int x = index.get(team);
        if (eliminated[x] != -1)
            return eliminated[x] == 1;
        if (w[x] + r[x] < maxWin) {
            eliminated[x] = 1;
            for (int i = 0; i < numberOfTeams; i++) {
                if (i == x)
                    continue;
                if (w[i] > w[x] + r[x])
                    add(x, i);
            }
            return true;
        } else {
            FlowNetwork fn = generateFlowNetworkOf(x);
            FordFulkerson ff = new FordFulkerson(fn, 0, flowSize - 1);
            int t = flowSize - numberOfTeams;
            for (int i = 0; i < numberOfTeams; i++) {
                if (i == x)
                    continue;
                if (ff.inCut(t++))
                    add(x, i);
            }
            eliminated[x] = certificate.get(x) == null ? 0 : 1;
            return eliminated[x] == 1;
        }
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !index.containsKey(team))
            error();
        if (!isEliminated(team))
            return null;
        ArrayList<String> ans = new ArrayList<>();
        for (int i : certificate.get(index.get(team)))
            ans.add(teams.get(i));
        return ans;
    }

    private void error() {
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
