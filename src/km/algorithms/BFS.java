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
        // Liczba miast do odwiedzenia
        int citiesCount = problem.getCitiesCount();

        // Kolejka FIFO do przeszukiwania wszerz
        Queue<Node> queue = new Queue<>();
        // Dodaj początkowy węzeł (startujemy w mieście 0)
        queue.enqueue(new Node(0, new ArrayList<>(List.of(0)), 0));

        // Zmienna przechowująca najlepszą znalezioną ścieżkę
        List<Integer> bestPath = null;
        // Najniższy koszt dla znalezionej trasy
        int bestDistance = Integer.MAX_VALUE;

        // Pętla przetwarzająca węzły z kolejki
        while (!queue.isEmpty()) {
            // Pobierz pierwszy węzeł z kolejki
            Node current = queue.dequeue();

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

                            // Dodaj nowy węzeł do kolejki
                            queue.enqueue(new Node(nextCity, nextPath, nextCost));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
}
