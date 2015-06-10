/*----------------------------------------------------------------------- |   Sample classification and feature selection for high-dimensional   |
 | genomic data using a genetic algorithm and k-nearest neighbor method |
 |                                                                      |
 |                           Leping Li, Ph.D.                           |
 |                                                                      |
 |            National Institute of Environmental Health Sciences       |
 |                     National Institute of Health                     |
 |                                                                      |
 |                          Li3@niehs.nih.gov                           |
 |                                                                      |
 |                        Date: March 26, 1999                          |
 |           Last Modification: Sept. 18, 2004                       |
 |                                                                      |
 |                      copyright (c) 1999-2003                         |
 -----------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <malloc.h>
#include <ctype.h>
#include <time.h>
#include "ga_knn.h"

void rank_by_variation(double **expValue,int numSamples,int numVariables,
   SampleInfo *sample,char **variableName);

int main(int argc,char **argv) {

   FILE *fp,*f_update,*fq2,*fq3;
   register int iii,ii,jj,kk,i,j,m,n;
   int numNiches,populationSize,numGenerations,numRuns;
   int numVariables,chromosomeLength,numTopGenes;
   int numSamples,numTraining,numTesting;
   int knn,numSolutionObtained,numSolutionsSpecified;
   int logTransform,medianCentering,zTransform,rangeScale,application,majorityRule;
   int totalCorrectOneRun;
   double percentCorrectOneRun[200]; /* allows up to 200 top-ranked features */

   SampleInfo *sample;                /* sample names and classes  */
   Neighbor **neighbors;              /* knn neighbors & distances */
   Fitness **fitness;                 /* fitness scores, all chr   */
   Fitness *niche;                    /* fitness scores, niches    */
   Wheel *weight;                     /* weights for chromosomes   */
   Class *class;                      /* sample class              */
   int ***allChr;                     /* all chromosomes           */
   int *bestChr;                      /* near-optimal chromosome   */
   int *predictedClass;               /* predicted class           */
   double **expValue;                 /* expression data matrix    */
   char dataFileName[200];            /* input data file name      */
   char *c,**variableName;
   char **missingIndicator;
   int seed,target_R2,printFitnessScore;
   int solutionFound,bestNicheId,bestChrId,worstChrId;
   Solution *selectCount,*countCopy;        /* no. times a feature selected */
   time_t t;                

   if (argc<23) {
      printf("ga_knn argument: \n");
      printf(" -a application\n");
      printf("    1 = application 1 variable selection using all samples.\n");
      printf("    2 = application 2 variable selection using only the training samples.\n");
      printf("    3 = application 3 leave-one-out cross validation.\n");
      printf("    4 = application 4 split the data into training and test sets multiple times.\n\n");
      printf(" -c classification rule\n");
      printf("    1 = majority rule (default).\n");
      printf("    2 = consensus rule.\n\n");
      printf(" -d chromosome length [integer]\n");
      printf("    10 or 20 should be ok for most cases.\n\n");
      printf(" -f data file name [char]\n\n");
      printf(" -k k-nearest neighbors[interger]\n");
      printf("    try 3 or 5.\n\n");
      printf(" -n number of samples in the data [integer]\n\n");
      printf(" -p print fitness score on screen for every generation of GA run\n");
      printf("    0 = no print\n");
      printf("    1 = print\n\n");
      printf(" -r target termination cutoff [integer]\n\n");
      printf("    should be less or equal to the number of training samples\n");
      printf(" -s total number of solutions to be collected [integer]\n");
      printf("    try 5000.\n\n");
      printf(" -t number of training samples [integer]\n");
      printf("    should be less or equal to total number of samples.\n\n");
      printf(" -v number of variables (genes or m/z ratios) in your data [integer]\n\n");
      printf(" -N standardization multiple choices are allowed e.g. -N 1 2 3 [Default: none]\n");
      printf("    1 = log2 transformation\n");
      printf("    2 = median cetering of columns\n");
      printf("    3 = z-transformation of rows\n");
      printf("    4 = range scale (may be used for SELDI-TOF data)\n\n");
      exit(0);
   }

   logTransform=-1; medianCentering=-1; zTransform=-1; knn=-1; chromosomeLength=-1;
   rangeScale=-1; application=-1; majorityRule=-1; numVariables=-1; numSamples=-1;
   numTraining=-1; numSolutionsSpecified=-1; target_R2=-1; printFitnessScore=-1;
   for (i=0; i<argc; i++) printf("%s\n",argv[i]);
   for (i=1; i<argc-1; i++) {
      if (argv[i][0]=='-') {
         switch(argv[i][1]) {
            case 'a': 
               application=atoi(argv[i+1]); break;
            case 'c':
               majorityRule=atoi(argv[i+1]); break;
            case 'd':
               chromosomeLength=atoi(argv[i+1]); break;
            case 'f':
               strcpy(dataFileName,argv[i+1]); break;
            case 'k':
               knn=atoi(argv[i+1]); break;
            case 'n':
               numSamples=atoi(argv[i+1]);  break;
            case 'p':
               printFitnessScore=atoi(argv[i+1]); break; 
            case 'r':
               target_R2=atoi(argv[i+1]); break;
            case 's':
               numSolutionsSpecified=atoi(argv[i+1]);  break;
            case 't':
               numTraining=atoi(argv[i+1]);  break;
            case 'v':
               numVariables=atoi(argv[i+1]); break;
            case 'N': 
               if (atoi(argv[i+1])==1 && argv[i+1][0]!='-')             logTransform=1;
               if (i+1<argc && atoi(argv[i+1])==2 && argv[i+1][0]!='-') medianCentering=1;
               if (i+1<argc && atoi(argv[i+1])==3 && argv[i+1][0]!='-') zTransform=1;
               if (i+1<argc && atoi(argv[i+1])==4 && argv[i+1][0]!='-') rangeScale=1;
               break;
            default: break;
         }
      }
   }

   seed=time(0);
   random_initialize(seed);

   numNiches=3; populationSize=50; numGenerations=50;

   if (application<1 || application>4) application=1;
   if (majorityRule !=1 && majorityRule!=2) majorityRule=1;

   if (application==3) numTraining=numSamples-1;

   numTesting=numSamples-numTraining;
   if (numTesting<1) { numTraining=numSamples; numTesting=0; }

   printf("\nData info:\n");
   printf("   Total number of samples:                   %d\n",numSamples);
   printf("   Number of samples in training set:         %d\n",numTraining);
   printf("   Number of samples in test set:             %d\n",numTesting);
   printf("   Number of variables (genes or m/z...):     %d\n",numVariables);
   printf("   Data file:                                 %s\n",dataFileName);
   printf("\nGA parameters:\n");
   printf("   Number of niches:                          %d\n",numNiches);
   printf("   Number of generations:                     %d\n",numGenerations);
   printf("   Population size:                           %d\n",populationSize);
   printf("   Chromosome length (d):                     %d\n",chromosomeLength);
   printf("   Termination fitness cutoff:                %d\n",target_R2);
   printf("\nK-nearest neighbors:\n");
   printf("   KNN:                                       %d\n",knn);
   printf("Others:\n");
   printf("   Number of near-optimal chromosomes:        %d\n",numSolutionsSpecified);

   if (numNiches<1 || populationSize<1 || numGenerations<1 || numSamples<1 ||
       numVariables<1 || chromosomeLength<1 || knn<1 || numSolutionsSpecified <1 || target_R2<0) {
       printf("Invalid input... parameter(s) is negative or zero. Bye...\n");
       exit(0);
   }
   if (target_R2>numTraining) {
       printf("\nError: Termination fitness cutoff is greater than the number of training samples!\n\n");
       exit(0);
   }
   /* finished reading parameters and names of input files */

   /* read data file  delimited)                           */
   sample=alloc_sample(numSamples);
   variableName=alloc_char_char(numVariables+5,MAX_NUM_CHAR);   /* up to 3000 characters long */
   missingIndicator=alloc_char_char(numVariables+5,numSamples);
   expValue=read_data(argv[1],dataFileName,numSamples,numVariables,variableName,sample,missingIndicator); 
#ifdef RANK_BY_VARIATION
   rank_by_variation(expValue,numSamples,numVariables,sample,variableName);
   exit(0);
#endif

#ifdef DEBUG_PRINT
   debug_print(expValue,numSamples,numVariables,variableName,sample,0);

