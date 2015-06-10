#
#        For questions, send email to Leping Li at Li3@niehs.nih.gov          #          
#                                                                             #
#-----------------------------------------------------------------------------#
# if you have a gcc compiler, set CC = gcc (default here)                     #
# if have a C compiler, set CC = cc                                           #
#-----------------------------------------------------------------------------#

CC = gcc
CFLAGS = -g -Wall
OBJS = main.o mutation.o  shaker_sort.o random.o alloc.o
EXE  = ga_knn
LIBS = -lm

all : ga_knn finish

ga_knn:	main.o  mutation.o  shaker_sort.o random.o alloc.o
	$(CC) $(CFLAGS) -o ga_knn main.o alloc.o \
	mutation.o  shaker_sort.o random.o $(LIBS)

main.o:		main.c ga_knn.h 
#		$(CC) $(CFLAGS) -c main.c -DRANK_BY_VARIATION
		$(CC) $(CFLAGS) -c main.c

random.o:	random.c
		$(CC) $(CFLAGS) -c random.c 

shaker_sort.o:	shaker_sort.c ga_knn.h
		$(CC) $(CFLAGS) -c shaker_sort.c 

mutation.o:	mutation.c ga_knn.h
		$(CC) $(CFLAGS) -c mutation.c 

alloc.o:	alloc.c ga_knn.h
		$(CC) $(CFLAGS) -c alloc.c

finish:
	@echo  "all done"
clean:
	rm ${OBJS} ${EXE}

