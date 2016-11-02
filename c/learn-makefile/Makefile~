CC=gcc
CFLAGS=-I .
DEPS=hellomake.h

%.o : %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)	
hellomake : hellomake.o hellofun.o
	$(CC)  -o hellomake  hellomake.o hellofun.o $(CFLAGS)