#endif
   class=assign_class(sample,numSamples);
   missing_value_impute(expValue,class,sample,missingIndicator,numVariables,numSamples);

   if (logTransform==1) log_transform(expValue,numSamples,numVariables);

   /* make sure knn is not too large */
   check_knn(numSamples,class->min,knn,application);

   /*-------------------------------------------------------------------*/
   /*              data standardization and transformation              */
   /*-------------------------------------------------------------------*/
   if (medianCentering==1) {
      printf("\n\nMedian centering each sample (column).\n");
      center_array(expValue,numSamples,numVariables);
   }
   if (zTransform==1) {
      printf("applying z-transformation (standardization, mean=0 sd=1) to each varialbe (row).\n");
      autoscale(expValue,numSamples,numVariables);
   }
   if (rangeScale==1) {
      printf("applying range scale to each varialbe (row).\n");
      range_scale(expValue,numSamples,numVariables);
   }

   if (chromosomeLength>=numVariables) chromosomeLength=numVariables;
   /*--------------------------------------------------------*/
   /*            additional memory allocations               */
   /*--------------------------------------------------------*/
   neighbors     =alloc_neighbors(numSamples,numSamples);
   allChr        =alloc_int_int_int(numNiches,populationSize,chromosomeLength);
   bestChr       =alloc_int(chromosomeLength);
   fitness       =alloc_fitness_fitness(numNiches,populationSize);
   niche         =alloc_fitness(numNiches);
   selectCount   =alloc_solution(numVariables);
   countCopy     =alloc_solution(numVariables);
   weight        =alloc_wheel(populationSize);
   predictedClass=alloc_int(numSamples);

   fq2=NULL;
   if (application==4) { /* split the data into training and test sets multiple times */
      numRuns=50;
      fq2=fopen("prediction_percentage.txt","w");
      fq3=fopen("prediction_test_set.txt","w");
   }
   else if (application==3) {  /* leave one out cross validation */
      fq3=fopen("prediction_loocv.txt","w");
      numRuns=numSamples;
   }
   else {   /* applications 1 & 2 */
      fq3=fopen("prediction_test_set.txt","w");
      numRuns=1;
   }

   fp=fopen("ga_knn_info.txt","w"); 

   c=(char *)calloc(50,sizeof(char));
   t=time(NULL);
   c=asctime(localtime(&t)); 
   fprintf(fp,"##################################################################\n");
   fprintf(fp,"     Gene Selection and Sample Classification Using\n");
   fprintf(fp,"  the Genetic algorithm/k-nearest neighbors algorithm\n\n");
   fprintf(fp,"                  Author: Leping Li\n\n");
   fprintf(fp,"   National Institute of Environmental Health Sciences\n");
   fprintf(fp,"           Research Triangle Park, NC 27709\n");
   fprintf(fp,"              Email: Li3@niehs.nih.gov\n\n");
   fprintf(fp,"               Copyright (c) 1999-2003\n\n");
   fprintf(fp,"   ---------------------Version 1.02---------------------  \n");
   fprintf(fp,"        Last modification: September 18, 2004\n\n");

   fprintf(fp,"Date & time calculation performed: %s\n",c);
   fprintf(fp,"\nData info:\n");
   fprintf(fp,"   Total number of samples:                   %d\n",numSamples);
   fprintf(fp,"   Number of samples in training set:         %d\n",numTraining);
   fprintf(fp,"   Number of samples in test set:             %d\n",numTesting);
   fprintf(fp,"   Number of variables (genes or m/z...):     %d\n",numVariables);
   fprintf(fp,"   Data file:                                 %s\n",dataFileName);
   fprintf(fp,"\nGA parameters:\n");
   fprintf(fp,"   Number of niches:                          %d\n",numNiches);
   fprintf(fp,"   Number of generations:                     %d\n",numGenerations);
   fprintf(fp,"   Population size:                           %d\n",populationSize);
   fprintf(fp,"   Chromosome length (d):                     %d\n",chromosomeLength);
   fprintf(fp,"   Termination fitness cutoff:                %d\n",target_R2);
   fprintf(fp,"   Number of solutions specified:             %d\n",numSolutionsSpecified);
   fprintf(fp,"\nOthers:\n");
   fprintf(fp,"  Random seed number:                         %d\n",seed);

   fprintf(fp,"\nTotal number of classes: %4d, individual class type: ",class->num_class);
   for (i=0; i<class->num_class; i++)
      fprintf(fp,"%3d ",class->type[i]);
   fprintf(fp,".\n");
   fprintf(fp,"Number of samples in each class: ");
   for (j=0; j<class->num_class; j++)
      fprintf(fp,"%3d [class - %1d] ",class->count[j],class->type[j]);
   fprintf(fp,"\n");
   fprintf(fp,"Minimal and maximal numbers of samples in a class: %4d %4d\n",class->min,class->max);
   fprintf(fp,"\n");

   for (i=0; i<class->num_class; i++) {
      fprintf(fp,"  class[%d]: ",class->type[i]);
      for (j=0; j<class->count[i]; j++) {
         fprintf(fp,"%4d ",class->which[i][j]+1);
      } fprintf(fp,"\n");
   }
   if (majorityRule==0) {
      fprintf(fp,"\nKNN:\n");
      fprintf(fp,"  k-nearest neighbors=%2d\n",knn);
      fprintf(fp,"  A consensus rule applies.\n");
      fprintf(fp,"    - All neighbors must agree (e.g, %d out of %d).\n\n",knn,knn);
   }
   else if (majorityRule==1) {
      printf("\nKNN: using a majority rule.\n\n");
      fprintf(fp,"\nKNN:\n");
      fprintf(fp,"  k-nearest neighbors=%2d\n",knn);
      fprintf(fp,"  A majority rule applies.\n");
      fprintf(fp,"    - A majority of the neighbors must agree that is AT LEAST\n");
      fprintf(fp,"      %d out of the %d neighbors must be the same type.\n",
                        (int)(ceil((double)knn/2.0)),knn);
   }
   if (application==4) {
      if (numTesting !=0) {
         fprintf(fp,"\nApplication 4...\n");
         fprintf(fp,"   The data set is randomly divided into a training and test set.\n");
         fprintf(fp,"   The number of samples in each class in both training and test\n");
         fprintf(fp,"   sets are proportional.\n\n");
         fprintf(fp,"   The training set is used to develop a classification rule which\n");
         fprintf(fp,"   is subsequently used to predict the classes of the test samples\n");
         fprintf(fp,"   This process is repeated many times (default is 50), each of which.\n");
         fprintf(fp,"   uses different training and test sets that are randonly partitioned.\n\n");
      }
   }
   if (application==3) {
      fprintf(fp,"\nApplication 3...\n");
      fprintf(fp,"   A leave-one-out cross-validation (LOOCV) is carried out.\n");
   }
   fprintf(fp,"  \nInformation on data processing:\n");
   if (logTransform==1) fprintf(fp,"    Data are log2 transformed.\n\n");
   if (medianCentering==1) 
      fprintf(fp,"     Each sample (column) is centered by the median of all elements in the col.\n");
   if (zTransform==1) 
      fprintf(fp,"     Each row (variable, e.g., m/z or gene) is z-transformed (mean=0 sd=1).\n");
   if (rangeScale==1)
      fprintf(fp,"     Each row (variable, e.g., m/z or gene) is range-standardized (Between 0 and 1).\n");
   fprintf(fp,"##################################################################\n");
   fflush (fp);  fclose(fp);

   for (j=0;j<numVariables;j++)   { selectCount[j].index=j; selectCount[j].total=0; }

   printf("\nRuning GA/KNN ... It may take a few minutes to many hours depending on\n");
   printf("the size of your data and how difficult the classes can be separated. In a\n");
   printf("few minutes, it should print out on screen the number of solutions obtained\n");
   printf("so far. If it takes too long, kill the job (Control/C), reduce fitness\n");
   printf("cuotff and restart it.\n\n");

   for (iii=0; iii<numRuns; iii++) {

   if (application==4) {
      seed=time(0)+iii;
      random_initialize(seed);
      /*---------------------------------------------------------*/
      /* Unblocking the two lines above will give you the same   */ 
      /* training and test sets when you re-run the code with    */
      /* different flag(s) in the Makefile, e.g, AUTOSCALE vs.   */
      /* the default (no autoscale).This is particularly useful, */
      /* if you want to compare the results from different       */
      /* standardization procedures.                             */
      /*---------------------------------------------------------*/

         splitIntoTrainingTest(expValue,numVariables,sample,numSamples,class,numTraining);
         class=assign_class(sample,numSamples);
      }
      if (application==3) {
         /*---------------------------------------------------------*/
         /* always take the last one as the leave-one out sample   */
         /*---------------------------------------------------------*/
         if (iii !=0)
            move_LOO_bottom(expValue,numVariables,sample,numSamples,iii);
      }

      numSolutionObtained=0;
      do {
   
         initialize_chr(populationSize,numVariables,chromosomeLength,allChr,numNiches);
         solutionFound=0;
         for (ii=0; ii<numGenerations; ii++) {
            for (jj=0; jj<numNiches; jj++) {
               for (kk=0; kk<populationSize; kk++) {
                
                  distance(expValue,allChr[jj][kk],chromosomeLength,numTraining,neighbors,knn);
                  predict_class(sample,numTraining,class,neighbors,knn,predictedClass,majorityRule);
                  (*(*(fitness+jj)+kk)).value=cal_fitness(sample,predictedClass,numTraining);
                  (*(*(fitness+jj)+kk)).index=kk;
               }

               sort_fitness(fitness[jj],populationSize);
               (*(niche+jj)).index=jj;
               (*(niche+jj)).value=(*(*(fitness+jj))).value;
         
               if (fitness[jj][0].value >= target_R2) {
                  /* a near-optimal solution has been obtained */
                  for (i=0; i<chromosomeLength; i++)
                     bestChr[i]=allChr[jj][fitness[jj][0].index][i];
   
                  solutionFound=1;
                  if (printFitnessScore) 
                     printf("generation[%4d]: fitness score: %4d\n",ii+1,(int)fitness[jj][0].value);
                 
                  /*----------------------------------------------*/ 
                  /* if a solution is found, break numNiches loop */
                  /*----------------------------------------------*/ 
                  break; 
               }
            }
            if (solutionFound) {
               /*-------------------------------------------------*/ 
               /* find out which genes are on the chromosome and  */
               /* update the number of times it has been selected.*/
               /*-------------------------------------------------*/ 
               for (i=0; i<chromosomeLength; i++) {
                  for (j=0; j<numVariables; j++) {
                     if (*(bestChr+i)==j) { (selectCount[j].total)++; break; }
                  }
               }
               numSolutionObtained++;
   
               /*------------------------------------------------------*/ 
               /*   you can print the individual solutions into a file */ 
               /*------------------------------------------------------*/ 
               /* for (i=0; i<chromosomeLength; i++)                   */
               /*    fprintf(fq,"%4d ",bestChr[i]);                    */
               /* fprintf(fq,"\n"); fflush(fq);                        */
               /*------------------------------------------------------*/
    
               break; 
            }
   
            else {
   
               sort_fitness(niche,numNiches);
               if (printFitnessScore) 
                  printf("generation[%4d]: fitness score: %4d\n",ii+1,(int)niche[0].value); 
    
               /*----------------------------------------------------------*/ 
               /* Replace the worst chromosomes (total: numNiches) in each */
               /* niche by the best chromsomes (one from each niche).      */
               /*----------------------------------------------------------*/ 
   
               m=populationSize-1; n=0;
               do {
   
                  bestNicheId=niche[n].index;
                  bestChrId  =fitness[bestNicheId][0].index;
   
                  for (jj=0; jj<numNiches; jj++) {
                     worstChrId=fitness[jj][m].index;
                     for (i=0; i<chromosomeLength; i++)
                        allChr[jj][worstChrId][i]=allChr[bestNicheId][bestChrId][i];
                     fitness[jj][m].value=niche[n].value;  
                  }
   
                  /*---------------------------------------------*/
                  /* if numNiches is greater than populationSize */
                  /*---------------------------------------------*/
                  if (m==0) break;
                  m--; n++;
               } while (n<numNiches);
   
               for (jj=0; jj<numNiches; jj++) {
   
                  /*------------------------------------------------------*/ 
                  /* The worst chromosomes have been replaced by the best */
                  /* chromosomes, the <fitness> array needs to be updated */
                  /*------------------------------------------------------*/ 
                  sort_fitness(fitness[jj],populationSize);
  
                  /* fitness score based selection  - roulette-wheel       */ 
                  roulett_wheel(fitness[jj],populationSize,weight);
                  mutation(allChr[jj],numVariables,chromosomeLength,populationSize,weight);
               }
            } 
         }
 
    
         if (numSolutionObtained !=0) {
          
            if (numSolutionObtained%10==0 || numSolutionObtained==numSolutionsSpecified) {
               if (application==4)
                  printf("split[%d]: number of near-optimal solutions obtained so far: %d\n",iii+1,numSolutionObtained);
               else if (application==3)
                  printf("leave-sample[%d]-out: number of near-optimal solutions obtained so far: %d\n",
                  numSamples-iii,numSolutionObtained);
               else
                  printf("number of near-optimal solutions obtained so far: %d\n",numSolutionObtained);
            }

            /* writes out the results every some steps */
            if (numSolutionObtained%500==0 || numSolutionObtained==numSolutionsSpecified) {
               f_update=fopen("selection_count.txt","w");
               fprintf(f_update,"Total number of near-optimal solutions obtained so far: %d\n\n",numSolutionObtained);
               fprintf(f_update,"This file can be sorted using unix command:\n");
               fprintf(f_update,"   sort -k2 -r selection_count.out > sorted_output.txt\n"); 
               fprintf(f_update,"It can also be displayed using any data display program.\n\n");
               fprintf(f_update,"Gene ID  No. of times it being selected  Freq.\n");
               fprintf(f_update,"-------  ------------------------------  -----\n");
               for (j=0; j<numVariables; j++)
                  fprintf(f_update,"%5d      %15d               %5.3f\n",
                     selectCount[j].index+1,selectCount[j].total,(double)selectCount[j].total/(double)numSolutionObtained);
               fprintf(f_update,"----------------------------------------------------\n");
               fprintf(f_update,"Total number of variables (genes, m/z): %d\n",numVariables);
               fprintf(f_update,"Chromosome length (d):                  %d\n",chromosomeLength);
   
               t=time(NULL);
               c=asctime(localtime(&t));
               fprintf(f_update,"\nFinished: %s",c);
               fclose(f_update);
   
               /*------------------------------------------------------------------*/
               /* <selectCount> array contains the number of times a gene is being */
               /* selected. It is being updated as the search continues. Thus a    */
               /* copy of it needs to be made before it is sorted.                 */
               /*------------------------------------------------------------------*/
               for (i=0; i<numVariables; i++) {
                  countCopy[i].total=selectCount[i].total; 
                  countCopy[i].index=selectCount[i].index; 
               }
   
               sort_count(countCopy,numVariables);

               /* only output at most the top 500 genes, see ga_knn.h for details */
               if (numVariables>NUM_TOP_GENES)
                  output_rank_list(countCopy,expValue,numSamples,NUM_TOP_GENES,sample,variableName);
               else 
                  output_rank_list(countCopy,expValue,numSamples,numVariables,sample,variableName);

               if (application!=3) {
                  /* update test set prediction result */
                  f_update=fopen("predict_test_update.txt","w");
                  fprintf(f_update,"  number solutions obtained: %d\n",numSolutionObtained);
                  fprintf(f_update,"\n----------------test set [%d]------------------\n\n",iii+1);
                  fprintf(f_update," original classes: ");
                  for (j=0; j<numTesting; j++) fprintf(f_update,"%1d",sample[j+numTraining].class);
                  fprintf(f_update,"\n");        

                  /* if all num of solutions obtained = num of solutions specified */
                  if (numSolutionObtained==numSolutionsSpecified) {
                     fprintf(fq3,"\ntest set [%4d]:\n",iii+1);
                     fprintf(fq3,"      sample index: ");
                     for (j=0; j<numTesting; j++) fprintf(fq3,"%4d",sample[j+numTraining].id+1);
                     fprintf(fq3,"\n");
                     fprintf(fq3,"  original classes: ");
                     for (j=0; j<numTesting; j++) fprintf(fq3,"%4d",sample[j+numTraining].class);
                     fprintf(fq3,"\n");
                  }
               }
               else {
                  /* print out update every some solutions */
                  f_update=fopen("loocv_update.txt","w");
                  fprintf(f_update,"  number solutions obtained: %d\n",numSolutionObtained);
                  fprintf(f_update,"\n----------------left-out-sample[%d]------------------\n\n",numSamples-iii);
                  fprintf(f_update," original classes: ");
                  for (j=0; j<numTesting; j++) fprintf(f_update,"%1d",sample[j+numTraining].class);
                  fprintf(f_update,"\n");        

                  /* when all num of solutions obtained = num of solutions specified */
                  if (numSolutionObtained==numSolutionsSpecified) {
                     fprintf(fq3,"Note: class type 99: a sample can't be classified to a single class\n");
                     fprintf(fq3,"left-out-sample[%4d]:\n",numSamples-iii);
                     fprintf(fq3,"  original classes: ");
                     for (j=0; j<numTesting; j++)
                        fprintf(fq3,"%4d",sample[j+numTraining].class);
                     fprintf(fq3,"\n");
                  }
               }

               for (i=0; i<200; i++) {
                  numTopGenes =i+1;
                  if (numTopGenes>=numVariables) break;
                  distance_test(expValue,numTraining,numTesting,numTopGenes,countCopy,neighbors,knn);
                  predict_class(sample,numTesting,class,neighbors,knn,predictedClass,majorityRule);
   
                  if (application!=3) {
                     fprintf(f_update,"predicted classes: ");
                     for (j=0; j<numTesting; j++)
                        fprintf(f_update,"%1d",predictedClass[j]);
                     fprintf(f_update,"  top-ranked variables (gene, m/z)=%d\n",numTopGenes);
                     if (numSolutionObtained==numSolutionsSpecified) {
                         fprintf(fq3,"top[%3d] predicted: ",numTopGenes);
                        for (j=0; j<numTesting; j++)
                           fprintf(fq3,"%4d",predictedClass[j]);
                        fprintf(fq3,"\n");
                        fflush(fq3);
                     }
                     totalCorrectOneRun=0;
                     for (j=0; j<numTesting; j++) {
                        if (predictedClass[j]==sample[j+numTraining].class)
                           totalCorrectOneRun++; 
                     }
                     percentCorrectOneRun[i]=(double)totalCorrectOneRun/(double)numTesting;
                  }
                  else {
                     fprintf(f_update,"predicted classes: ");
                     for (j=0; j<numTesting; j++)
                        fprintf(f_update,"%1d",predictedClass[j]);
                     fprintf(f_update,"  number of top-ranked variables (genes or m/z) used in prediction: %d\n",numTopGenes);
                     if (numSolutionObtained==numSolutionsSpecified) {
                         fprintf(fq3,"top[%3d] predicted: ",numTopGenes);
                        for (j=0; j<numTesting; j++)
                           fprintf(fq3,"%4d",predictedClass[j]);
                        fprintf(fq3,"\n");
                        fflush(fq3);
                     }
                  }
               }
               fclose(f_update);
            }
         }
         else {
            printf("The maximal number of generations has reached. No solution is found!\n");
            printf("Make sure the termination cutoff (%d) is not too stringent.\n",target_R2);
         }
      } while (numSolutionObtained<numSolutionsSpecified);

      if (application==4) {
         fprintf(fq2,"split[%4d] top-ranked variables (genes,m/z) and percentage of correct prediction:\n",iii+1);
         for (i=0; i<200; i++) {
             if ((i+1)>=numVariables) break;
             fprintf(fq2,"%5d ",i+1);
         }
         fprintf(fq2,"\n");
         if (numTesting !=0) {
            for (i=0; i<40; i++) {
                if ((i+1)>=numVariables) break;
                fprintf(fq2,"%5.3f ",percentCorrectOneRun[i]);
            }
            fprintf(fq2,"\n");
            fflush(fq2);    
         }
      }
   }

   fclose(fq3);
   if (application==4)  fclose(fq2); 

   if (sample)              { destroy_sample(sample,numSamples);         }
   if (class)               { destroy_class(class);                      }
   if (neighbors[0])        { free(neighbors[0]);   neighbors[0]=NULL;   }
   if (neighbors)           { free(neighbors);      neighbors=NULL;      }
   if (fitness[0])          { free(fitness[0]);     fitness[0]=NULL;     }
   if (fitness)             { free(fitness);        fitness=NULL;        }
   if (niche)               { free(niche);          niche=NULL;          }
   if (weight)              { free(weight);         weight=NULL;         }
   if (predictedClass)      { free(predictedClass); predictedClass=NULL; }
   if (expValue[0])         { free(expValue[0]);    expValue[0]=NULL;    }
   if (expValue)            { free(expValue);       expValue=NULL;       }
   if (allChr[0][0])        { free(allChr[0][0]);   allChr[0][0]=NULL;   }
   if (allChr[0])           { free(allChr[0]);      allChr[0]=NULL;      }
   if (allChr)              { free(allChr);         allChr=NULL;         }
   if (bestChr)             { free(bestChr);        bestChr= NULL;       }
   if (selectCount)         { free(selectCount);    selectCount=NULL;    }
   if (variableName)            { free(variableName);       variableName=NULL;       }
   if (countCopy)           { free(countCopy);      countCopy=NULL;      }
   if (missingIndicator[0]) { free(missingIndicator[0]); missingIndicator[0]=NULL; }
   if (missingIndicator)    { free(missingIndicator);    missingIndicator=NULL;    }

   return (1);
}

