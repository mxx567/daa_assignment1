package algorithms;

public class DeterministicSelector {
    public static int detSel(int[] arr, int left, int right, int k) {
        int s = right - left + 1;

        if (s <= 5) {
            insertionSort(arr, left, right);
            return arr[left + k - 1];
        }

        int medbufsize = (s + 4) / 5;
        int[] medbuf = new int[medbufsize];
        int j = 0;

        for (int i = left; i <= right; i += 5) {
            int rightmost = Math.min(i + 4, right);
            insertionSort(arr, i, rightmost);
            int medianIndex = i + (rightmost - i) / 2;
            medbuf[j++] = arr[medianIndex];
        }

        int medianOfMedians = detSel(medbuf, 0, medbuf.length - 1, (medbuf.length + 1) / 2);
        int pivotIndex = partition(arr, left, right, medianOfMedians);

        int rank = pivotIndex - left + 1;

        if (k == rank) {
            return arr[pivotIndex];
        }
        else if (k < rank) {
            return detSel(arr, left, pivotIndex - 1, k);
        }
        else {
            return detSel(arr, pivotIndex + 1, right, k - rank);
        }
    }

    public static int partition(int[] arr, int start, int end, int pivot){
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
