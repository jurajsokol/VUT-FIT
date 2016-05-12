/*
 * Soubor:  proj2.c
 * Datum:   26.11.2014
 * Autor:   Juraj Sokol xsokol08@fit.vutbr.cz
 * Projekt: Iteračné výpočty, projekt č. 2 pro předmět IZP
 * Popis:   Program vypočíta tangens uhlov, porovná presnosť výpočtu funkcie tangens (viď. --help) a vypočíta vzdialenosť a výšku objektu
 */

# include <stdio.h>
# include <stdlib.h>
# include <math.h>
# include <string.h>


/*
 * Funkcia vypise jednoduchu napovedu na pouzivanie a skonci
 */
void help()
{
    printf("Vitajte v pomocníkovi!\n\nvytvoril: Juraj Sokol\nlogin: xsokol08\n\n");
    printf("O Programe:\nProgram vypocita tangens uhlu zadaného v radiánoch dvomi algoritmami a porovná odchýlku výpočtu týchto algoritmov s funkciou implementovanou v matematickej knžnici. Program dokáže vypočítať vzdialenosť a výšku meraného objektu.\n\n");
    printf("Ako používať program:\n\n\t--help -> program vypíše nápovedu používania programu a skončí.\n\t--tan -> porovná presnosti výpočtu tangens uhlu A (v radiánoch) medzi volaním tan z matematickej knižnice, a výpočtu tangens pomocou Taylorovho polynómu a zreťazeného zlomku. Argumenty N a M udávajú, v ktorých iteráciách iteračného výpočtu má porovnanie prebiehať. 0 < N <= M < 14\n\t-m -> vypočíta a zmeria vzdialenosti. Uhol α (viď. obrázok) je daný argumentom A v radiánoch. Program vypočíta a vypíše vzdialenosť meraného objektu. 0 < A <= 1.4 < π/2.Pokiaľ je zadaný, uhol β udáva argument B v radiánoch. Program vypočíta a vypíše i výšku meraného objektu. 0 < B <= 1.4 < π/2\n\t-c -> nastavuje výšku meracieho prístroja pre výpočet. Výška c je daná argumentom X (0 < X <= 100). Argument je voliteľný - implicitná výška je 1.5 metrov.");
}


/*
 * Funkcia umocni zaklad na prislusny exponent
** Argumenty **
 *      c -> zaklad mocniny
 *      e -> exponent
 */
double fmocnina(double c, int e)
{
    int i;
    double vysledok = 1;

	for(i = 1; i <= e; i++)
        vysledok *= c;

    return vysledok;
}


/*
 * Funkcia vypocita tangens pomocou talorovho polynomu
** Argumenty **
 *      x -> uhol v radianoch
 *      n -> udava rozvoj polynomu
 */
double taylor_tan(double x, unsigned int n)
{
    if(x == 0)
        return 0;

    double vysledok = 0.0;    
    unsigned int i, mocnina = 1;
    double citatele[] = {1, 1, 2, 17, 62, 1382, 21844, 929569, 6404582, 443861162, 18888466084, 113927491862, 58870668456604};
    double menovatele[] = {1, 3, 15, 315, 2835, 155925, 6081075, 638512875, 10854718875, 1856156927625, 194896477400625, 49308808782358125, 3698160658676859375};

    for(i = 0; i < n; i++){
        vysledok += fmocnina(x, mocnina)*citatele[i]/menovatele[i];
        mocnina += 2;
    }

    return vysledok;
}


/*
 * Funkcia vypocita tangens pomocou zretazeneho zlomku
** Argumenty **
 *      x -> uhol v radianoch
 *      n -> udava rozvoj polynomu
 */
double cfrac_tan(double x, unsigned int n)
{
    double citatel;
    double m_vys = 0; //medzivysledok;

    if(x == 0)
        return 0.0;

    for(citatel = (n * 2) - 1; citatel > 0; citatel -= 2){
         m_vys = 1/(citatel/x - m_vys);
    }

    return m_vys;
}


/*
 * Funkcia porovnava medzivypocty funkcii na vypocet tangensu
** Argumenty **
 *      zm -> prvymedzivypocet, ktory ma vypisat
 *      km -> posledny medzivypocet, ktory ma vypisat 
 *      uhol -> uhol, ktory ma vypocitat
 */
