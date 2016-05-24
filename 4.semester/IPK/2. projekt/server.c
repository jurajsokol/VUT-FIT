// autor: Juraj Sokol
// loggin: xsokol08

# include <stdio.h>
# include <stdlib.h>

# include <netdb.h>
# include <netinet/in.h>

# include <string.h>
# include <strings.h>
# include <unistd.h>
# include <unistd.h> // read - write
# include <sys/types.h>
# include <sys/socket.h>


int communication(int n_socket_n){
  int n = 0;

  char f_name[255];

  // prijatie požiadavku
  bzero(f_name, 255);
  n = read(n_socket_n, f_name, 255);

  // prijatie súboru
  if(f_name[0] == 'u'){
    FILE* f = fopen(f_name+1, "w");
    char znak;
    if (f == NULL) {
         fprintf(stderr, "Error opening file\n");
         n = write(n_socket_n, "er", 2);
    }
    else{
      n = write(n_socket_n, "ok", 3);
      while(read(n_socket_n, &znak, 1) > 0){
        fputc(znak, f);
      }
      fclose(f);
    }
  }

  // odoslanie súboru
  if(f_name[0] == 'd'){
    FILE* f = fopen(f_name+1, "r");
    char znak;
    if (f == NULL) {
         fprintf(stderr, "Error opening file\n");
         n = write(n_socket_n, "er", 2);
    }
    else{
      n = write(n_socket_n, "ok", 2);
      while( feof(f) == 0){
        znak = fgetc(f);
        if(feof(f) != 0 && znak == EOF){
          continue;
        }
        n = write(n_socket_n, &znak, 1);
      }
      fclose(f);
    }
  }

  if (n < 0) {
    fprintf(stderr, "ERROR writing to socket");
    return 1;
  }
  return 0;
}

int server(int port_n){
  int socket_n, n_socket_n, n = 0, pid;
  unsigned int ca_len;
  struct sockaddr_in server_address, client_address;

  // Vytvorenie soketu
  if ((socket_n = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
     fprintf(stderr, "socketerror");
     return 1;
  }

  // Inicializáca štruktúry soketu
  bzero((char *) &server_address, sizeof(server_address));
  server_address.sin_family = AF_INET;
  server_address.sin_addr.s_addr = INADDR_ANY;
  server_address.sin_port = htons(port_n);

  // Bind address
  if (bind(socket_n, (struct sockaddr *) &server_address, sizeof(server_address)) < 0) {
     perror("ERROR on binding");
     exit(1);
  }

    // prepnnutie do listen reežimu
    listen(socket_n,5);
    ca_len = sizeof(client_address);

  while(1){
    // pripojenie klienta
    if ((n_socket_n = accept(socket_n, (struct sockaddr *)&client_address, &ca_len)) < 0) {
      perror("ERROR on accept");
      exit(1);
    }

    if (n < 0) {
      perror("ERROR reading from socket");
      exit(1);
    }

    //ytvorí nový proces
    pid = fork();

    if (pid < 0) {
        fprintf(stderr, "Fork error");
        return 1;
    }


     if (pid == 0) {
        // klientský proces
        close(socket_n);
        communication(n_socket_n);
        return 0;
     }
     else {
       // server proces
       close(n_socket_n);
     }
  }
  return 0;
}

int main( int argc, char *argv[] ) {
  int port_n;

  // spracovanie argumentov
  if( argc == 3 && 0 == strcmp(argv[1], "-p")){
    port_n = atoi(argv[2]);
    return server(port_n);
  }
  else{
    fprintf(stderr, "Nesprávne argumnty\n");
    return 1;
  }
}