/* this piece has been rewritten. It should be more robust than the previous one */
double **read_data(char *inputFile,char *dataFileName,int numSamples,
   int numVariables,char **variableName,SampleInfo *sample,char **missingIndicator) {

   FILE *fp;
   int columnCount,rowCount;
   int tabCount,missingCount,len,numWhiteSpace;
   int *tabPosition;
   double **expValue;
   char buffer[10000],tmp[10000];
   register int i,j,k;

   fp=fopen(dataFileName,"r");
   if (!fp) {
      perror(dataFileName); 
      printf("\nMake sure the data file Name and Location (path) are exactly\n");
      printf(" the same as they are specified in %s.\n\n",inputFile);
      exit(0); 
   }

    for (i=0; i<numSamples; i++) sample[i].id=i;
   /*-------------------------------------------------------------------*/
   /*                   reading sample name                             */
   /*-------------------------------------------------------------------*/
   tabPosition=alloc_int(numSamples+5);

   fgets(buffer,10000,fp);
   len=strlen(buffer);
   buffer[len-1]='\0';

   tabCount=0;
   for (i=0; i<len; i++) {
      if(buffer[i]=='\t') {
         tabPosition[tabCount]=i; tabCount++; 
      } 
   }

   if (tabCount !=numSamples) {
      printf("\nError: number of columns in the 1st row: %d\n",tabCount+1);
      printf("It should equal to %d (1+%d: the number of samples specified in file) [%s]\n",
         numSamples+1,numSamples,inputFile);
      printf("Check both input file[%s] and datafile[%s] for the discrepancy.\n\n",
         inputFile,dataFileName);
      exit(0);
   }

   columnCount=0; rowCount=0;
   for (i=1; i<tabCount; i++) {
      numWhiteSpace=0;
      tmp[0]='\0';
      for (k=0,j=tabPosition[i-1]+1; j<tabPosition[i]; j++,k++) {
         tmp[k]=buffer[j]; 
         if (tmp[k]==' ') numWhiteSpace++;
      }
      tmp[k]='\0';
      if (tmp[0]=='\0' || numWhiteSpace==tabPosition[i]-tabPosition[i-1]-1) {
         printf("No sample name: row[%d] column[%d]\n",rowCount+1,columnCount+2);
         strcpy(sample[columnCount].name,"SampleNameUnknown"); 
      }
      else
         strcpy(sample[columnCount].name,tmp);
      columnCount++;
   }

   numWhiteSpace=0;
   tmp[0]='\0';
   for (k=0,j=tabPosition[tabCount-1]+1; j<len; j++,k++) {
      tmp[k]=buffer[j]; 
      if (tmp[k]==' ') numWhiteSpace++;
   }
   tmp[k]='\0';

   if (tmp[0]=='\0' || numWhiteSpace==len-tabPosition[tabCount-1]-2) {
      printf("\nNo sample name: row[%d] column[%d]\n",rowCount+1,columnCount+1);
      strcpy(sample[columnCount].name,"SampleNameUnknown"); 
   }
   else
      strcpy(sample[columnCount].name,tmp);

   columnCount++; rowCount++;

   if (columnCount !=numSamples) {
      printf("Error: number of samples in %s (%d) and %s (%d) doesn't match\n",
          dataFileName,columnCount,inputFile,numSamples);
      exit(0); 
   }

   /*-------------------------------------------------------------------*/
   /*                   reading class type                              */
   /*-------------------------------------------------------------------*/

   fgets(buffer,10000,fp);
   len=strlen(buffer);
   buffer[len-1]='\0';

   tabCount=0;
   for (i=0; i<len; i++) {
      if(buffer[i]=='\t') {
         tabPosition[tabCount]=i; tabCount++; 
      } 
   }

   if (tabCount !=numSamples) {
      printf("\nError: number of columns in the 2nd row: %d\n",tabCount+1);
      printf("It should equal to 1+number of samples[%d] specified in file[%s]\n",
         numSamples,inputFile);
      printf("Check both input file[%s] and datafile[%s] for discrepancy.\n\n",
         inputFile,dataFileName);
      exit(0);
   }

   columnCount=0;
   for (i=1; i<tabCount; i++) {
      numWhiteSpace=0;
      tmp[0]='\0';
      for (k=0,j=tabPosition[i-1]+1; j<tabPosition[i]; j++,k++) {
         tmp[k]=buffer[j]; 
         if (tmp[k]==' ') numWhiteSpace++;
      }
      tmp[k]='\0';
      if (tmp[0]=='\0' || numWhiteSpace==tabPosition[i]-tabPosition[i-1]-1) {
         printf("\nNo class information: row[%d] column[%d]\n",rowCount+1,columnCount+2);
         exit(0); 
      }
      else 
         sscanf(tmp,"%d",&(sample[columnCount].class));
      columnCount++;
   }

   numWhiteSpace=0;
   tmp[0]='\0';
   for (k=0,j=tabPosition[tabCount-1]+1; j<len; j++,k++) {
      tmp[k]=buffer[j]; 
      if (tmp[k]==' ') numWhiteSpace++;
   }
   tmp[k]='\0';

   if (tmp[0]=='\0' || numWhiteSpace==len-tabPosition[tabCount-1]-2) {
      printf("\nNo class information: row[%d] column[%d]\n",rowCount+1,columnCount+1);
      exit(0);
   }
   else
      sscanf(tmp,"%d",&(sample[columnCount].class));

   columnCount++; rowCount++;

   if (columnCount !=numSamples) {
      printf("Error: number of samples in %s (%d) and %s (%d) doesn't match\n",
          dataFileName,columnCount,inputFile,numSamples);
      exit(0); 
   }

   printf("       Sample (array) name         Class\n");
   printf(" ----------------------------    -------------\n");
   for (i=0; i<numSamples; i++) {
      printf("   %20s\t%15d\n",sample[i].name,sample[i].class);
   }

   /*-------------------------------------------------------------------*/
   /*            reading variable (gene,m/z idenfier) name and expression data                  */
   /*-------------------------------------------------------------------*/
   expValue=alloc_double_double(numSamples,numVariables);

   for (i=0; i<numVariables; i++) {
      for (j=0; j<numSamples; j++)
        missingIndicator[i][j]='0'; 
   }

   missingCount=0; 
   while (!feof(fp)) { 
      if (fgets(buffer,10000,fp)) {
         len=strlen(buffer);
         buffer[len-1]='\0';

         tabCount=0;
         for (i=0; i<len; i++) {
            if(buffer[i]=='\t') {
               tabPosition[tabCount]=i;
               tabCount++; 
            } 
         }

         if (tabCount !=numSamples) {
            printf("\nError: number of columns in row[%d]: %d\n",rowCount+1,tabCount+1);
            printf("It should equal to 1+number of samples[%d] specified in file[%s]\n",
               numSamples,inputFile);
            printf("Check both input file[%s] and datafile[%s] for discrepancy.\n\n",
               inputFile,dataFileName);
            exit(0);
         }

         strncpy(variableName[rowCount-2],buffer,tabPosition[0]);
         columnCount=0;
         for (i=1; i<tabCount; i++) {
            numWhiteSpace=0;
            tmp[0]='\0';
            for (k=0,j=tabPosition[i-1]+1; j<tabPosition[i]; j++,k++) {
               tmp[k]=buffer[j]; 
               if (tmp[k]==' ') numWhiteSpace++;
            }
            tmp[k]='\0';
            if (tmp[0]=='\0' || numWhiteSpace==tabPosition[i]-tabPosition[i-1]-1) {
               printf("\nMissing value: row[%d] column[%d]\n",rowCount+1,columnCount+1);
               missingIndicator[rowCount-2][columnCount]='1';
               missingCount++;
            }
            else {
               sscanf(tmp,"%lf",&(expValue[columnCount][rowCount-2]));
            }
            columnCount++;
         }

         numWhiteSpace=0;
         tmp[0]='\0';
         for (k=0,j=tabPosition[tabCount-1]+1; j<len; j++,k++) {
            tmp[k]=buffer[j]; 
            if (tmp[k]==' ') numWhiteSpace++;
         }
         tmp[k]='\0';
 
         if (tmp[0]=='\0' || numWhiteSpace==len-tabPosition[tabCount-1]-2) {
            printf("\nMissing value: row[%d] column[%d]\n",rowCount+1,columnCount+1);
            missingIndicator[rowCount-2][columnCount]='1';
            missingCount++;
         }
         else {
            sscanf(tmp,"%lf",&(expValue[columnCount][rowCount-2]));
         }
         columnCount++;

         rowCount++; 
         if (rowCount>numVariables+2) {
            printf("Error: number of variables (genes,m/z) in file[%s] = %d and in file[%s] = %d\n",
                dataFileName,rowCount-2,inputFile,numVariables);
            printf("Make sure no extra empty line at the end of the file[%s]\n",dataFileName);
            exit(0);
         }
      } 
   };

   if (rowCount !=numVariables+2) {
       printf("number of variables (genes,m/z) in file[%s] = %d doesn't match that specified in %s = %d\n",
          inputFile,rowCount-2,dataFileName,numVariables);  
       exit(0); 
   }

   fclose(fp);
   printf("\nReading data file done...\n");
   printf("Number of missing values: %d\n\n",missingCount);

   if (missingCount>0) {
      printf("Each of the missing values will be replaced by the mean value\n");
      printf("of the class to which the missing sample belongs in the row.\n\n");
   }
   if (tabPosition) { free(tabPosition); tabPosition=NULL; }

   return (expValue);
}