int porovnanie_vypoctu(double uhol, unsigned int zm, unsigned int km)
{
    unsigned int i;
    double p_p; //pomocna peremenna
    double tan_mk; //vypocet tangensu z matematicke knihovny

    for(i = zm; i <= km; i++){
        tan_mk = tan(uhol);
        p_p = taylor_tan(uhol, i-1);
        printf("%d %e %e %e ", i, tan_mk, p_p, tan_mk - p_p);
        p_p = cfrac_tan(uhol, i-1);
        printf("%e %e\n", p_p, tan_mk - p_p);
    }

    return 0;
}


/*
 * Funkcia vypocita vzdialenost objektu od meracieho pristroja
 ** Argumenty **
 *      vyska_mp -> v akej vyske sa nachadza meracie zariadenie
 *      uhol -> uhol pod ktorym sa meria
 */
double vzdialenost(double vyska_mp, double uhol)
{
    return vyska_mp/cfrac_tan(uhol, 13);
}


/*
 * Funkcia vypocita vysku meraného objektu
 ** Argumenty **
 *      vyska_mp -> v akej vyske sa nachadza meracie zariadenie
 *      uhol -> uhol pod ktorym sa meria
 *      vzdialenost -> vzdialenost meraneho objektu
 */
double vyska(int vyska_mp, double uhol, double vzdialenost)
{
    return vyska_mp + (vzdialenost*cfrac_tan(uhol, 13));
}


/*
 * Funkcia spracuje argumenty prikazoveho riadku zadane pri spusteni
 */
int spracuj_argumenty(int argc, char *argv[])
{
    char *str;
    double uhol_A, uhol_B, vyska_mp = 1.5;

    if (strcmp(argv[1], "--help") == 0){
        help();
    }

    else if(strcmp(argv[1], "--tan") == 0){
        if (argc < 5){
            fprintf(stderr, "Málo argumentov\n");
            exit(EXIT_FAILURE);
        }
        if (argc > 5){
            fprintf(stderr, "Priveľa argumentov\n");
            exit(EXIT_FAILURE);
        }
        if((uhol_A = strtod(argv[2], &str)) < 0 || uhol_A > 1.4){
            fprintf(stderr, "Uhol mimo povolený rozshah hodnôt\n");
            exit(EXIT_FAILURE);		
        }
        porovnanie_vypoctu(uhol_A, strtol(argv[3], &str, 10), strtol(argv[4], &str, 10));
    }

    else if(strcmp(argv[1], "-c") == 0){
        vyska_mp = strtod(argv[2], &str);
        if(str[0] != '\0'){
            fprintf(stderr, "Nesprávny argument\n");
            exit(EXIT_FAILURE);
        }
        uhol_A = strtod(argv[4], &str);
        if(str[0] != '\0'){
            fprintf(stderr, "Nesprávny argument\n");
            exit(EXIT_FAILURE);
        }

        if(uhol_A  < 0 || uhol_A > 1.4){
            fprintf(stderr, "Uhol mimo povolený rozshah hodnôt\n");
            exit(EXIT_FAILURE);		
        }

        printf("%.10e\n", vzdialenost(vyska_mp, uhol_A));
        if(argc == 6){
            uhol_B = strtod(argv[5], &str);
            if(uhol_B  < 0 || uhol_B > 1.4){
                fprintf(stderr, "Uhol mimo povolený rozshah hodnôt\n");
                exit(EXIT_FAILURE);		
            }
            printf("%.10e\n", vyska(vyska_mp, uhol_B, vzdialenost(vyska_mp, uhol_A)));
        }
    }

    else if(strcmp(argv[1], "-m") == 0){
        uhol_A = strtod(argv[2], &str);
        if(uhol_A  < 0 || uhol_A > 1.4){
            fprintf(stderr, "Uhol mimo povolený rozshah hodnôt\n");
            exit(EXIT_FAILURE);
        }
        printf("%.10e\n", vzdialenost(vyska_mp, uhol_A));
        if(argc == 4){
            uhol_B = strtod(argv[3], &str);
            if(uhol_B  < 0 || uhol_B > 1.4){
                fprintf(stderr, "Uhol mimo povolený rozshah hodnôt\n");
                exit(EXIT_FAILURE);
            }		
            printf("%.10e\n", vyska(vyska_mp, uhol_B, vzdialenost(vyska_mp, uhol_A)));
        }
    }
    else
        fprintf(stderr, "Nespravne argementy!\n");

    return 0;
}


int main(int argc, char *argv[])
{
    spracuj_argumenty(argc, argv);    

    return 0;
}
