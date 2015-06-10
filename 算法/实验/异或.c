#include <stdio.h>

char * getB(int);

int main(void){
	
	int x=1;
	int y=2;
	char * bt;
	bt=getB(1);	
	printf("%s\n", bt);	
	//printf("%d\n", x^y);
	return 0;

}

//获得二进制的表示 
//1 ->0000 0001
//2 ->0000 0010
char * getB(int x){
	char * t;
	char b[]={'0','0','1','2','3','1','0','\0'};
	// while(x!=0){
	// 	// b[]=x%2;
	// 	x=x/2;
	// }	
	t=b;
	return t;
}