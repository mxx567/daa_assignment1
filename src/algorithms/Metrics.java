package algorithms;


public class Metrics {
    public static int maxDepth;
    public static long calls;

    public static void reset() {
        maxDepth = 0;
        calls = 0;
    }

    public static void enter(int depth) {
        calls++;
        if (depth > maxDepth) maxDepth = depth;
    }
}
