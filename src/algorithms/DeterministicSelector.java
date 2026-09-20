package algorithms;

public class DeterministicSelector {
    public static int detSel(int[] arr, int left, int right, int k) {
        return detSel(arr, left, right, k, 1);
    }

    public static int detSel(int[] arr, int left, int right, int k, int depth) {
        Metrics.enter(depth);
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
        for (int i = left; i <= right; i++) {
            if (arr[i] == medianOfMedians) {
                int t = arr[i]; arr[i] = arr[left]; arr[left] = t;
                break;
            }
        }
        int split = partition(arr, left, right);

        int leftSize = split - left + 1;
        if (k <= leftSize) {
            return detSel(arr, left, split, k, depth + 1);
        } else {
            return detSel(arr, split + 1, right, k - leftSize, depth + 1);
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
