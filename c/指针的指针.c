#include <stdio.h>

int main(int argc, char const *argv[])
{

	int a=10;
	int *b=&a;
	int **c=&b;
	printf("*b =%d\n", *b);
	//&a b *c 
	printf("&a =%p\n", &a);
	printf("b =%p\n", b);
	printf("*c =%p\n", *c);
	printf("**c =%d\n", **c);

	printf("&a =%p\n", &a);
	printf("&b =%p\n", &b);
	printf("&c =%p\n", &c);

	/* code */
	return 0;
}