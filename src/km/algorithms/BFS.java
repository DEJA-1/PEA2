package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;

import java.util.*;

public class BFS extends Algorithm {
    private final TSPProblem problem;

    public BFS(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Jeśli odwiedzono wszystkie miasta, dodaj koszt powrotu do miasta początkowego (0)
            if (current.path.size() == citiesCount) {
                int totalDistance = current.cost + problem.getDistance(current.city, 0);
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(0); // Zamknięcie cyklu
                }
            } else {
                // Generowanie sąsiednich węzłów
                for (int nextCity = 0; nextCity < citiesCount; nextCity++) {
                    if (!current.path.contains(nextCity)) {
                        int nextCost = current.cost + problem.getDistance(current.city, nextCity);

                        // Wycinanie nieopłacalnych ścieżek
                        if (nextCost < bestDistance) {
                            List<Integer> nextPath = new ArrayList<>(current.path);
                            nextPath.add(nextCity);
                            queue.add(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
