CC = gcc
CFLAGS = -std=c99 -Wall -Wextra -pedantic -g

all: client server

client: client.c
	$(CC) $(CFLAGS) client.c -o client

server: server.c
	$(CC) $(CFLAGS) server.c -o server
