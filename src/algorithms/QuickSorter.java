package algorithms;

import java.util.Random;

public class QuickSorter {
    static Random random = new Random();
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }
    public static void quickSort(int[] arr, int start, int end){
        while(start < end){
            int pivin = start + random.nextInt(end - start + 1);
            int temp = arr[pivin];
            arr[pivin] = arr[end];
            arr[end] = temp;
            int pivot = partition(arr, start, end);
            if (pivot - start < end - pivot) {
                quickSort(arr, start, pivot - 1);
                start = pivot + 1;

            } else {
                quickSort(arr, pivot + 1, end);
                end = pivot - 1;
            }
        }

    }

    public static int partition(int[] arr, int start, int end){
        int pivot = arr[end];
        int i = start -1;
        for(int j = start; j <= end - 1; j++){
            if(arr[j] < pivot){
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        i++;
        int temp = arr[i];
        arr[i] = arr[end];
        arr[end] = temp;

        return i;
    }

}
