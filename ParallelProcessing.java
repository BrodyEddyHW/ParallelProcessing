import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ParallelProcessing {

    public static void main(String[] args) {
        // Larger Array size to increase workload
        int size = 10_000_000; 
        int[] largeArray = IntStream.range(0, size).toArray();

        // Test different parallelism levels
        int[] parallelismLevels = {1, 2, 4, 8, 16};
        
        for (int parallelism : parallelismLevels) {
            System.out.println("Parallelism Level: " + parallelism);

            // Measure time for parallel processing
            long startTime = System.nanoTime();
            processArrayWithParallelism(largeArray, parallelism, 10); // Repeat 10 times
            long endTime = System.nanoTime();

            double elapsedTime = (endTime - startTime) / 1.0e9; // Convert to seconds
            System.out.printf("Elapsed Time: %.3f seconds%n%n", elapsedTime);
        }
    }

    private static void processArrayWithParallelism(int[] array, int parallelism, int repetitions) {
        // Set the parallelism level
        ForkJoinPool customPool = new ForkJoinPool(parallelism);
        
        try {
            customPool.submit(() -> {
                for (int i = 0; i < repetitions; i++) {
                    // Perform a more computation-heavy task: sum of squares, logs, and trigonometric functions
                    IntStream.of(array)
                             .parallel() // Enable parallel stream processing
                             .map(x -> (int)(Math.pow(x, 2) + Math.log(x + 1) * Math.sin(x))) // Heavier computation
                             .sum();
                }
            }).join();
        } finally {
            customPool.shutdown(); // Shutdown the custom pool
        }
    }
}
