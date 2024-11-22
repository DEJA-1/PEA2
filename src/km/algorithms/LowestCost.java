package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;

import java.util.*;

public class LowestCost extends Algorithm {
    private final TSPProblem problem;

    public LowestCost(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));

        priorityQueue.add(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.path.size() == citiesCount) {
                // Obliczamy całkowity koszt dla pełnej ścieżki
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

                        // Dodajemy do kolejki tylko, jeśli koszt jest mniejszy od najlepszego dotychczasowego rozwiązania
                        if (nextCost < bestDistance) { // Pruning
                            List<Integer> nextPath = new ArrayList<>(current.path);
                            nextPath.add(nextCity);
                            priorityQueue.add(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
