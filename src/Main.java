import algorithms.*;
import objects.*;
void main(String[] args) {
    int[] arr = {1,3,2,10,7,82,12,34,21,42,30,55,21};
    Point[] points = {
            new Point(0, 0),
            new Point(2, 10),
            new Point(4, 0),
            new Point(6, 10),
            new Point(8, 0)
    };
    ClosestPairSolver.insertionSort(points, 0, points.length-1, true);

    System.out.println(ClosestPairSolver.closestPair(points, 0, points.length-1));
}