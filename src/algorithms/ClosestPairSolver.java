package algorithms;
import objects.Point;

public class ClosestPairSolver {
    public static double closestPair(Point[] points, int l, int r){
        return closestPair(points, l, r, 1);
    }

    public static double closestPair(Point[] points, int l, int r, int depth){
        Metrics.enter(depth);
        if (r - l + 1 <= 2) {
            if (r - l + 1 == 2) {
                return getDistance(points[l], points[r]);
            }
            return Double.MAX_VALUE;
        }
        else{
            int mid = l + (r-l)/2;
            double dA = closestPair(points, l,mid, depth + 1);
            double dB = closestPair(points, mid+1, r, depth + 1);
            double d = Math.min(dA,dB);
            double midX = points[mid].getX();
            Point[] strip = new Point[r - l + 1];
            int size = 0;

            for (int i = l; i <= r; i++) {
                if (Math.abs(points[i].getX() - midX) < d) {
                    strip[size++] = points[i];
                }
            }
            insertionSort(strip, 0, size - 1, false);

            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (strip[j].getY() - strip[i].getY() >= d) {
                        break;
                    }

                    d = Math.min(d, getDistance(strip[i], strip[j]));
                }
            }

            return d;
        }
    }

    public static void insertionSort(Point[] arr, int l, int r, boolean byX) {
        if(byX){
            for (int i = l + 1; i <= r; i++) {
                Point key = arr[i];
                int j = i - 1;

                while (j >= l && arr[j].getX() > key.getX()) {
                    arr[j + 1] = arr[j];
                    j--;
                }

                arr[j + 1] = key;
            }
        }
        else {
            for (int i = l + 1; i <= r; i++) {
                Point key = arr[i];
                int j = i - 1;

                while (j >= l && arr[j].getY() > key.getY()) {
                    arr[j + 1] = arr[j];
                    j--;
                }

                arr[j + 1] = key;
            }
        }
    }

    private static double getDistance(Point p1, Point p2){
        return Math.sqrt((p2.getX()- p1.getX()) * (p2.getX()- p1.getX()) +
                (p2.getY()- p1.getY()) * (p2.getY()- p1.getY()));
    }
}
