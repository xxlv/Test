
#include <stdio.h>


int getMinCommonMultiple(int,int);
int getMaxCommonDivide(int,int);

int main(void){

	int min_m=getMinCommonMultiple(45,30);
	int max_d=getMaxCommonDivide(45,30);
	printf("min common multiple=%d  max common divide =%d \n",min_m,max_d);
	getchar();
	return 0;
}
//最小公倍数
//a=45 b=30
//a=3*3*5  a=3*15
//b=2*3*5  b=2*15
//a*b/15 就是最小公倍数啦 15是最大公约数
int getMinCommonMultiple(int a,int b){
	return (a*b)/getMaxCommonDivide(a,b);
}

//求最大公约数
// a=45 b=30 r=15
//怎么求r呢？
//
int getMaxCommonDivide(int a,int b){
	if(a<b){
		a=a+b;
		b=a-b;
		a=a-b;		
		//a=a^b
		//b=b^a
		//a=a^b
	}

	int tmp;
	while (b!=0){
		tmp=a%b;
		a=b;
		b=tmp;
		// printf("a =%d b=%d  a%%b=%d\n",a,b,tmp);
	}
		// printf("MaxCommonDivide=%d\n",a);
	return a;
}