int filteration(double **expValue,int numSamples,int numVariables,char **variableName,
   double cutoffThreshold) {

   register int i,j,k;
   int cn;
   double **value;
   char **name;

   value=alloc_double_double(numSamples,numVariables);
   name =alloc_char_char(numVariables,3000);

   k=0;
   for (i=0; i<numVariables; i++) {

      cn=0;
      for (j=0; j<numSamples; j++) {
         if (expValue[j][i]<cutoffThreshold) {
            expValue[j][i]=cutoffThreshold;
            cn++;
         }
      }
      if (cn<(int)(0.7*numSamples)) {
         strcpy(name[k],variableName[i]);
         for (j=0; j<numSamples; j++)
            value[j][k]=expValue[j][i];
         k++;
      }
   }

   printf("number of rows passed the cutoff: %5d\n",k);

   for (i=0; i<k; i++) {
      strcpy(variableName[i],name[i]);
      for (j=0; j<numSamples; j++)
         expValue[j][i]=value[j][i];
  }

   if (value[0]) { free(value[0]); value[0]=NULL; }
   if (value)    { free(value);    value=NULL;    }
   if (name[0])  { free(name[0]);  name[0]=NULL;  }
   if (name)     { free(name);     name=NULL;     }
   
   return (k);
}

void log_transform(double **expValue,int numSamples,int numVariables) {

   register int i,j;

   for (i=0; i<numVariables; i++) {
      for (j=0; j<numSamples; j++) {
         if  (expValue[j][i]<=0) {
            printf("\nCan't apply log-transformation to negative values or zero.\n");
            printf("Row[%d] Column[%d]\n\n",i+3,j+1);
            exit(0); 
         }
         else expValue[j][i]=log(expValue[j][i])/log(2.0);
      }
   }
}

