%demo 词法分析器
%{ 
	#include <stdio.h>
	int id_num;
%}

letter [A-Za-z]
start start
stop stop

%%
letter {id_num++;}
start {printf("start recevied");}
stop {printf("stop recevied");}

%%
void main(){
	yylex();
	printf("no %d",id_num);
}
int yywrap(){

	return 1;
}

