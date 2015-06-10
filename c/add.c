#include <stdio.h>

int main(void){


	int $sum=1;
	int $i;
	for($i=2;$i<=100;$i++){
		printf("%d + %d = %d \n",$sum,$i,$sum+$i);	
		$sum+=$i;
	}
	return 0;
	
}