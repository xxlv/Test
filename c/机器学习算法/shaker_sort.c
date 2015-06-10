
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "ga_knn.h"

/*--------------------------------------------------------*/
/*       modified bubble sort in ascending order          */
/* from Advanced C, Herbert Schild, McGraw-Hill, page 28. */
/*--------------------------------------------------------*/

void shaker_sort_ascen(Neighbor *x,int size,int knn) {

   register int a,b,c,d,t2;
   double t1;

   c = 1;
   b = size-1; d = size-1;

   do {
      for (a = d; a >=c; --a) {
         if (x[a-1].dis > x[a].dis) {
            t1 = x[a-1].dis;
            x[a-1].dis = x[a].dis;
            x[a].dis = t1;

            t2 = x[a-1].id;
            x[a-1].id = x[a].id;
            x[a].id = t2;
            b = a;
         }
      }

      c = b+1;
      for (a = c; a < d+1; ++a) {
         if (x[a-1].dis > x[a].dis) {
            t1 = x[a-1].dis;
            x[a-1].dis = x[a].dis;
            x[a].dis = t1;

            t2 = x[a-1].id;
            x[a-1].id = x[a].id;
            x[a].id = t2;
            b=a;
         }
      }
      d = b-1;

      /*-----------------------------*/
      /* this breaks the loop in the */
      /* worst case scenario.        */
      /*-----------------------------*/
      if (c > d) break;
   } while (c <=knn);

}

/*---------- descending order ----------*/
void shaker_double(double *x,int size,int k) {

   register int a,b,c,d;
   double t1;

   c = 1;
   b = size-1; d = size-1;

   do {
      for (a = d; a >=c; --a) {
         if (x[a-1] < x[a]) {
            t1 = x[a];
            x[a] = x[a-1];
            x[a-1] = t1;
            b = a;
         }
      }

      c = b+1;
      for (a = c; a < d+1; ++a) {
         if (x[a] > x[a-1]) {
            t1 = x[a];
            x[a] = x[a-1];
            x[a-1] = t1;
            b=a;
         }
      }
      d = b-1;
      if (c > d) break;
   } while (c <=k);

}


void shaker_int_descend(int *x,int size) {

   register int a,b,c,d,t1;

   c = 1;
   b = size-1; d = size-1;

   do {
      for (a = d; a >=c; --a) {
         if (x[a-1] < x[a]) {
            t1 = x[a];
            x[a] = x[a-1];
            x[a-1] = t1;
            b = a;
         }
      }

      c = b+1;
      for (a = c; a < d+1; ++a) {
         if (x[a] > x[a-1]) {
            t1 = x[a];
            x[a] = x[a-1];
            x[a-1] = t1;
            b=a;
         }
      }
      d = b-1;
   } while (c <=d);
}

void shaker_int_ascend(int *x,int count) {

   register int a,b,c,d;
   int t;

   c = 1;
   b = count-1; d = count-1;

   do {
      for (a = d; a >=c; --a) {
         if (x[a-1]>x[a]) {
            t = x[a-1];
            x[a-1] = x[a];
            x[a] = t;
            b = a;
         }
      }

      c = b+1;
      for (a = c; a < d+1; ++a) {
         if (x[a-1]>x[a]) {
            t = x[a-1];
            x[a-1] = x[a];
            x[a] = t;
            b=a;
         }
      }
      d = b-1;
      if (c > d) break;

   } while (c <=count);
}

void shaker_double_ascent(double *x,int count) {

   register int a,b,c,d;
   double t;

   c = 1;
   b = count-1; d = count-1;

   do {
      for (a = d; a >=c; --a) {
         if (x[a-1]>x[a]) {
            t = x[a-1];
            x[a-1] = x[a];
            x[a] = t;
            b = a;
         }
      }

      c = b+1;
      for (a = c; a < d+1; ++a) {
         if (x[a-1]>x[a]) {
            t = x[a-1];
            x[a-1] = x[a];
            x[a] = t;
            b=a;
         }
      }
      d = b-1;
   } while (c <=d);
}

