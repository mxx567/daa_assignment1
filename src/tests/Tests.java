package tests;

import algorithms.DeterministicSelector;
import algorithms.MergeSorter;
import algorithms.QuickSorter;
import experiments.Experiment;
import objects.*;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;


public class Tests {
    private static final Random RND = new Random(2024);
    private static int failedGroups = 0;

    public static boolean runAll() {
        testSorter("MergeSort", MergeSorter::mergeSort);
        testSorter("QuickSort", QuickSorter::quickSort);
        testSelect();
        testClosestPair();

        System.out.println(failedGroups == 0 ? "\nALL TESTS PASSED" : "\n" + failedGroups + " TEST GROUP(S) FAILED");
        return failedGroups == 0;
    }

    // ------------------------------------------------------------------ sorting

    private static void testSorter(String name, Consumer<int[]> sorter) {
        int bad = 0, total = 0;

        // edge cases: empty, single, two elements, all equal, negatives, extremes
        int[][] special = {
                {}, {5}, {1, 2}, {2, 1}, {7, 7}, new int[100],
                {-5, 3, -1, 0, -100, 99, -5},
                {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MIN_VALUE}
        };
        for (int[] a : special) {
            total++;
            if (!sortsCorrectly(a, sorter)) bad++;
        }

        // random / sorted / reverse / duplicate-heavy at many sizes (9-12 straddle MergeSort's cutoff of 10)
        int[] sizes = {2, 3, 9, 10, 11, 12, 100, 1_000, 10_000, 100_000, 1_000_000};
        for (String type : Experiment.TYPES) {
            for (int n : sizes) {
                total++;
                if (!sortsCorrectly(Experiment.makeInts(type, n, RND), sorter)) {
                    bad++;
                    System.out.println("    mismatch: " + name + " " + type + " n=" + n);
                }
            }
        }
        report(name + " vs Arrays.sort()", bad, total);
    }

    private static boolean sortsCorrectly(int[] input, Consumer<int[]> sorter) {
        int[] actual = input.clone();
        int[] expected = input.clone();
        Arrays.sort(expected);
        sorter.accept(actual);
        return Arrays.equals(actual, expected);
    }

    // ------------------------------------------------------------------ deterministic select

    /** detSel takes a 1-indexed rank, tests use a 0-indexed k like Arrays.sort(a)[k]. */
    private static int select(int[] a, int k) {
        return DeterministicSelector.detSel(a.clone(), 0, a.length - 1, k + 1);
    }

    private static void testSelect() {
        int bad = 0, total = 0;

        // 500 random tests
        int[] valueRanges = {3, 50, 1_000_000};
        for (int t = 0; t < 500; t++) {
            int n = 1 + RND.nextInt(400);
            int range = valueRanges[t % valueRanges.length];
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = RND.nextInt(range) - range / 2;

            int k = RND.nextInt(n);
            int[] sorted = a.clone();
            Arrays.sort(sorted);

            total++;
            if (select(a, k) != sorted[k]) bad++;
        }

        // every k on small arrays with duplicates
        for (int n = 1; n <= 30; n++) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = RND.nextInt(5);
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            for (int k = 0; k < n; k++) {
                total++;
                if (select(a, k) != sorted[k]) bad++;
            }
        }

        // structured inputs (min, quartiles, median, max), incl. large n and all-equal
        for (String type : Experiment.TYPES) {
            int n = 100_001;
            int[] a = Experiment.makeInts(type, n, RND);
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            for (int k : new int[]{0, n / 4, n / 2, 3 * n / 4, n - 1}) {
                total++;
                if (select(a, k) != sorted[k]) bad++;
            }
        }
        total++;
        if (select(new int[50_000], 25_000) != 0) bad++;      // all equal (zeros)
        total++;
        if (select(new int[]{42}, 0) != 42) bad++;            // single element

        report("DeterministicSelect vs Arrays.sort(a)[k]", bad, total);
    }

    // closest pair testing

    private static void testClosestPair() {
        int bad = 0, total = 0;

        // edge cases
        total++; if (Experiment.closestPair(new Point[]{}) != Double.MAX_VALUE) bad++;                 // no points
        total++; if (Experiment.closestPair(new Point[]{new Point(1, 1)}) != Double.MAX_VALUE) bad++;  // one point
        total++; if (!same(Experiment.closestPair(pts(0, 0, 3, 4)), 5.0)) bad++;                       // two points
        total++; if (!same(Experiment.closestPair(pts(1, 1, 5, 5, 1, 1, 9, 9)), 0.0)) bad++;          // duplicate points
        total++; if (!same(Experiment.closestPair(pts(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)), 0.0)) bad++;    // all identical
        total++; if (!same(Experiment.closestPair(pts(-5, -5, -5.5, -5, 100, 100)), 0.5)) bad++;      // negatives

        Point[] vertical = new Point[100], horizontal = new Point[100];   // all same x / all same y
        for (int i = 0; i < 100; i++) {
            vertical[i] = new Point(5, i * i);
            horizontal[i] = new Point(i * i, -3);
        }
        total++; if (!same(Experiment.closestPair(vertical), 1.0)) bad++;
        total++; if (!same(Experiment.closestPair(horizontal), 1.0)) bad++;

        // n <= 2000: compare with brute force (real coordinates, small integer grid, big integer grid)
        for (int t = 0; t < 300; t++) {
            int n = 2 + RND.nextInt(t < 290 ? 200 : 1800);
            Point[] p = randomPoints(n, t % 3);
            total++;
            if (!same(Experiment.closestPair(p.clone()), bruteForce(p))) bad++;
        }
        for (String type : Experiment.TYPES) {
            for (int n : new int[]{2, 3, 4, 5, 100, 1000, 2000}) {
                Point[] p = Experiment.makePoints(type, n, RND);
                total++;
                if (!same(Experiment.closestPair(p.clone()), bruteForce(p))) bad++;
            }
        }

        // large n: brute force is too slow, so plant two very close points; the answer can't be larger
        Point[] large = randomPoints(200_000, 0);
        large[1_234]   = new Point(500_000, 500_000);
        large[150_000] = new Point(500_000.001, 500_000);
        total++;
        if (Experiment.closestPair(large) > 0.001 + 1e-9) bad++;

        report("ClosestPair vs brute force (n <= 2000) + large n", bad, total);
    }

    private static Point[] randomPoints(int n, int mode) {
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) {
            p[i] = switch (mode) {
                case 0  -> new Point(RND.nextDouble() * 1000, RND.nextDouble() * 1000);
                case 1  -> new Point(RND.nextInt(10), RND.nextInt(10));                 // many duplicates
                default -> new Point(RND.nextInt(1_000_000), RND.nextInt(1_000_000));
            };
        }
        return p;
    }

    //brute force solution
    private static double bruteForce(Point[] p) {
        double best = Double.MAX_VALUE;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                double dx = p[i].getX() - p[j].getX();
                double dy = p[i].getY() - p[j].getY();
                best = Math.min(best, Math.sqrt(dx * dx + dy * dy));
            }
        }
        return best;
    }

    private static Point[] pts(double... xy) {
        Point[] p = new Point[xy.length / 2];
        for (int i = 0; i < p.length; i++) p[i] = new Point(xy[2 * i], xy[2 * i + 1]);
        return p;
    }

    private static boolean same(double a, double b) {
        return Math.abs(a - b) <= 1e-9;
    }

    private static void report(String name, int bad, int total) {
        if (bad > 0) failedGroups++;
        System.out.printf("[%s] %s  (%d cases, %d failed)%n", bad == 0 ? "PASS" : "FAIL", name, total, bad);
    }
}
