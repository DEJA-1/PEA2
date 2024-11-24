package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;
import km.model.structures.Queue;

import java.util.ArrayList;
import java.util.List;

public class BFS extends Algorithm {
    private final TSPProblem problem;

    public BFS(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();
        Queue<Node> queue = new Queue<>();
        queue.enqueue(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            Node current = queue.dequeue();

            if (current.path.size() == citiesCount) {
                int totalDistance = current.cost + problem.getDistance(current.city, 0);
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(0);
                }
            } else {
                for (int nextCity = 0; nextCity < citiesCount; nextCity++) {
                    if (!current.path.contains(nextCity)) {
                        int nextCost = current.cost + problem.getDistance(current.city, nextCity);
                        if (nextCost < bestDistance) {
                            List<Integer> nextPath = new ArrayList<>(current.path);
                            nextPath.add(nextCity);
                            queue.enqueue(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
