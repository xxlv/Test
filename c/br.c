#include <stdio.h>
//求50人生日至少有两人相同的概率
//问题分析：首先求出50人生日都不再同一天的概率
//50人生日不再同一天的概率是 365/365*364/365*...*(365-n)/365^n
long int g(int,int);

void main(){	
	printf("%d\n",g(365,50));
}

long int g(int y,int t){
	if(t>0){
		return y*g(y-1,t-1);
	}else{
		return 1;
	}

}
