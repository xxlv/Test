#include <stdio.h>


int * choice_sort(int * arr,int len){

    printf("%d",len);

    for(int i=0;i<len;i++){
        for(int j=i+1;j<len;j++){
            int min=arr[j];
            if (min<arr[i]){
            }
        }
    }
    return arr;
}
int main(){

    int arr[]={11,23,1,24,3};
    choice_sort(arr,5);

    return 0;
}
