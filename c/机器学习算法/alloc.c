
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <malloc.h>
#include "ga_knn.h"

 /*---------------------------------------------------------------
 | Sample classification and gene assessment for expression data |
 |   using a genetic algorithm and k-nearest neighbor method     |
 |                                                               |
 |                        Leping Li, Ph.D.                       |
 |        National Institute of Environmental Health Sciences    |
 |                  National Institute of Health                 |
 |                                                               |
 |                      Li3@niehs.nih.gov                        |
 |                                                               |
 |                  Date: March 26, 1999                         |
 |     Last Modification: December 24, 2002                      |
 |                                                               |
 |              copyright (c) 1999-2003                          |
 ----------------------------------------------------------------*/

Neighbor **alloc_neighbors(int num_obj,int num_neighbors) {

   Neighbor **tmp=NULL;
   register int i;

   tmp=(Neighbor **)calloc(num_obj,sizeof(Neighbor *));
   if (tmp==0) { printf("distance matrix calloc failed!\n"); exit(1); }

   tmp[0]=(Neighbor *)calloc(num_obj*num_neighbors,sizeof(Neighbor));
   if (tmp[0]==0) { printf("matrix calloc failed!\n"); exit(1); }

   for (i=1; i<num_obj; i++) {
      *(tmp+i)=tmp[0]+(num_neighbors * i);
   }

   return (tmp);
}

unsigned char **alloc_bits_bits(int pop_size,int bit_size) {

   unsigned char **bits=NULL;
   register int i;

   bits=(unsigned char **)calloc(pop_size,sizeof(unsigned char *));
   if (!bits)   { printf("bits calloc failed!\n"); exit(1); }

   bits[0]=(unsigned char *)calloc(pop_size*bit_size,sizeof(unsigned char));
   if (bits[0]==0) { printf("bit[0] calloc failed!\n"); exit(1); }

   /* set up vector pointers */
   for (i=1; i<pop_size; i++) {
      bits[i]=bits[0]+(bit_size * i);
   }

   return (bits);
}

unsigned char *alloc_bits(int bit_size) {

   unsigned char *bits=NULL;

   bits=(unsigned char *)calloc(bit_size,sizeof(unsigned char));
   if (!bits)   { printf("bits calloc failed!\n"); exit(1); }

   return (bits);
}

Class *alloc_class(int num) {

   Class *tmp=NULL;
   register int i;

   tmp=(Class *)calloc(1,sizeof(Class));
   tmp->count=(int *)calloc(num,sizeof(int));
   tmp->type=(int *)calloc(num,sizeof(int));
   if (!tmp->type || !tmp->count) {
      printf("calloc failed for class!\n"); exit(0);
   }

   tmp->which=(int **)calloc(num,sizeof(int *));
   if (!tmp->which)   { printf("tmp calloc failed!\n"); exit(1); }

   tmp->which[0]=(int *)calloc(num*num,sizeof(int));
   if (tmp->which[0]==0) { printf("bit calloc failed!\n"); exit(1); }

   for (i=1; i<num; i++) {
      tmp->which[i]=tmp->which[0]+(num * i);
   }
   return (tmp);
}

void destroy_class(Class *class) {
   
   if (class->which[0]) { free(class->which[0]); class->which[0]=NULL; }
   if (class->which)    { free(class->which);    class->which=NULL;    }
   if (class->type)     { free(class->type);     class->type=NULL;     }
   if (class->count)    { free(class->count);    class->count=NULL;    }
   if (class)           { free(class);           class=NULL;           }
}

SampleInfo *alloc_sample(int num_obj) {

   SampleInfo *tmp=NULL;
   register int i;

   tmp=(SampleInfo *)calloc(num_obj,sizeof(SampleInfo));
   if (!tmp) {
      printf("calloc failed for data!\n"); exit(0);
   }

   for (i=0; i<num_obj; i++) {
      tmp[i].name=(char *)calloc(500,sizeof(char));
      if (!tmp[i].name) {
         printf("calloc failed for data.name!\n"); exit(0);
      }
   }
   return(tmp);
}

Fitness **alloc_fitness_fitness(int size1,int size2) {

   Fitness **tmp=NULL;
   register int i;

   tmp=(Fitness **)calloc(size1,sizeof(Fitness *));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }

   tmp[0]=(Fitness *)calloc(size1*size2,sizeof(Fitness));
   if (tmp[0]==0) { printf("bit calloc failed!\n"); exit(1); }

   /* set up vector pointers */
   for (i=1; i<size1; i++) {
      tmp[i]=tmp[0]+(size2 * i);
   }
   return (tmp);
}

Fitness *alloc_fitness (int pop_size) {

   Fitness *tmp=NULL;

   tmp=(Fitness *)calloc(pop_size,sizeof(Fitness));
   if (!tmp) {
      printf("calloc failed for fitness !\n"); exit(0);
   }
   return (tmp);
}

