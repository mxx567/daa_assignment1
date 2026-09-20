package experiments;

import algorithms.*;
import objects.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

//CSV columns: algorithm, input_type, n, trial, time_ns, max_depth, recursive_calls

public class Experiment {

    static final int[] SIZES = {1_000, 100_000, 1_000_000};                 // small, medium, large
    public static final String[] TYPES = {"random", "sorted", "reverse", "duplicates"};
    static final int TRIALS = 5;

    static final Comparator<Point> BY_X = Comparator.comparingDouble(Point::getX);

    private static PrintWriter out;

    public static void run(String csvFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(csvFile)) {
            out = writer;
            out.println("algorithm,input_type,n,trial,time_ns,max_depth,recursive_calls");

            for (String type : TYPES) {
                for (int n : SIZES) {
                    Random rnd = new Random(n * 31L + type.hashCode());
                    int[] ints = makeInts(type, n, rnd);
                    Point[] points = makePoints(type, n, rnd);

                    measure("MergeSort",           type, n, ints::clone,   MergeSorter::mergeSort);
                    measure("QuickSort",           type, n, ints::clone,   QuickSorter::quickSort);
                    measure("DeterministicSelect", type, n, ints::clone,   a -> DeterministicSelector.detSel(a, 0, a.length - 1, a.length / 2 + 1));
                    measure("ClosestPair",         type, n, points::clone, Experiment::closestPair);
                    System.out.printf("done: %-10s n=%,d%n", type, n);
                }
            }
        }
        System.out.println("Results saved to " + csvFile);
    }

    // Warm-up
    private static <T> void measure(String algo, String type, int n, Supplier<T> freshInput, Consumer<T> run) {
        run.accept(freshInput.get());   // warm-up for the JIT

        for (int trial = 1; trial <= TRIALS; trial++) {
            T input = freshInput.get();
            Metrics.reset();
            long start = System.nanoTime();
            run.accept(input);
            long elapsed = System.nanoTime() - start;
            out.println(algo + "," + type + "," + n + "," + trial + "," + elapsed + ","
                    + Metrics.maxDepth + "," + Metrics.calls);
        }
    }
    public static double closestPair(Point[] p) {
        Arrays.sort(p, BY_X);
        return ClosestPairSolver.closestPair(p, 0, p.length - 1);
    }

    //input generator
    public static int[] makeInts(String type, int n, Random rnd) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            switch (type) {
                case "sorted":     a[i] = i;               break;
                case "reverse":    a[i] = n - i;           break;
                case "duplicates": a[i] = rnd.nextInt(10); break;   // only 10 distinct values
                default:           a[i] = rnd.nextInt();            // random
            }
        }
        return a;
    }

    //points generator
    public static Point[] makePoints(String type, int n, Random rnd) {
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) {
            if (type.equals("duplicates")) {
                p[i] = new Point(rnd.nextInt(32), rnd.nextInt(32));
            } else {
                p[i] = new Point(rnd.nextDouble() * 1e6, rnd.nextDouble() * 1e6);
            }
        }
        if (type.equals("sorted"))  Arrays.sort(p, BY_X);                    // sorted by x
        if (type.equals("reverse")) Arrays.sort(p, BY_X.reversed());         // sorted by x, descending
        return p;
    }
}
