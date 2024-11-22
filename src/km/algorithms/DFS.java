package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;

import java.util.*;

public class DFS extends Algorithm {
    private final TSPProblem problem;

    public DFS(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (current.path.size() == citiesCount) {
                int totalDistance = current.cost + problem.getDistance(current.city, 0);
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(0); // Zamknięcie cyklu
                }
            } else {
                for (int nextCity = 0; nextCity < citiesCount; nextCity++) {
                    if (!current.path.contains(nextCity)) {
                        int nextCost = current.cost + problem.getDistance(current.city, nextCity);
                        if (nextCost < bestDistance) { // Pruning
                            List<Integer> nextPath = new ArrayList<>(current.path);
                            nextPath.add(nextCity);
                            stack.push(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