void debug_print(double **expValue,int numSamples,int numVariables,char **variableName,
   SampleInfo *sample,int flag) {

   FILE *fp;
   register int i,j;

   if (flag==0)
      fp=fopen("debug_original.dat","w");
   else
      fp=fopen("debug_filtered.dat","w");

   fprintf(fp,"Sample\t");
   for (j=0; j<numSamples; j++) {
      if (j<numSamples-1)
         fprintf(fp,"%s\t",sample[j].name);
      else
         fprintf(fp,"%s\n",sample[j].name);
   }
   fprintf(fp,"Class\t");
   for (j=0; j<numSamples; j++) {
      if (j<numSamples-1)
         fprintf(fp,"%1d\t",sample[j].class);
      else
         fprintf(fp,"%1d\n",sample[j].class);
   }
   for (i=0; i<numVariables; i++) {
      fprintf(fp,"%s\t",variableName[i]);
      for (j=0; j<numSamples; j++) {
         if (j<numSamples-1)
            fprintf(fp,"%5.3f\t",expValue[j][i]);
         else
            fprintf(fp,"%5.3f\n",expValue[j][i]);
      }
   }
   fclose(fp);
}

Class *assign_class(SampleInfo *sample,int numSamples) {
 
   register int i,j;
   int used,cmax,cmin,numclass;
   int *cn;
   Class *class;

   class=alloc_class(numSamples);

   numclass=0;
   for (i=0; i<numSamples; i++) {
      used=0;
      for (j=0; j<numclass; j++) {
         if (class->type[j]==sample[i].class)  {
            used=1; break;
         }
      }
      if (!used) {
         class->type[numclass]=sample[i].class;
         numclass++;
      }
   }

   cn=alloc_int(numclass);
   for (j=0; j<numclass; j++) cn[j]=0;

   for (i=0; i<numSamples; i++) {
      for (j=0; j<numclass; j++) {
         if (sample[i].class==class->type[j]) {
            class->which[j][cn[j]]=i;
            (cn[j])++; break;
         }
      }
   }
   for (j=0; j<numclass; j++)
      class->count[j]=cn[j];
   class->num_class=numclass;
 
   if (cn) { free(cn); cn=NULL; }

   cmin=9999; cmax=0;
   for (j=0; j<numclass; j++) {
      if (class->count[j]<cmin) cmin=class->count[j];
      if (class->count[j]>cmax) cmax=class->count[j];
   }

   class->max=cmax; class->min=cmin;

   printf("\nTotal number of classes: %4d, individual class type: ",numclass);
   for (i=0; i<numclass; i++)
      printf("%3d ",class->type[i]);
   printf(".\n");
   printf("Number of samples in each class: ");
   for (j=0; j<numclass; j++)
      printf("%3d [type - %1d] ",class->count[j],class->type[j]);
   printf("min max=%4d %4d\n",class->min,class->max);
   printf("\n");

  for (i=0; i<class->num_class; i++) {
      printf("class[%d]: ",class->type[i]);
      for (j=0; j<class->count[i]; j++) {
         printf("%4d ",class->which[i][j]+1);
      } printf("\n");
   }

   if (numclass==1) {
      printf("\n\tThe GA/KNN algorithm selects the most discriminative variables for classification.\n");
      printf("The data must consist of at least two classes of samples (e.g, normal vs tumor). It is\n");
      printf("capable of handling multiple classes\n");
      printf("\tYour data has only one class...thanks for trying... Goodbye...\n\n");
      exit(0);
   }
   return (class);
}

