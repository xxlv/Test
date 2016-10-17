#include <stdio.h>




void quick_sort(int * arr ,int len){
  int * less;
  int * great;
  int * equal;

  int pivot=arr[0];
    for(int i=0;i<len;i++){
      if(pivot>arr[i]){
          less[i]=arr[i];
         }
         else if(pivot==arr[i]){
	       equal[i]=arr[i];
       }else{
           great[i]=arr[i];
       }

    }

  return quick_sort(less)+[equal]+quick_sort(great);
}

int main(void){

  int * arr={13,3,21,1,1,3};
  quick_sort(arr);
  return 0;

}
