#include <stdio.h>

int main(void){

	int arr[5];

	printf("int has %d  bytes\n",sizeof(int));  //4
	printf("char has %d  bytes\n",sizeof(char));//1
	printf("short has %d  bytes\n",sizeof(short));//2

	arr[3]=128;
	arr[4]=10;
	((short *)arr)[6]=2;
	printf("%d\n", ((short *)(((char *)&arr[1])+8))[2]);//10 将数组arr[1]的地址转换成char 类型，移动8*(sizeof(char))后,到达a[3]，此时转换为short之后,移动2*sizeof(short)到底的位置啊a[4]
	
	printf("%d\n",arr[3] );	
	return 0;
}