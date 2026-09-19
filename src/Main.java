import algorithms.*;
void main(String[] args) {
    int[] arr = {1,3,2,10,7,82,12,34,21,42,30,55,21};
    System.out.println(DeterministicSelector.detSel(arr, 0, arr.length-1, 5));
}