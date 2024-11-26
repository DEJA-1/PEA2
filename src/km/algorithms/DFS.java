package km.algorithms;

import km.model.Node;
import km.model.TSPProblem;
import km.model.structures.Stack;

import java.util.ArrayList;
import java.util.List;

public class DFS extends Algorithm {
    private final TSPProblem problem;

    public DFS(TSPProblem problem) {
        this.problem = problem;
    }

    @Override
    public List<Integer> solve() {
        int citiesCount = problem.getCitiesCount();

        // Stos LIFO do przeszukiwania wgłąb
        Stack<Node> stack = new Stack<>();
        // Dodaj początkowy węzeł (start w mieście 0)
        stack.push(new Node(0, new ArrayList<>(List.of(0)), 0));

        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        // Pętla przetwarzająca węzły na stosie
        while (!stack.isEmpty()) {
            // Pobierz wierzchołek stosu (ostatni dodany element)
            Node current = stack.pop();

            // Jeśli bieżący węzeł odwiedził wszystkie miasta
            if (current.path.size() == citiesCount) {
                // Oblicz całkowity koszt dodając powrót do miasta startowego (0)
                int totalDistance = current.cost + problem.getDistance(current.city, 0);

                // Jeśli nowa ścieżka ma niższy koszt, zaktualizuj wynik
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestPath = new ArrayList<>(current.path); // Skopiuj ścieżkę
                    bestPath.add(0); // Dodaj powrót do miasta początkowego
                }
            } else {
                // Generuj nowe węzły dla nieodwiedzonych miast
                for (int nextCity = 0; nextCity < citiesCount; nextCity++) {
                    // Sprawdź, czy miasto jeszcze nie zostało odwiedzone
                    if (!current.path.contains(nextCity)) {
                        // Oblicz koszt dotarcia do nowego miasta
                        int nextCost = current.cost + problem.getDistance(current.city, nextCity);

                        // Przycinanie (pruning): ignoruj ścieżki o większym koszcie niż najlepszy znaleziony
                        if (nextCost < bestDistance) {
                            // Utwórz nową ścieżkę, dodając nowe miasto
                            List<Integer> nextPath = new ArrayList<>(current.path);
                            nextPath.add(nextCity);

                            // Dodaj nowy węzeł do stosu
                            stack.push(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
