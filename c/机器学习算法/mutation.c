
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

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <malloc.h>
#include "ga_knn.h"

void which_positions(int *which,int numReplacement,int chromosomeLength);
/*-------------------------------------------------------*/
/* 1). pass the best chromosome to the next generation   */
/* 2). select a chromosome for mutation. A chromosome    */
/*     is selected with probability that is proportional */
/*     to its fitness score. Next, between one and five  */
/*     positions on the chr are selected. Their contents */
/*     (gene indices) are replaced with the gene indices */
/*     that are not on the chromosome.                   */
/*-------------------------------------------------------*/
void mutation(int **nicheChr,int numGenes,int chromosomeLength,
   int populationSize,Wheel *weight) {

   int wh,num_save,count,numReplacement,maxNumMutate;
   int **tmpChr,*genesOnChr;
   int *replaceGenes,*position;
   register int i,j;

   num_save=1;

   maxNumMutate=(int)(chromosomeLength/10);
   if (maxNumMutate==0) maxNumMutate=1;

   /* printf("maxNumMutate: %3d\n",maxNumMutate); */

   tmpChr      =alloc_int_int(populationSize,chromosomeLength);
   genesOnChr  =alloc_int(chromosomeLength);
   position    =alloc_int(maxNumMutate);
   replaceGenes=alloc_int(maxNumMutate);

   /* pass the best chromosome to <tmpChr> */
   for (i=0; i<num_save; i++) {
      for (j=0; j<chromosomeLength; j++)
         *(*(tmpChr+i)+j)=*(*(nicheChr+(*(weight+i)).index)+j);
   }

   count=num_save; 
   do {
      /*-----------------------------------------------*/
      /* fitness score based selection of a chromosome */
      /*-----------------------------------------------*/
      wh=which_chromosome(weight,populationSize);
      for (i=0; i<chromosomeLength; i++)
         *(genesOnChr+i)=*(*(nicheChr+wh)+i);

      /*--------------------------------------------------*/
      /* determine the number of positions to be replaced */
      /* and the gene indices that are not on the chr     */
      /*--------------------------------------------------*/
      numReplacement=num_mutated(maxNumMutate);
      selectGenesNotOnChr(genesOnChr,chromosomeLength,replaceGenes,numReplacement,numGenes);

      /*-----------------------------------------------*/
      /* randomly select positions on the chromosomes. */
      /* replace the gene indices in these positions   */
      /* with the gene indices that are not on the chr */
      /*-----------------------------------------------*/
      which_positions(position,numReplacement,chromosomeLength);
      for (i=0; i<numReplacement; i++)
         genesOnChr[position[i]]=*(replaceGenes+i);

      /* add to the tmpChr array */
      for (i=0; i<chromosomeLength; i++)
         *(*(tmpChr+count)+i)=*(genesOnChr+i); 

      count++;

   } while (count<populationSize);

   /* update */
   for (i=0; i<populationSize; i++) {
      for (j=0; j<chromosomeLength; j++)
         *(*(nicheChr+i)+j)=*(*(tmpChr+i)+j); 
   } 
 
   if (genesOnChr)   { free(genesOnChr);   genesOnChr=NULL;   }
   if (tmpChr[0])    { free(tmpChr[0]);    tmpChr[0]=NULL;    }
   if (tmpChr)       { free(tmpChr);       tmpChr=NULL;       }
   if (position)     { free(position);     position=NULL;     }
   if (replaceGenes) { free(replaceGenes); replaceGenes=NULL; }
}

int which_chromosome(Wheel *wheel,int populationSize) {

   register int i;
   int which_chr;
   double dummy;

   dummy=100.0*random_gen();
   for (i=0; i<populationSize; i++) {
      if (dummy >= wheel[i].start && dummy <= wheel[i].end)
         return (wheel[i].index); 
   }
   which_chr=(int)(populationSize*random_gen());
   if (which_chr==populationSize) which_chr -= 1;

   return (which_chr);
}

/*--------------------------------------------------------------*/
/* number of mutations is equal to the probability of 1/(2^k),  */
/* k=1 to maximal number of genes can be mutated.               */
/*--------------------------------------------------------------*/
int num_mutated(int maxNumMutate) {
 
   register int i;

   for (i=1; i <= maxNumMutate; i++) {
      if (random_gen() >= (1.0/(pow(2.0,(double)i)) ))
         return (i);
   }
   return (1);
}

void selectGenesNotOnChr(int *genesOnChr,int chromosomeLength,int *which,
   int numReplacement,int numGenes) {

   register int i,j;
   int num_filled,du,used,onChr;

   for (i=0; i<numReplacement; i++) *(which+i)=-1;

   num_filled=0;
   while (num_filled<numReplacement) {
      du=(int)(numGenes*random_gen());
      if (du==numGenes) du--;
      onChr=0;
      for (j=0; j<chromosomeLength; j++) {
         if (du == *(genesOnChr+j)) {
            onChr=1; break; 
         }
      }
      if (!onChr) {
         used=0;
         for (i=0; i<num_filled; i++) {
            if (du == *(which+i)) {
               used=1; break;
            }
         }
         if (!used) {
            *(which+num_filled)=du;
            num_filled++;
         }
      }
   }
}

/* randomly select the postions on the chr to be mutated */
void which_positions(int *which,int numReplacement,int chromosomeLength) {

   register int i;
   int num_filled,dummy,used;

   num_filled=0;
   for (i=0; i<numReplacement; i++) *(which+i)=-1;

   while (num_filled<numReplacement) {
      dummy=(int)(chromosomeLength*random_gen());
      if (dummy == chromosomeLength) dummy--; 
      used=0;
      for (i=0; i<num_filled; i++) {
         if (dummy==*(which+i)) {
            used=1; break;
         }
      }
      if (!used) {
         *(which+num_filled)=dummy;
         num_filled++;
      }
   }
}
