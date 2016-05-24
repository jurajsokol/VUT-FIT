IJA
==============
Projekt do predmetu Java
***
## Zadání projektu:
Navrhněte a implementujte aplikaci *Othello (Reversi)*, která vychází ze stejnojmenné deskové hry.
***
### Specifikace požadavků

* Zadání definuje podstatné vlastnosti aplikace, které musí být splněny. Předpokládá se, že detaily řešení si doplní řešitelské týmy.
* Aplikace bude implementovat základní pravidla hry, která jsou uvedena [zde](https://cs.wikipedia.org/wiki/Othello_%28deskov%C3%A1_hra%29#Pravidla_hry)
* rozšíření pravidel:
  * možnost zvolit velikost hrací desky (6, 8, 10, 12); implicitní hodnota je 8
  * možnost zvolit "zamrzání" kamenů
    * pokud je zvoleno, nastaví se časové intervaly I a B (v sekundách) a počet kamenů C
    * po uplynutí náhodně vygenerované doby z časového intervalu (0,I) se zablokuje C náhodně vybraných kamenů na náhodně vygenerovanou dobu z intervalu (0,B)
    * kameny se odblokují po uplynutí doby blokování, ale až po ukončení aktuálního tahu
    * zablokované kameny nelze použít pro tvorbu řad (tj. zajímání soupeřových kamenů)
* aplikace bude umožňovat
    * vytvořit a hrát více her současně
    * uložit a načíst rozehranou partii
    * operaci undo
    * zvolit soupeře
      * člověk nebo počítač
      * v případě počítače implementujte dva různé (triviální !) algoritmy
* součástí aplikace bude grafické uživatelské rozhraní zobrazující
  * hrací desku
  * hrací kameny, které má hráč k dispozici
  * hrací kameny, které jsou vloženy na desku
  * výsledné skóre po ukončení hry

### Rozšíření pro tříčlenný tým

* rozšiřte aplikaci o možnost on-line hry
* aplikace bude typu klient-server
* možnost rozehrát více her současně zůstává
* klient
    * klient obsahuje grafické uživatelské rozhraní (GUI)
    * jeden klient představuje právě jednoho hráče; klienti spolu komunikují prostřednictvím serveru
    * klient vždy ovládá pouze svého hráče, stav hry a aktivity ostatních hráčů se mu pouze zobrazuje
* server
    * autentizaci a autorizaci uživatelů není třeba řešit - při prvním kontaktu klienta se serverem se mu např. přidělí unikátní název/kód/číslo, kterým se poté klient identifikuje
    * zpracovává události od klientů a rozesílá změny stavu hry připojeným klientům
    * obsahuje úložiště rozehraných her; hra je uložena v souboru v textové podobě a je identifikována svým názvem
* hra
    * klient se připojuje na server, který nabízí seznam přihlášených nehrajících klientů
    * jeden z hráčů (lídr) vyzve vybraného hráče ke hře; jakmile se shodnou, lídr buď založí novou hru nebo vybere uloženou rozehranou hru
