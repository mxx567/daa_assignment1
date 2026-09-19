import algorithms.*;
void main(String[] args) {
    int[] arr = {1,3,2,10,7};
    MergeSort.mergeSort(arr);
    for(int i = 0; i < arr.length; i++){
        System.out.print(arr[i] +" ");
    }
}