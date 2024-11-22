package km.ui;

public class ProgressIndicator {
    private final int totalIterations;
    private int currentIteration;

    public ProgressIndicator(int totalIterations) {
        this.totalIterations = totalIterations;
        this.currentIteration = 1;
    }

    public void updateProgress() {
        currentIteration++;
    }

    public double getProgress() {
        return ((double) currentIteration / totalIterations) * 100; // Obliczmay stan progressu
    }
}
