package km.utils;

import km.algorithms.Algorithm;

public class TimeMeasurer {

    public static long measureAlgorithmTime(Algorithm algorithm) {
        long startTime = System.nanoTime();
        algorithm.solve();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}

