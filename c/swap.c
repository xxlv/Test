#include <stdio.h>
#include <string.h>
#include <stdlib.h>


char *strdup(const char *str)
{
    int n = strlen(str) + 1;
    char *dup = malloc(n);
    if(dup)
    {
        strcpy(dup, str);
    }
    return dup;
}
void swap(int *,int *);
void swap2(void *,void *,int);
int main(void){

	//判断大尾巴小尾巴
	//husband执行Hello\0
	//&husband执行Hello\0所在的地址

	char * husband=strdup("Hello");
	char * wife=strdup("Word");
	printf("wife=%p\n",wife );

	printf("&wife=%p\n",&wife );

	// printf("husband=%s\n",husband);
	// printf("wife=%s\n",wife );
	swap2(&husband,&wife,sizeof(char *));
	// printf("husband=%s\n",husband);
	// printf("wife=%s\n",wife );
	
	// char a='A';
	// char b='B';

	// unsigned long  a=123;
	// unsigned long  b=32;

	// int a =1;
	// int b=2;	

	// char * p1=strdup("Hello");
	// char * p2=strdup("word");

	int a=1;
	short b=5;
	// printf("%s\n",*p1);
	// printf("%s\n", *(&p1));
	printf("%d\n",sizeof(short));
	printf("a=%d,b=%d \n", a,b);
	// swap2(&a,&b,sizeof(short));
	// swap(&a,&b);
	printf("a=%d,b=%d \n", a,b);



	return 0;	
}

//问题 我现在不想 让参数局限于int 我想着把任意字节的数据进行交换，该怎么做呢？
//首先是参数该如何传递，我如果动态的体现一个参数？ void * 这时候就参数的类型就指定了

void swap(int * a,int * b){

	int t;
	t=*a;
	*a=*b;
	*b=t;
}

//使用c++的模板的,会根据传递的指针的类型不同产生不同的swap ，比如分配一个特定域int的swap，而使用下面的版本产生的汇编是相同的
// 不能使用*a来获取a在内存中的值 因为不确定一个void *到底占据多大空间
//如果同时调用1000W次代码的话，就相当于开辟了1000W个swap，相同代码的不同copy
//对void * 接引用是有可能发生的 编译器的问题

void swap2(void * a,void * b,int size){
	//把a存入buffer
	printf(">>>=%p\n",b );

	char buffer[size];
	memcpy(buffer,a,size);
	memcpy(a,b,size);
	memcpy(b,buffer,size);

	// //这里计算a占据的字节
	// int sizeofa=sizeof(a);
	// int sizeofb=sizeof(b);
	// printf("a=%d,b=%d\n",sizeofa,sizeofb );
}