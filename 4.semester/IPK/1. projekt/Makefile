CC = gcc
CFLAGS = -std=c99 -Wall -Wextra -pedantic -g
BIN = webclient

project: client.o
	$(CC) client.o -o $(BIN)

client.o: client.c
	$(CC) -c $(CFLAGS) $*.c

clean:
	rm -f *.o *.d $(BIN)

# zamže .o súbory
clean_o:
	rm -f *.o
