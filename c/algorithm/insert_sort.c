#include <stdio.h>

int * insert_sort(int * arr,int len){
    // accept a arr [112,3,2,1,5,66,1]  returns [1,1,2,3,5,66,112]
    int arr_sorted[len] ={arr[0]};

    for(int i=1;i<len;i++){
        while(i>0 && arr[i]>arr[i+1]){
            //swap


            i++
        }
    }
    return arr_sorted;
}


int main(void){

    int arr[4]={11,2,3,4};
    insert_sort(arr,4);

    return 0;
}
