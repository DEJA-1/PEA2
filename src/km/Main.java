package km;

import km.algorithms.*;
import km.data.CSVWriter;
import km.data.ConfigLoader;
import km.data.FileLoader;
import km.model.TSPProblem;
import km.ui.Display;
import km.ui.ProgressIndicator;
import km.utils.MemoryMeasurer;
import km.utils.TimeMeasurer;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConfigLoader configLoader = null;

        // Pobranie ścieżki pliku konfiguracyjnego
        while (configLoader == null) {
            System.out.println("Wprowadź ścieżkę pliku konfiguracyjnego: ");
            String configFilePath = scanner.nextLine();

            try {
                // Inicjalizacja Config Loadera
                configLoader = new ConfigLoader(configFilePath);
            } catch (IOException e) {
                // Jeśli ścieżka jest niepoprawna, wyświetl komunikat
                System.out.println("Błąd: Nieprawidłowa ścieżka pliku konfiguracyjnego lub plik nie istnieje. Spróbuj ponownie.");
            }
        }

        try {
            // Zczytywanie wartości z configu
            String[] problemSizes = configLoader.getProperty("problemSize").split("\\s+");  // Zmieniamy na listę wartości
            int executions = configLoader.getIntProperty("executions");
            int useInputFile = configLoader.getIntProperty("useInputFile");
            boolean showProgress = configLoader.getBooleanProperty("showProgress");
            boolean isSymmetric = configLoader.getBooleanProperty("isSymmetric");

            String bfsOutputFile = configLoader.getProperty("bfsOutputFile");
            String dfsOutputFile = configLoader.getProperty("dfsOutputFile");
            String lowestCostOutputFile = configLoader.getProperty("lowestCostOutputFile");

            String inputDataFile = configLoader.getProperty("inputData");

            // Inicjalizacja paska progresu, bierzemy pod uwagę wykonanie wszystkich algorytmów dla wszystkich rozmiarów problemu
            ProgressIndicator progressIndicator = new ProgressIndicator(problemSizes.length * executions * 3); // Liczymy wszystkie algorytmy

            // Inicjalizacja CSVWriterów
            CSVWriter bfsWriter = new CSVWriter();
            CSVWriter dfsWriter = new CSVWriter();
            CSVWriter lowestCostWriter = new CSVWriter();

            bfsWriter.setFilePath(bfsOutputFile);
            dfsWriter.setFilePath(dfsOutputFile);
            lowestCostWriter.setFilePath(lowestCostOutputFile);

            // Inicjalizacja całkowitego zajętego czasu wywołania n algorytmów w celu obliczenia średniej
            long bfsTotalTime = 0;
            long dfsTotalTime = 0;
            long lowestCostTotalTime = 0;
            long initialMemory = MemoryMeasurer.getUsedMemory();

            // Iteracja po wszystkich problemSize
            for (String problemSizeString : problemSizes) {
                int problemSize = Integer.parseInt(problemSizeString);
                int displayProblemSize = problemSize;

                bfsTotalTime = 0;
                dfsTotalTime = 0;
                lowestCostTotalTime = 0;

                /*
                    Jeżeli useInputFile = 0, to z każdą iteracją (po wykonaniu wszystkich 3 algorytmów na danej instancji) generujemy
                    nową, losową instancje problemu i wykonujemy algorytmy wraz z pomiarem czasu
                 */
                if (useInputFile == 0) {
                    for (int i = 0; i < executions; i++) {
                        TSPProblem problem = TSPProblem.generateRandomProblem(problemSize, isSymmetric);
                        bfsTotalTime += runAlgorithm("BFS", new BFS(problem), bfsWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                        dfsTotalTime += runAlgorithm("DFS", new DFS(problem), dfsWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                        lowestCostTotalTime += runAlgorithm("Lowest Cost", new LowestCost(problem), lowestCostWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                    }
                } else {
                    TSPProblem problem = initializeProblemFromFile(inputDataFile);
                    displayProblemSize = problem.getCitiesCount();
                    for (int i = 0; i < executions; i++) {
                        bfsTotalTime += runAlgorithm("BFS", new BFS(problem), bfsWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                        dfsTotalTime += runAlgorithm("DFS", new DFS(problem), dfsWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                        lowestCostTotalTime += runAlgorithm("Lowest Cost", new LowestCost(problem), lowestCostWriter, problem.getDistanceMatrix(), progressIndicator, problem, showProgress, i + 1);
                    }
                }

                // Wyświetlenie podsumowania po każdym problemSize
                Display.printSummarySeparator();
                Display.printProblemSize(displayProblemSize);
                Display.printSummary("BFS - Średni czas wykonania: " + (bfsTotalTime / executions) + " ns (" + (bfsTotalTime / executions / 1_000_000) + " ms)");
                Display.printSummary("DFS - Średni czas wykonania: " + (dfsTotalTime / executions) + " ns (" + (dfsTotalTime / executions / 1_000_000) + " ms)");
                Display.printSummary("Lowest Cost - Średni czas wykonania: " + (lowestCostTotalTime / executions) + " ns (" + (lowestCostTotalTime / executions / 1_000_000) + " ms)");
            }

            // Po zapisaniu wyników zamykamy pliki
            bfsWriter.close();
            dfsWriter.close();
            lowestCostWriter.close();

            // Wyświetlenie całkowitego zużycia pamięci
            Display.displayTotalMemoryUsage(initialMemory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TSPProblem initializeProblemFromFile(String inputDataFile) throws IOException {
        return new TSPProblem(FileLoader.loadMatrixFromFile(inputDataFile));
    }

    /* Odpalenie algorytmu. Zwracany jest czas wykonania algorytmu, który wyżej dodajemy do sumy. Aktualizujemy stan progressu
        po każdym wywołaniu oraz zapisujemy rezultat do pliku
    */
    public static long runAlgorithm(String algorithmName, Algorithm algorithm, CSVWriter csvWriter, int[][] matrix, ProgressIndicator progressIndicator, TSPProblem problem, boolean showProgress, int iteration) throws IOException {
        Display.printIterationSeparator(algorithmName, iteration, showProgress, progressIndicator.getProgress());

        List<Integer> solution = algorithm.solve();
        int totalDistance = calculateTotalDistance(solution, problem);
        long timeNano = TimeMeasurer.measureAlgorithmTime(algorithm);

        Display.displayRoute(solution);
        Display.displayDistance(totalDistance);
        Display.displayExecutionTime(timeNano);

        csvWriter.writeRecord(matrix.length, matrix, algorithmName, timeNano, timeNano / 1_000_000);
        progressIndicator.updateProgress();

        return timeNano;
    }

    /*
        Obliczanie całkowitej długości ścieżki na podstawie listy miast.
   */
    public static int calculateTotalDistance(List<Integer> cities, TSPProblem problem) {
        int distance = 0;
        for (int i = 0; i < cities.size() - 1; i++) {
            distance += problem.getDistance(cities.get(i), cities.get(i + 1));
        }
        // Dodanie kosztu powrotu z ostatniego miasta do początkowego
        int returnDist = problem.getDistance(cities.get(cities.size() - 1), cities.get(0));
        if (returnDist != -1) {
            distance += returnDist;
        }
        return distance;
    }



}