void missing_value_impute(double **expValue,Class *class,SampleInfo *sample,
   char **missingIndicator,int numVariables,int numSamples) {

   register int i,j,k,l;
   int numMissing,inMissing,cn,id;
   int *whichSample;
   double classSum;

   whichSample=alloc_int(numSamples);

   for (i=0; i<numVariables; i++) {
      numMissing=0;
      for (j=0; j<numSamples; j++) {
         if (missingIndicator[i][j]=='1') {
	    whichSample[numMissing]=j; numMissing++;
         }
      }
      for (j=0; j<numMissing; j++) {
         id=-1; cn=0; classSum=0;
         for (k=0; k<class->num_class; k++) {
            if (sample[whichSample[j]].class==class->type[k]) {
               id=k; break; 
            }
         }
	 for (k=0; k<class->count[id]; k++) {
            inMissing=0;
            for (l=0; l<numMissing; l++) {
               if (class->which[id][k]==whichSample[l]) {
                   inMissing=1; break; 
               }
            }
	    if (!inMissing) {
               classSum +=expValue[class->which[id][k]][i];
	       cn++;
            }
         }
	 if (cn<class->count[id]/2) {
            printf("\nToo many missing cells in a row[%d] for class[%d]\n",i+3,class->type[id]);
	    printf("A quick and dirty way can't fix it -:)...\n");
	    printf("You may want to remove the row[%d] and try it again...\n\n",i+3);
	    exit(0);
         }
	 else {
	    classSum /=(double)cn;
            expValue[whichSample[j]][i]=classSum; 
         }
      }
   }
}

void initialize_chr(int populationSize,int numVariables,int chromosomeLength,
   int ***allChr,int numNiches) {

   register int i,j,k,l;
   int total;
   int **chr;
   int *tmp1;
 
   chr =alloc_int_int(numNiches*populationSize,chromosomeLength); 
   tmp1=alloc_int(chromosomeLength);
  
   if (chromosomeLength==numVariables) {
      printf("\nNote: chromosome length equals to number of variables (genes,m/z).\n");
      printf("!!!All chromosomes will be identical!!!\n\n");
      for (i=0; i<numNiches; i++) {
         for (j=0; j<populationSize; j++) {
            for (k=0; k<numVariables; k++)
               *(*(*(allChr+i)+j)+k)=k; 
         }
      }    
   }

   else {
      total=0;
      do {
         random_selection(tmp1,chromosomeLength,numVariables);
         for (k=0; k<chromosomeLength; k++)
             *(*(chr+total)+k)=*(tmp1+k); 
         total++;
      } while (total<numNiches*populationSize);

      for (i=0; i<numNiches; i++) {

         l=i*populationSize;
         for (j=0; j<populationSize; j++) {
            for (k=0; k<chromosomeLength; k++) {
               *(*(*(allChr+i)+j)+k)=*(*(chr+l+j)+k);
            }
         }
      }
   }
   /* printf("generating initial population of chromosomes - done...\n"); */

   if (tmp1)   { free(tmp1);   tmp1=NULL;   }
   if (chr[0]) { free(chr[0]); chr[0]=NULL; }
   if (chr)    { free(chr);    chr=NULL;    }
}

void random_selection(int *which,int chromosomeLength,int numVariables) {
   
   register int i;
   int numFilled,dummy,used;

   numFilled=0;
   for (i=0; i<chromosomeLength; i++) *(which+i)=-1;

   while (numFilled<chromosomeLength) {
      dummy=(int)(numVariables*random_gen());
      if (dummy == numVariables) dummy--;
      used=0;
      for (i=0; i<numFilled; i++) {
         if (dummy==*(which+i)) {
            used=1; break;
         }
      }
      if (!used) {
         *(which+numFilled)=dummy;
         numFilled++;
      }
   }
}

/*------------------------------------------------------------------------
   Note: this subroutine and the sort_fitness subroutine maximize
   the fitness score, not minimize it. If you want to achieve the
   opposite (minimization), modify the following:

   int roulett_wheel:
   range = fitness[populationSize-1]-fitness[0].value;
   for (i=0; i<populationSize; i++) {
      scaledScore[i] = 1-(fitness[i].value-fitness[0].value)/range;
      totalScore += scaledScore[i];
   }

   in sort_fitness:
   int Compare_fitness(const void *s1, const void *s2) {

      if (((Fitness *)s1)->value<((Fitness *)s2)->value) { return -1; }
      if (((Fitness *)s1)->value>((Fitness *)s2)->value) { return  1; }
         return 0;
   }
------------------------------------------------------------------------*/

