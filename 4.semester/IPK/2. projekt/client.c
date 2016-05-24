// autor: Juraj Sokol
// loggin: xsokol08

# include <stdio.h>
# include <stdlib.h>

# include <netdb.h>
# include <netinet/in.h>

# include <string.h>
# include <strings.h>
# include <sys/types.h>
# include <sys/socket.h>
# include <unistd.h>

# include <arpa/inet.h>
# define h_addr h_addr_list[0]

int client(int port_n, char *server_name, char *file_name, char *operation){
  int socket_n, n;
  struct sockaddr_in server_address;
  struct hostent *server;

  char buffer[3];

  // Vytvorí soket
  if((socket_n = socket(AF_INET, SOCK_STREAM, 0)) < 0){
    fprintf(stderr, "Socket error\n");
    return 1;
  }

  server = gethostbyname(server_name);
  if(server == NULL){
    fprintf(stderr, "Unknown server name\n");
    return 1;
  }

  // inicializácia štruktúry
  bzero((char *) &server_address, sizeof(server_address));
  server_address.sin_family = AF_INET;
  bcopy((char *)server->h_addr, (char *)&server_address.sin_addr.s_addr, server->h_length);
  server_address.sin_port = htons(port_n);

  // pripojí sa na server
  if (connect(socket_n, (struct sockaddr*) &server_address, sizeof(server_address)) < 0) {
    fprintf(stderr, "Dead server.\n");
    return 1;
  }

  // posielanie požiadavku na server
  n = write(socket_n, operation, 1);
  if (n < 0) {
     fprintf(stderr, "ERROR writing to socket\n");
     return 1;
  }
  // odosiela sa názov súboru a akcia
  n = write(socket_n, file_name, strlen(file_name));
  if (n < 0) {
      fprintf(stderr, "ERROR writing to socket\n");
      return 1;
   }

   // čítanie odpovede
  bzero(buffer,3);
  n = read(socket_n, buffer, 3);
  if (n < 0) {
    fprintf(stderr, "ERROR reading from socket\n");
    return 1;
  }

  if(strncmp("ok", buffer, 2) != 0){
    fprintf(stderr, "Server error\n");
    return 1;
  }

  // upload
  if(operation[0] == 'u'){
    char znak;
    FILE *f = fopen(file_name, "r");
    if (f == NULL) {
      fprintf(stderr, "%s, Error opening file\n", file_name);
      return 1;
    }
    else{
      while( feof(f) == 0){
        znak = fgetc(f);
        if(feof(f) != 0 && znak == EOF){
          continue;
        }
        n = write(socket_n, &znak, 1);
      }
      fclose(f);
      close(socket_n);
    }
  }

  // download
  if(operation[0] == 'd'){
    char znak;
    FILE *f = fopen(file_name, "w+");
    if(f == NULL){
      fprintf(stderr, "Error opening file");
      return 1;
    }
    else{
      while(read(socket_n, &znak, 1) > 0){
        fputc(znak, f);
      }
      fclose(f);
    }
  }
  return 0;
}

int main( int argc, char *argv[] ){
  int n = 1, port_n;
  char *host_name = NULL;
  char *file_name = NULL;
  char op[] = {'N',  '\0'};

  //spracovanie argumentovs
  while(n < argc){
    if(argc > 4 && argv[n][0] == '-'){
      switch(argv[n][1]){
        case 'p':
            n++;
            port_n = atoi(argv[n]);
            n++;
            continue;
        case 'h':
            n++;
            host_name = argv[n];
            n++;
            continue;
        case 'u':
            n++;
            if(file_name == NULL){
              file_name = argv[n];
              op[0] = 'u';
            }
            else{
              fprintf(stderr, "Zle zadané argumety ... pozri README!\n");
              return 1;
            }
            n++;
            continue;
        case 'd':
            n++;
            if(file_name == NULL){
              file_name = argv[n];
              op[0] = 'd';
            }
            else{
              fprintf(stderr, "Zle zadané argumety ... pozri README!\n");
              return 1;
            }
            n++;
            continue;
      }
    }
    else{
      fprintf(stderr, "Zle zadané argumety ... pozri README!\n");
      return 1;
    }
  }

  return client(port_n, host_name, file_name, op);
  /*printf("%d\n", port_n);
  printf("%s\n", host_name);
  if(file_name != NULL){
      printf("%s\n", file_name);
  }*/
  return 0;
}
