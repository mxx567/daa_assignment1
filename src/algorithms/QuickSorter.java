package algorithms;

import java.util.Random;

public class QuickSorter {
    static Random random = new Random();
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1,1);
    }
    public static void quickSort(int[] arr, int start, int end, int depth){
        Metrics.enter(depth);
        while(start < end){
            int pivin = start + random.nextInt(end - start + 1);
            int temp = arr[pivin];
            arr[pivin] = arr[start];
            arr[start] = temp;
            int pivot = partition(arr, start, end);
            if (pivot - start < end - pivot) {
                quickSort(arr, start, pivot, depth + 1);
                start = pivot + 1;

            } else {
                quickSort(arr, pivot + 1, end, depth + 1);
                end = pivot;
            }
        }

    }

    public static int partition(int[] arr, int start, int end){
        int pivot = arr[start];
        int i = start - 1, j = end + 1;
        while (true) {
            do { i++; } while (arr[i] < pivot);
            do { j--; } while (arr[j] > pivot);
            if (i >= j) return j;
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
    }

}
