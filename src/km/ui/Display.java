package km.ui;

import km.utils.MemoryMeasurer;

import java.util.List;

public class Display {

    public static void printIterationSeparator(String algorithmName, int iteration, boolean showProgress, double progressPercentage) {
        if (showProgress) {
            displayProgressBar(progressPercentage);
        } else {
            System.out.println("\n========================================");
        }
        System.out.printf("%s - Iteracja %d\n", algorithmName, iteration);
    }

    public static void displayProgressBar(double progressPercentage) {
        int totalBarLength = 20;
        int filledLength = (int) (progressPercentage / 5); // co 5% uzupełniamy pasek

        StringBuilder progressBar = new StringBuilder("\n[");
        for (int i = 0; i < filledLength; i++) {
            progressBar.append("=");
        }
        for (int i = filledLength; i < totalBarLength; i++) {
            progressBar.append(" ");
        }
        progressBar.append("] ");

        progressBar.append(String.format("%.0f%%", progressPercentage));
        progressBar.append("\n");

        System.out.println(progressBar);
    }

    public static void displayRoute(List<Integer> route) {
        System.out.println("Znaleziono trasę: " + route.toString());
    }

    public static void displayDistance(int distance) {
        System.out.println("Total Distance: " + distance);
    }

    public static void displayExecutionTime(long timeNano) {
        long timeMilli = timeNano / 1_000_000;
        System.out.println("Czas wykonania: " + timeNano + " ns (" + timeMilli + " ms)");
    }

    public static void printSummarySeparator() {
        System.out.println("\n=================== PODSUMOWANIE ===================");
    }

    public static void printSummary(String summary) {
        System.out.println(summary);
    }

    public static void printProblemSize(int size) {
        System.out.println("Rozmiar instancji: " + size);
    }


    public static void displayTotalMemoryUsage(long initialMemory) {
        long finalMemory = MemoryMeasurer.getUsedMemory();
        long totalMemoryUsed = finalMemory - initialMemory;
        System.out.println("Całkowita zajętość pamięci: " + totalMemoryUsed + " B");
    }
}