void roulett_wheel(Fitness *fitness,int populationSize,Wheel *wheel) {

   register int i;
   double totalScore,worstScore,range,area;
   double *scaledScore;

   worstScore=fitness[populationSize-1].value;
   range=fitness[0].value-worstScore;

   /*---------------------------------------------------*/
   /* if the first and last chromosomes (after sorting) */
   /* have same score, GA converged                     */
   /*---------------------------------------------------*/
   if (range<0.00001) printf("GA converged ...\n"); 

   else {
      scaledScore=alloc_double(populationSize);

      totalScore=0;
      for (i=0; i<populationSize; i++) {
         /* range scale */
         scaledScore[i] = (fitness[i].value-worstScore)/range;
         totalScore += scaledScore[i];
      }
      for (i=0; i<populationSize; i++) scaledScore[i] /= totalScore;

      area=100.0*scaledScore[0];
      wheel[0].start=0;
      wheel[0].end  =area;
      wheel[0].index=fitness[0].index;

      for (i=1; i<populationSize; i++) {
         wheel[i].start=wheel[i-1].end;
         area=100.0*scaledScore[i];
         wheel[i].end=area+wheel[i].start;
         wheel[i].index=fitness[i].index;
      }

      if (scaledScore) { free(scaledScore); scaledScore = NULL; }
      /*---------------------------------------------------------
      for (i=0; i<populationSize; i++) {
         printf("start, end and index %5.1f\t%5.1f\t%4d\n", wheel[i].start,
            wheel[i].end, wheel[i].index);
      }
      ---------------------------------------------------------*/
   }
}

/* qsort */
void sort_fitness(Fitness *fitness,int size) {

   int (*compar)(const void *,const void *);

   compar=Compare_fitness;
   qsort((void *)fitness,(size_t)size,sizeof(Fitness),compar);
}

int Compare_fitness(const void *s1, const void *s2) {

   if (((Fitness *)s1)->value < ((Fitness *)s2)->value) { return  1; }
   if (((Fitness *)s1)->value > ((Fitness *)s2)->value) { return -1; }
      return 0;
}

void sort_count(Solution *count,int size) {

   int (*compar)(const void *,const void *);

   compar = Compare_count;
   qsort((void *)count,(size_t)size,sizeof(Solution),compar);
}

/* decending order */
int Compare_count(const void *s1, const void *s2) {

   if (((Solution *)s1)->total < ((Solution *)s2)->total) { return  1; }
   if (((Solution *)s1)->total > ((Solution *)s2)->total) { return -1; }
      return 0;
}

void predict_class(SampleInfo *sample,int numSamples,Class *class,
   Neighbor **neighbors,int original_knn,int *pred_act,int majorityRule) {

   register int i,j,k;
   int which,knn,id1,cmax;
   int *count;
   int found;

   count=alloc_int(class->num_class);

   knn=original_knn;
   for (i=0;i<numSamples;i++) {

      for (k=0;k<class->num_class;k++) *(count+k)=0;

      /* find out how many classes the k neighbors have */
      for (j=0; j<knn; j++) {
         which=neighbors[i][j].id;
         for (k=0; k<class->num_class; k++) {
            if (sample[which].class == class->type[k]) {
               (count[k])++; break; 
            }
         }
      }
      if (majorityRule==0) {
         found=0;
         for (k=0; k<class->num_class; k++) {
            if (*(count+k)==knn) { 
               *(pred_act+i)=class->type[k]; found=1; break; 
            } 
         }
         if (!found)
            *(pred_act+i)=UNCLASSIFIABLE;
      }
      else {
         cmax=0; id1=-1;
         for (k=0; k<class->num_class; k++) {
             if (*(count+k)>cmax) { cmax=*(count+k); id1=k; }
         }

         if (cmax >= (int)(ceil((double)knn/2.0)))
            *(pred_act+i)=class->type[id1]; 
         else
            *(pred_act+i)=UNCLASSIFIABLE;
      }
   }
   if (count) { free(count); count =NULL; }
}

double cal_fitness(SampleInfo *sample,int *pred_act,int numSamples) {

   register int i;
   double score;

   score=0.0;
   for (i=0; i<numSamples; i++) {
      if (pred_act[i]==sample[i].class) score += 1.0; 
   }
   return (score);
}

/*-----------------------------------------------------------*/
/* pair wise between each obj and its KNN neighbbors         */ 
/* similarity using the selected features.                   */
/*-----------------------------------------------------------*/
void distance(double **expValue,int *which1,int chromosomeLength,
   int numSamples,Neighbor **neighbors,int knn) {

   register int i,j,k;
   double sum; 

   for (i=0; i<numSamples; i++) {
      (*(*(neighbors+i)+i)).dis=99999.0;
      (*(*(neighbors+i)+i)).id=i;
   }
   
   for (i=0; i<numSamples-1; i++) {
      for (j=i+1; j<numSamples; j++) {

         sum=0.0;
         for (k=0; k<chromosomeLength; k++) {
            sum += (expValue[i][which1[k]]-expValue[j][which1[k]])* 
                   (expValue[i][which1[k]]-expValue[j][which1[k]]);
         }
         neighbors[i][j].dis=sqrt(sum); 
         neighbors[i][j].id =j;
         neighbors[j][i].dis=neighbors[i][j].dis;
         neighbors[j][i].id =i;
      } 
   }

   /* sort the distance in ascending order */
   for (i=0; i<numSamples; i++)
      shaker_sort_ascen(neighbors[i],numSamples,knn+1);
}

void distance_test(double **expValue,int numTraining,int numTesting,
   int numTopGenes,Solution *countCopy,Neighbor **neighbors,int knn) {

   register int i,j,k,l;
   double sum; 

   for (i=0,l=numTraining; i<numTesting; i++,l++) {
      for (j=0; j<numTraining; j++) {

         sum=0.0;
         for (k=0; k<numTopGenes; k++) {
            sum += (expValue[l][countCopy[k].index]-expValue[j][countCopy[k].index])* 
                   (expValue[l][countCopy[k].index]-expValue[j][countCopy[k].index]);
         }
         neighbors[i][j].dis=sqrt(sum); 
         neighbors[i][j].id =j;
      }
   }

   for (i=0; i<numTesting; i++) {
      shaker_sort_ascen(neighbors[i],numTraining,knn+1);
      /*-----------------------------------------------
      for (j=0; j<knn; j++)
         printf("test %d: %d[%lf]",i+1,neighbors[i][j].id,neighbors[i][j].dis);  
      -----------------------------------------------*/
   }   
}

void center_array(double **expValue,int numSamples,int numVariables) {

   int i,j;
   double median,*gene=NULL;

   gene=alloc_double(numVariables);

   for (i=0; i<numSamples; i++) {
      for (j=0; j<numVariables; j++)
         *(gene+j)=*(*(expValue+i)+j);

      shaker_double_ascent(gene,numVariables);

      if (numVariables%2==0)
         median=(gene[(numVariables-2)/2]+gene[numVariables/2])/2.0;
      else
         median=gene[(numVariables-1)/2];

      printf("median for array[%3d]:   %8.3f\n",i+1,median);

      for (j=0; j<numVariables; j++)
         *(*(expValue+i)+j) -= median;
   }
   printf("\n");

   if (gene) { free(gene); gene=NULL; }
}

void output_rank_list(Solution *count,double **expValue,int numSamples,
   int numVariables,SampleInfo *sample,char **variableName) {

   FILE *fp;
   register int i,j;

   fp=fopen("variable_ranked_by_GA_KNN.txt","w");

   fprintf(fp,"Sample\t");
   for (i=0; i<numSamples; i++) {
      if (i<(numSamples-1))
         fprintf(fp,"%s\t",sample[i].name);
      else
         fprintf(fp,"%s\n",sample[i].name); 
   }

   for (i=0; i<numVariables; i++) {
       fprintf(fp,"%s\t",variableName[count[i].index]);
      for (j=0; j<numSamples; j++) {
         if (j<(numSamples-1))
            fprintf(fp,"%6.3f\t",expValue[j][count[i].index]);
         else
            fprintf(fp,"%6.3f\n",expValue[j][count[i].index]);
      }
   }
   fclose(fp);
}

/* standardization - z transformation */
void autoscale(double **expValue,int numSamples,int numVariabless) {

   int i,j;
   double ave,sd;

   for (i=0; i<numVariabless; i++) {
      ave=0.0;
      for (j=0; j<numSamples; j++) {
         ave += *(*(expValue+j)+i);
      }
      ave /= (double)numSamples;

      sd=0.0;
      for (j=0; j<numSamples; j++)
         sd += (expValue[j][i]-ave)*(expValue[j][i]-ave);

      sd=sqrt(sd/(double)(numSamples-1));
      if (sd<0.01) {
         for (j=0; j<numSamples; j++)
            expValue[j][i]=0.0;
      }
      else {
         for (j=0; j<numSamples; j++)
            expValue[j][i] = (expValue[j][i]-ave)/sd;
      }
   }
}

