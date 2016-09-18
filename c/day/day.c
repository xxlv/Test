#include <stdio.h>
#include <stdlib.h>
#include <math.h>


int main(){

    int n=1;
    while(1)
    {
        if(100*sqrt(n)<pow(2,n)){

            printf("%f-%f\n", 100*sqrt(n),pow(2,n));
            printf("%d\n",n );
            return n;
        }
        n++;
    }


    return 0;

}
