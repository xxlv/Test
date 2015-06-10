
#define  min(a,b)          (((a)<(b))?(a):(b))
#define  max(a,b)          (((a)>(b))?(a):(b))
#define  mod(a,b)          ((a)-(b)*((a)/(b)))
#define  UNCLASSIFIABLE     99
#define  MAX_NUM_CHAR       3000 /* maximal number of chars    */
#define  NUM_TOP_GENES      1000  /* number of top-ranked genes will be outputed */

typedef struct neighbor_info Neighbor;
struct neighbor_info {
   double dis;   /* distance to the neighbor */
   int    id;    /* neighbor's id            */
};

typedef struct classification Class;
struct classification {
   int *type,*count;  /* class type and no. of samples in the class */
   int **which;       /* sample IDs in each class                   */
   int num_class;
   int min,max;
};

typedef struct sample_info SampleInfo;
struct sample_info {
   char *name;
   int class,id;
};

typedef struct score Fitness;
struct score {
   double value;
   int    index;
};

typedef struct roulette Wheel;
struct roulette {
   double start,end;
   int index;
};

typedef struct solution_info Solution;
struct solution_info {
   int total,index;
};

int Compare_fitness(const void *, const void *);
int Compare_count(const void *s1, const void *s2);
int **alloc_int_int (int ,int );
int *alloc_int (int );
int ***alloc_int_int_int(int ,int ,int );
int which_chromosome(Wheel *,int );
int num_mutated(int );
int filteration(double **,int ,int ,char **,double );

double **alloc_double_double(int ,int );
double *alloc_double(int );
double random_gen(void);
double cal_fitness(SampleInfo *,int *, int );
double **read_data(char *,char *,int ,int ,char **,SampleInfo *,char **);

void sort_fitness(Fitness *,int );
void shuffle_class(SampleInfo *,int );
void random_selection(int *,int ,int );
void shaker_sort_ascen(Neighbor *,int ,int );
void destroy_sample(SampleInfo *,int );
void destroy_class(Class *);
void distance(double **,int *,int ,int ,Neighbor **,int );
void random_initialize(int );
void mutation(int **,int ,int ,int ,Wheel *);
void initialize_chr(int ,int ,int ,int ***,int );
void swap_genes(int *,int ,int ,int *);
void which_gene0(int *,int ,int *,int ,int );
void shaker_double_ascent(double *,int );
void predict_class(SampleInfo *,int ,Class *,Neighbor **,int ,int *,int );
void center_genes(double **,int ,int );
void center_arrays(double **,int ,int );
void roulett_wheel(Fitness *,int ,Wheel *);
void sort_count(Solution *count,int size);
void output_rank_list(Solution *,double **,int ,int ,SampleInfo *,char **);
void center_array(double **,int ,int );
void distance_test(double **,int ,int ,int ,Solution *,Neighbor **,int );
void autoscale(double **,int ,int );
void range_scale(double **expValue,int numSamples,int numVariables);
void splitIntoTrainingTest(double **,int ,SampleInfo *,int ,Class *,int );
void move_LOO_bottom(double **,int ,SampleInfo *,int ,int );
void check_knn(int ,int ,int knn,int );
void selectGenesNotOnChr(int *,int ,int *,int ,int );
void log_transform(double **,int ,int );
void debug_print(double **,int ,int ,char **,SampleInfo *,int );
void missing_value_impute(double **,Class *,SampleInfo *,char **,int ,int );

char *alloc_char(int );
char **alloc_char_char(int ,int );

SampleInfo *read_class(char *,int ,Class *);
Class *alloc_class(int );
Class *assign_class(SampleInfo *,int );
Fitness **alloc_fitness_fitness(int ,int );
Neighbor **alloc_neighbors(int ,int );
Solution *alloc_solution(int size);
SampleInfo *alloc_sample(int );
Fitness *alloc_fitness (int );
Wheel *alloc_wheel (int );
