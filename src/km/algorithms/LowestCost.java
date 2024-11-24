package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;
import km.model.structures.PriorityQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LowestCost extends Algorithm {
    private final TSPProblem problem;

    public LowestCost(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        priorityQueue.enqueue(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.dequeue();

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
                            priorityQueue.enqueue(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
