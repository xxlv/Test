#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <conio.h> 

#define ARRAY_SIZE 21

int	* bubble_sort(int *,int);
void print_arr(int *,int);

int main(void){
	
	int a[ARRAY_SIZE];
	srand((unsigned)time(NULL)); 

	for(int i=0;i<ARRAY_SIZE;i++){
		a[i]=rand()%ARRAY_SIZE+1;
	}
	// int a[5]={11,2,3,4,5};

	int len=sizeof(a)/sizeof(a[0]);
	printf("--------------Begin Sort-----------------------------------------------------\n\n");
	print_arr(a,len);
	printf("--------------End Sort--------------------------------------------------------\n\n");
	bubble_sort(a,len);
	printf("----------------------------------------------------------------------\n\n");
	print_arr(a,len);
	getchar();
	return 0;
}

int * bubble_sort(int a [],int size){
	int sort_cnt=0;
	int sw_cnt=0;

	// for(int i=0;i<size;i++){
	// 	for(int j=0;j<size-i-1;j++){				
	// 		sort_cnt++;
	// 		if(a[j]>a[j+1]){
	// 			//交换位置
	// 			int tmp=a[i];
	// 			a[i]=a[j];
	// 			a[j]=tmp;
	// 			sw_cnt++;
	// 		}
	// 	}
	for(int i=0;i<size;i++){
		for(int j=i+1;j<size;j++){				
			sort_cnt++;
			if(a[i]>a[j]){
				//交换位置
				int tmp=a[i];
				a[i]=a[j];
				a[j]=tmp;
				sw_cnt++;
			}
		}		

	}
	printf("Sort Count is : %d   Swap Count is :%d \n",sort_cnt,sw_cnt);
	return a;

}


void print_arr(int a[],int size){
	for (int i=0;i<size;i++){
		printf("%5d\t",a[i]);
		if(i%10==0&&i>=10){
			printf("\n");
		}
	}
	printf("\n");
}