Wheel *alloc_wheel (int pop_size) {

   Wheel *tmp=NULL;

   tmp=(Wheel *)calloc(pop_size,sizeof(Wheel));
   if (!tmp) {
      printf("calloc failed for fitness !\n"); exit(0);
   }
   return (tmp);
}

double *alloc_double (int pop_size) {

   double *tmp=NULL;

   tmp=(double *)calloc(pop_size,sizeof(double));
   if (!tmp) { printf("calloc failed for int!\n"); exit(0); }
   return (tmp);
}

void destroy_sample(SampleInfo *data,int num_obj) {

   int i;

   for (i=0; i<num_obj; i++) {
      if(data[i].name) { free(data[i].name); data[i].name=NULL; }
   }
   if (data) { free(data); data=NULL; }
}

int ***alloc_int_int_int(int num_niche,int pop_size,int size) {

   int ***tmp=NULL;
   register int i,j;

   tmp      =(int ***)calloc(num_niche,sizeof(int **));
   tmp[0]   =(int **) calloc(num_niche*pop_size,sizeof(int *));
   tmp[0][0]=(int *)  calloc(num_niche*pop_size*size,sizeof(int));

   for (i=1; i<num_niche; i++)
      tmp[i]   =tmp[0]+i*pop_size;

   for (j=1; j<pop_size; j++)
      tmp[0][j]=tmp[0][0]+size*j;

   for (i=1; i<num_niche; i++) {
      tmp[i][0]=tmp[0][0]+size*pop_size*i;
      for (j=1; j<pop_size; j++) {
         tmp[i][j]=tmp[i][0]+size*j;
      }
   }

   return (tmp);
}

unsigned char ***alloc_bits_bits_bits(int num_niche,int pop_size,int size) {

   unsigned char ***tmp=NULL;
   register int i,j;

   tmp      =(unsigned char ***)calloc(num_niche,sizeof(unsigned char **));
   tmp[0]   =(unsigned char **) calloc(num_niche*pop_size,sizeof(unsigned char *));
   tmp[0][0]=(unsigned char *)  calloc(num_niche*pop_size*size,sizeof(unsigned char));

   for (i=1; i<num_niche; i++)
      tmp[i]=tmp[0]+i*pop_size;

   for (j=1; j<pop_size; j++)
      tmp[0][j]=tmp[0][0]+size*j;

   for (i=1; i<num_niche; i++) {
      tmp[i][0]=tmp[0][0]+size*pop_size*i;
      for (j=1; j<pop_size; j++) {
         tmp[i][j]=tmp[i][0]+size*j;
      }
   }

   return (tmp);
}

int **alloc_int_int(int size1,int size2) {

   int **tmp=NULL;
   register int i;

   tmp=(int **)calloc(size1,sizeof(int *));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }

   tmp[0]=(int *)calloc(size1*size2,sizeof(int));
   if (tmp[0]==0) { printf("bit calloc failed!\n"); exit(1); }

   /* set up vector pointers */
   for (i=1; i<size1; i++) {
      tmp[i]=tmp[0]+(size2 * i);
   }
   return (tmp);
}

double **alloc_double_double(int size1,int size2) {

   double **tmp=NULL;
   register int i;

   tmp=(double **)calloc(size1,sizeof(double *));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }

   tmp[0]=(double *)calloc(size1*size2,sizeof(double));
   if (tmp[0]==0) { printf("bit calloc failed!\n"); exit(1); }

   /* set up vector pointers */
   for (i=1; i<size1; i++) {
      tmp[i]=tmp[0]+(size2 * i);
   }

   return (tmp);
}

char **alloc_char_char(int size1,int size2) {

   char **tmp=NULL;
   register int i;

   tmp=(char **)calloc(size1,sizeof(char *));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }

   tmp[0]=(char *)calloc(size1*size2,sizeof(char));
   if (tmp[0]==0) { printf("bit calloc failed!\n"); exit(1); }

   /* set up vector pointers */
   for (i=1; i<size1; i++) {
      tmp[i]=tmp[0]+(size2 * i);
   }

   return (tmp);
}

char *alloc_char(int size1) {

   char *tmp=NULL;
   tmp=(char *)calloc(size1,sizeof(char));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }
   return (tmp);
}

int *alloc_int(int size1) {

   int *tmp=NULL;
   tmp=(int *)calloc(size1,sizeof(int));
   if (!tmp)   { printf("tmp calloc failed!\n"); exit(1); }
   return (tmp);
}

Solution *alloc_solution (int size) {

   Solution *tmp=NULL;

   tmp=(Solution *)calloc(size,sizeof(Solution ));
   if (!tmp) { printf("calloc failed!\n"); exit(0); }
   return (tmp);
}

