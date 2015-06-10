#include <stdio.h>

int main(void){
	int a[7]={1,2,3,4,5,6,7};
	int b[2]={[1]=222};
	
	int * const p=a;

	for(int i=0;i<sizeof(b)/sizeof(b[0]);i++){
		printf("b[%d] = %d \n",i,b[i]);
	}
	return 0;	

}