void range_scale(double **expValue,int numSamples,int numVariables) {

   register int i,j;
   double *tmp;

   tmp=alloc_double(numSamples);

   for (i=0; i<numVariables; i++) {
      for (j=0; j<numSamples; j++) tmp[j] = expValue[j][i];
      shaker_double_ascent(tmp,numSamples);
      for (j=0; j<numSamples; j++)
          expValue[j][i]=(expValue[j][i]-tmp[0])/(tmp[numSamples-1]-tmp[0]);
   }
   if (tmp) { free(tmp); tmp=NULL; }
}

/*--------------------------------------------------------*/
/*  random selection of sample id without replacement     */
/*  the number of samples in each class in both training  */
/*  and test sets are proportional.                       */
/*--------------------------------------------------------*/
void splitIntoTrainingTest(double **expValue,int numVariables,SampleInfo *sample,
   int numSamples,Class *class,int numTraining) {

   register int i,j,k;
   int cn,cn2,du,numSoFar,numNeeded,found,used;
   int *wh;
   double **tmp_value;
   SampleInfo *tmp_sample;

   wh=alloc_int(numSamples);
   tmp_value=alloc_double_double(numSamples,numVariables);
   tmp_sample=alloc_sample(numSamples);

   for (i=0; i<numSamples; i++) wh[i]=-1;
   cn=0;
   for (i=0; i<class->num_class; i++) {

      /*---------------------------------------------------*/ 
      /* allow the number of samples in both training and  */
      /* test sets are proportional                        */
      /*---------------------------------------------------*/ 
      numNeeded=(int)(class->count[i]*(double)numTraining/(double)numSamples);
      numSoFar=0;
      while(numSoFar<numNeeded) {
         du=(int)(class->count[i]*random_gen());
         if (du==class->count[i]) du--;
         used=0;
         for (k=0; k<cn; k++) {
            if (class->which[i][du]==wh[k])  { used=1; break; }
         }
         if (!used) {
            wh[cn]=class->which[i][du];
            cn++; numSoFar++;
         }
      };
   }

   cn2=0;
   for (i=0; i<numSamples; i++) {
      found=0;
      for (j=0; j<cn; j++) {
         if (wh[j]==i) { found=1; break;  }
      }
      if (!found) {
         wh[cn+cn2]=i; cn2++;
      }
   }
   printf("total number of samples in test set: %d\n",cn2);
                                                                                   
   for (i=0; i<numSamples; i++) {                                                  
      for (j=0; j<numVariables; j++)                                                   
         tmp_value[i][j] = expValue[wh[i]][j];                                     
      tmp_sample[i].id   = sample[wh[i]].id;                                       
      tmp_sample[i].class= sample[wh[i]].class;                                    
      strcpy(tmp_sample[i].name,sample[wh[i]].name);                               
   }                                                                               
                                                                                   
   for (i=0; i<numSamples; i++) {                                                  
      for (j=0; j<numVariables; j++)     
         expValue[i][j] = tmp_value[i][j];
      sample[i].id      = tmp_sample[i].id;
      sample[i].class   = tmp_sample[i].class;
      strcpy(sample[i].name,tmp_sample[i].name);
   }

   if (wh)           { free(wh);           wh=NULL;           }
   if (tmp_value[0]) { free(tmp_value[0]); tmp_value[0]=NULL; }
   if (tmp_value)    { free(tmp_value);    tmp_value=NULL;    }
   if (tmp_sample)   { destroy_sample(tmp_sample,numSamples); }

   /* re-assign class will be carried out */
   if (class)        { destroy_class(class);                  }
}

/* move the one to be predicted to the bottom of the data array */
void move_LOO_bottom(double **expValue,int numVariables,SampleInfo *sample,
   int numSamples,int which) {

   register int j;
   int tmp_class,tmp_id;
   double tmp_value;
   char *tmp_name;

   tmp_name=alloc_char(MAX_NUM_CHAR);
   for (j=0; j<numVariables; j++) {
      tmp_value=expValue[numSamples-1][j];
      expValue[numSamples-1][j]=expValue[numSamples-which-1][j];
      expValue[numSamples-which-1][j]=tmp_value;
   }
   tmp_class=sample[numSamples-1].class;
   sample[numSamples-1].class=sample[numSamples-which-1].class;
   sample[numSamples-which-1].class=tmp_class;

   tmp_id=sample[numSamples-1].id;
   sample[numSamples-1].id=sample[numSamples-which-1].id;
   sample[numSamples-which-1].id=tmp_id;

   strcpy(tmp_name,sample[numSamples-1].name);
   strcpy(sample[numSamples-1].name,sample[numSamples-which-1].name);
   strcpy(sample[numSamples-which-1].name,tmp_name);

   if (tmp_name) { free(tmp_name); tmp_name=NULL; }
}

void check_knn(int numSamples,int class_min,int knn,int application) {

   if (application==3) {
      if (knn>class_min-2) {
         printf("\nFor leave-one-out, KNN must not be larger than the number of\n");
         printf("   samples in the smallest class -2. In your case, the smallest\n");
         printf("   class has %d samples, leaving-one-out leads to %d samples\n",class_min,class_min-1);
         printf("   left with %d neighbor(s) of the same type. The maximal\n",class_min-2);
         printf("   value of KNN can have for this data set is: %d.\n\n",class_min-2);
         printf("Please change the KNN in the input parameter file accordingly.\n\n");  exit(0);
      }
   }
   else if (application==4) {
      if (class_min<6 || numSamples<20) {
         printf("\nFor a small data set of less than 20 samples with two or more\n");
         printf("   classes, one should use a leave-one-out cross-validation instead\n");
         printf("   of leave-many-out. One should also use leave-one-out when one class\n");
         printf("   has only a few samples even the overall sample size is large.\n\n");
      }
   }
   if (knn>class_min-1) {
      printf("\nKNN must not be larger than the number of samples in the smallest\n"); 
      printf("   class -1. In your case, the smallest class has %d samples. Thus,\n",class_min); 
      printf("   the maximal value KNN can have is: %d.\n\n",class_min-1);
      printf("Please change the KNN in the input parameter file accordingly.\n\n");  exit(0);
   }
}
 
void rank_by_variation(double **expValue,int numSamples,int numVariables,
   SampleInfo *sample,char **variableName) {

   register int i,j,id;
   double ave,sd;
   Fitness *variation;
   FILE *fp;

   variation=(Fitness *)calloc(numVariables,sizeof(Fitness ));

   for (i=0; i<numVariables; i++) {
      ave=0;
      for (j=0; j<numSamples; j++) ave +=expValue[j][i]; 
      ave /=(double)numSamples;
      sd=0;
      for (j=0; j<numSamples; j++) {
         sd +=(expValue[j][i]-ave)*(expValue[j][i]-ave); 
      }
      variation[i].value=sd;
      variation[i].index=i;
   }
   sort_fitness(variation,numVariables);

   fp=fopen("variation_ranked.dat","w");

   fprintf(fp,"Sample\t");
   for (j=0; j<numSamples; j++) {
      if (j<numSamples-1)
         fprintf(fp,"%s\t",sample[j].name);
      else
         fprintf(fp,"%s\n",sample[j].name);
   }
   fprintf(fp,"Class\t");
   for (j=0; j<numSamples; j++) {
      if (j<numSamples-1)
         fprintf(fp,"%1d\t",sample[j].class);
      else
         fprintf(fp,"%1d\n",sample[j].class);
   }
   for (i=0; i<numVariables; i++) {
      id=variation[i].index;
      /* if (atof(variableName[id])<=2000) continue;  for m/z data */
      fprintf(fp,"%s\t",variableName[id]);
      for (j=0; j<numSamples; j++) {
         if (j<numSamples-1)
            fprintf(fp,"%5.3f\t",expValue[j][id]);
         else
            fprintf(fp,"%5.3f\n",expValue[j][id]);
      }
   }
   fclose(fp);
   if (variation) { free(variation); variation=NULL; }
}
