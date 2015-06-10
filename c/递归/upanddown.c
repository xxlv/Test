#include <stdio.h>

int up_and_down(int);//测试
long long rfact(int);//阶乘
void to_binary(unsigned int);//转换成二进制表示
long Fibonacci(int);//费波拉契数列

int main(void){

	//up_and_down(2);//测试
	//printf("rfact result is %ld",rfact(5));//阶乘
	to_binary(1024);
	return 0;
}
/*	
	*递归理解
	*输出8条信息 递归中止条件 n=4
	*首先执行的是第一个
	*
	*
*/
int up_and_down(int n){
	printf("Levels %d  Location %p\n",n,&n );
	if(n<4)
		up_and_down(n+1);

	printf("Level %d  Location %p\n",n,&n );
}

/*
	* 使用递归输出阶乘
	* 13！将产生一个比long还大的整数
	* 因此需要提升类型long long
	*
*/

long long rfact(int m){
	long long ans;
	if(m>0)
		ans =m*rfact(m-1);
	else
		ans=1;
	return ans;
}

/*
	* 编写一个函数将整数转换为二进制
	* 如何将一个整数变成二进制形式呢？ 
	* 比如n=5的表示方式是 0101 n=6的表示方式是 0110  奇数的尾部是1 偶数的尾部是0 
	*
*/
void to_binary(unsigned  int n){
	unsigned int r=n/2;
	if(r>0){
		to_binary(r);
	}
	printf("%d",n%2!=0);
}
/*
	* 费波拉契数列
	* 1 1 2 3 5 8 13 ....
	* 接受整数输入 输出这个数的费波拉契数列
	* 
*/

long Fibonacci(int n){
	if(n>2)	//当n==2 时候 1就是那个值 当n=5的时候，需要知道n=4 和 n=3 时候的值 如此进行递归
		   	//把一个复杂的问题分解对比较简单的问题求解
		return Fibonacci(n-1)+Fibonacci(n-2);
	else
		return 1;

}