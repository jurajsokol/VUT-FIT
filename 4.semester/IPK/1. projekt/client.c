/* IPK 1. projekt
** autor: Juraj Sokol
*/

#define BUFFERSIZE 1024

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <unistd.h>


int s(char *host_url, int jump, char* file){
  int port = 80;
  int soc; // obsahuje cislo soketu
  struct sockaddr_in s_address;
  struct hostent *host;
  struct in_addr **addr_list;
  char* get = NULL;
  int response;

  if (file == NULL){
    file = malloc(sizeof("index.html"));
    if(file == NULL){
      fprintf(stderr, "Malloc error\n");
      return 1;
    }
    memcpy(file, "index.html", strlen("index.html"));
  }
  if (jump > 5){
    fprintf(stderr, "Too many redirections.\n");
    return 1;
  }

  // spracovanie vstupných parametrov
  char *hs;
  char *u;
  char *url;
  char *p;

  // odstráni http:// ak sa tam nachádza
  if(memcmp(host_url, "http://", 7) == 0){
    hs = malloc(sizeof(host_url));
    memcpy(hs, host_url+7, strlen(host_url)); // +7 vynechá začiatok
  }
  else{
    hs = host_url;
  }

  // rozdelí adresu
  u = strchr(hs, '/');

  if(u == 0){
    url = malloc(sizeof(char)*2);
    if(url == NULL){
      fprintf(stderr, "Malloc error\n");
      return 1;
    }
    url[0] = '/';
    url[1] = '\0';
  }
  if(u != NULL){
    url = malloc(sizeof(*u)+1);
    if(url == NULL){
      fprintf(stderr, "Malloc error\n");
      return 1;
    }
    memcpy(u, url, sizeof(*u));

  }

  printf("%s\n", url);
  if(u != NULL){
    hs[strlen(hs) - strlen(url)] = '\0';
    url[strlen(url)] = '\0';
  }

  if(strlen(url) == 0){
    url[0] = '/';
    url[1] = '\0';
  }

  p = strchr(hs, ':');
  if(p != NULL){
    port = atoi(p+1);
    hs[strlen(hs) - strlen(p)] = '\0';
  }
  printf("port: %d,\nhost: %s,\nurl: %s\n", port, hs, url);

  // vytvorí soket
  soc = socket(PF_INET, SOCK_STREAM, 0);
	if (soc < 0) {
		fprintf(stderr, "Socket error.\n");
		return 1;
	}

  bzero(&s_address, sizeof(s_address)); // vynulovanie štruktúry
	s_address.sin_family = AF_INET; // piradnie typu rodiny

  // ziskanie ip aresy zmena
	if ((host = gethostbyname(hs)) == NULL) {
		fprintf(stderr, "Unknown host.\n");
		return 1;
	}

  // print information about this host:
    printf("Official name is: %s\n", host->h_name);
    printf("<--- IP addresses --->\n");
    addr_list = (struct in_addr **)host->h_addr_list;
    for(int i = 0; addr_list[i] != NULL; i++) {
        printf("    %s\n", inet_ntoa(*addr_list[i]));
    }
    printf("\n");

    memcpy(&s_address.sin_addr, host->h_addr_list[0], host->h_length); //adresa
  	s_address.sin_port = htons(port);

  	if (connect(soc, (struct sockaddr*) &s_address, sizeof(s_address)) < 0) {
  		fprintf(stderr, "D'OH! Server is dead.\n");
  		return 1;
  	}
    	printf("Connected!\n\n");

  get = malloc((strlen("GET /") + strlen(url) + strlen(" HTTP/1.0\r\nHost: ") + strlen(hs) + strlen("\r\n\r\n")) * sizeof(char));
	if (get == NULL) {
		fprintf(stderr, "Malloc error\n");
		return 1;
	}

  // vytorenie requetu
  get[0] = '\0';
	strcat(get, "GET ");
	strcat(get, url);
	strcat(get, " HTTP/1.0\r\nHost: ");
	strcat(get, host->h_name);
	strcat(get, "\r\n\r\n");
	printf("%s\n", get);

  if ((write(soc, get, strlen(get))) <= 0) {
    fprintf(stderr, "Write error.\n");
  	return 1;
  }

  //free(get);

  printf("Reading...\n");
  char* header;
  int alloc_size = 1;
  int i = 0;
  header = malloc(sizeof(char)*alloc_size);
  if (header == NULL) {
  	fprintf(stderr, "Allocation error\n");
  	return 1;
  }
  while (strstr(header, "\r\n\r\n") == NULL) {
  	read(soc, header + i, 1);
  	i++;
  	if (i >= alloc_size) {
      alloc_size *= 2;
  		header = realloc(header, sizeof(char)*alloc_size);
  		if (header == NULL) {
  		  fprintf(stderr, "Reallocation error\n");
        return 1;
  		}
  	}
  }
  printf("Header:\n%s", header);

  response = atoi(header + 9);
	printf("Response: %d\n", response);
  char *location = NULL;
  if (response >= 300 && response < 400) {
		location = strstr(header, "Location: ") + strlen("Location: ");
		i = 0;
		char *l = strchr(location, '\r');
		l[0] = '\0';
		//printf("\nLocation: %s\n", location);
	}

    // úspešný response
    if(response == 200){
      printf("Downloading...\n");
      FILE* fp = fopen(file, "w");
      if (fp == NULL) {
			fprintf(stderr, "Error opening file\n");
			return 1;
		  }
      char socketchar[1];
		  while (read(soc, socketchar, 1) > 0) {
			  fwrite(socketchar, sizeof(char), 1, fp);
		  }
		  fclose(fp);
    }

    if (response >= 300 && response < 400) {
		  printf("Redirected...\n");
      printf("%s\n", location);
		  s(location, ++jump, file);
	  }

    shutdown(soc, 2);
	  close(soc);
    return 0;
}

int main(int argc, char *argv[]){

  if (argc == 1 || argc > 3) {
    perror("ERROR zle zadané argumenty");
    exit(EXIT_FAILURE);
  }

  if(argc == 3){
    return s(argv[1], 0, argv[2]);
  }
  else{
    return s(argv[1], 0, NULL);
  }
}
