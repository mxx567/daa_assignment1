package algorithms;
public class MergeSorter {
    public static void mergeSort(int[] arr){
        int[] buf = new int[arr.length];
        mergeSort(arr, buf, 0, arr.length-1);
    }

    public static void mergeSort(int[] arr, int[] buf, int l, int r){
        mergeSort(arr, buf, l, r, 1);
    }

    public static void mergeSort(int[] arr, int[] buf, int l, int r, int depth){
        Metrics.enter(depth);
        if(l >= r){
            return;
        }

        if (r - l + 1 <= 10) {
            insertionSort(arr, l, r);
            return;
        }

        int mid = l + (r - l) / 2;

        mergeSort(arr, buf, l, mid, depth + 1);
        mergeSort(arr, buf, mid + 1, r, depth + 1);

        merge(l,r,arr, buf, mid);
    }

    public static void merge(int l, int r, int[] a, int[] buf, int mid){
        int leftIndex = l;
        int rightIndex = mid + 1;
        int bufferIndex = l;


        while (leftIndex <= mid && rightIndex <= r) {

            if (a[leftIndex] <= a[rightIndex]) {
                buf[bufferIndex++] = a[leftIndex++];
            } else {
                buf[bufferIndex++] = a[rightIndex++];
            }
        }

        while (leftIndex <= mid) {
            buf[bufferIndex++] = a[leftIndex++];
        }

        while (rightIndex <= r) {
            buf[bufferIndex++] = a[rightIndex++];
        }

        for (int i = l; i <= r; i++) {
            a[i] = buf[i];
        }
    }

    private static void insertionSort(int[] arr, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= l && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }
}
