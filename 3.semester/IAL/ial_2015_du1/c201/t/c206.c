/* c206.c **********************************************************}
{* Téma: Dvousmìrnì vázaný lineární seznam
**
**                   Návrh a referenèní implementace: Bohuslav Køena, øíjen 2001
**                            Pøepracované do jazyka C: Martin Tuèek, øíjen 2004
**                                            Úpravy: Bohuslav Køena, øíjen 2015
**
** Implementujte abstraktní datový typ dvousmìrnì vázaný lineární seznam.
** U¾iteèným obsahem prvku seznamu je hodnota typu int.
** Seznam bude jako datová abstrakce reprezentován promìnnou
** typu tDLList (DL znamená Double-Linked a slou¾í pro odli¹ení
** jmen konstant, typù a funkcí od jmen u jednosmìrnì vázaného lineárního
** seznamu). Definici konstant a typù naleznete v hlavièkovém souboru c206.h.
**
** Va¹ím úkolem je implementovat následující operace, které spolu
** s vý¹e uvedenou datovou èástí abstrakce tvoøí abstraktní datový typ
** obousmìrnì vázaný lineární seznam:
**
**      DLInitList ...... inicializace seznamu pøed prvním pou¾itím,
**      DLDisposeList ... zru¹ení v¹ech prvkù seznamu,
**      DLInsertFirst ... vlo¾ení prvku na zaèátek seznamu,
**      DLInsertLast .... vlo¾ení prvku na konec seznamu,
**      DLFirst ......... nastavení aktivity na první prvek,
**      DLLast .......... nastavení aktivity na poslední prvek,
**      DLCopyFirst ..... vrací hodnotu prvního prvku,
**      DLCopyLast ...... vrací hodnotu posledního prvku,
**      DLDeleteFirst ... zru¹í první prvek seznamu,
**      DLDeleteLast .... zru¹í poslední prvek seznamu,
**      DLPostDelete .... ru¹í prvek za aktivním prvkem,
**      DLPreDelete ..... ru¹í prvek pøed aktivním prvkem,
**      DLPostInsert .... vlo¾í nový prvek za aktivní prvek seznamu,
**      DLPreInsert ..... vlo¾í nový prvek pøed aktivní prvek seznamu,
**      DLCopy .......... vrací hodnotu aktivního prvku,
**      DLActualize ..... pøepí¹e obsah aktivního prvku novou hodnotou,
**      DLSucc .......... posune aktivitu na dal¹í prvek seznamu,
**      DLPred .......... posune aktivitu na pøedchozí prvek seznamu,
**      DLActive ........ zji¹»uje aktivitu seznamu.
**
** Pøi implementaci jednotlivých funkcí nevolejte ¾ádnou z funkcí
** implementovaných v rámci tohoto pøíkladu, není-li u funkce
** explicitnì uvedeno nìco jiného.
**
** Nemusíte o¹etøovat situaci, kdy místo legálního ukazatele na seznam
** pøedá nìkdo jako parametr hodnotu NULL.
**
** Svou implementaci vhodnì komentujte!
**
** Terminologická poznámka: Jazyk C nepou¾ívá pojem procedura.
** Proto zde pou¾íváme pojem funkce i pro operace, které by byly
** v algoritmickém jazyce Pascalovského typu implemenovány jako
** procedury (v jazyce C procedurám odpovídají funkce vracející typ void).
**/

#include "c206.h"

int solved;
int errflg;

void DLError() {
/*
** Vytiskne upozornìní na to, ¾e do¹lo k chybì.
** Tato funkce bude volána z nìkterých dále implementovaných operací.
**/
    printf ("*ERROR* The program has performed an illegal operation.\n");
    errflg = TRUE;             /* globální promìnná -- pøíznak o¹etøení chyby */
    return;
}

void DLInitList (tDLList *L) {
/*
** Provede inicializaci seznamu L pøed jeho prvním pou¾itím (tzn. ¾ádná
** z následujících funkcí nebude volána nad neinicializovaným seznamem).
** Tato inicializace se nikdy nebude provádìt nad ji¾ inicializovaným
** seznamem, a proto tuto mo¾nost neo¹etøujte. V¾dy pøedpokládejte,
** ¾e neinicializované promìnné mají nedefinovanou hodnotu.
**/
    L->First = NULL;
		L->Act = NULL;
		L->Last = NULL;
}

void DLDisposeList (tDLList *L) {
/*
** Zru¹í v¹echny prvky seznamu L a uvede seznam do stavu, v jakém
** se nacházel po inicializaci. Ru¹ené prvky seznamu budou korektnì
** uvolnìny voláním operace free.
**/
  tDLElemPtr prvok = L->First;
  tDLElemPtr d_prvok;

  while(prvok != NULL){
    d_prvok = prvok->rptr;
    free(prvok);
    prvok = d_prvok;
  }

  DLInitList (L);
}

void DLInsertFirst (tDLList *L, int val) {
/*
** Vlo¾í nový prvek na zaèátek seznamu L.
** V pøípadì, ¾e není dostatek pamìti pro nový prvek pøi operaci malloc,
** volá funkci DLError().
**/
	tDLElemPtr prvok; // ulozi prvy prvok
	prvok = (tDLElemPtr) malloc(sizeof(struct tDLElem));

	if(prvok == NULL){
		DLError();
		return;
	}

	prvok->rptr = L->First;
  prvok->lptr = NULL;
  prvok->data = val;

  if(L->First != NULL){ // osetrenie prazdneho zoznamu
    L->First->lptr = prvok;
  }
  else{
    L->Last = prvok;
  }

  L->First = prvok;
}

void DLInsertLast(tDLList *L, int val) {
/*
** Vlo¾í nový prvek na konec seznamu L (symetrická operace k DLInsertFirst).
** V pøípadì, ¾e není dostatek pamìti pro nový prvek pøi operaci malloc,
** volá funkci DLError().
**/
  tDLElemPtr prvok; // ulozi novy prvok
  prvok = (tDLElemPtr) malloc(sizeof(struct tDLElem));

  if(prvok == NULL){
    DLError();
    return;
  }

  prvok->lptr = L->Last;
  prvok->rptr = NULL;
  prvok->data = val;
  if(L->Last != NULL){
    L->Last->rptr = prvok;
  }
  else{
    L->First = prvok;
  }

  L->Last = prvok;
}

void DLFirst (tDLList *L) {
/*
** Nastaví aktivitu na první prvek seznamu L.
** Funkci implementujte jako jediný pøíkaz (nepoèítáme-li return),
** ani¾ byste testovali, zda je seznam L prázdný.
**/
  L->Act = L->First;
}

void DLLast (tDLList *L) {
/*
** Nastaví aktivitu na poslední prvek seznamu L.
** Funkci implementujte jako jediný pøíkaz (nepoèítáme-li return),
** ani¾ byste testovali, zda je seznam L prázdný.
**/
  L->Act = L->Last;
}

void DLCopyFirst (tDLList *L, int *val) {
/*
** Prostøednictvím parametru val vrátí hodnotu prvního prvku seznamu L.
** Pokud je seznam L prázdný, volá funkci DLError().
**/
  if(L->First == NULL){
    DLError();
    return;
  }

  *val = L->First->data;
}

void DLCopyLast (tDLList *L, int *val) {
/*
** Prostøednictvím parametru val vrátí hodnotu posledního prvku seznamu L.
** Pokud je seznam L prázdný, volá funkci DLError().
**/
  if(L->Last == NULL){
    DLError();
    return;
  }

  *val = L->Last->data;
}

void DLDeleteFirst (tDLList *L) {
/*
** Zru¹í první prvek seznamu L. Pokud byl první prvek aktivní, aktivita
** se ztrácí. Pokud byl seznam L prázdný, nic se nedìje.
**/
  tDLElemPtr prvok;

  if(L->First == NULL){
    return;
  }

  if(L->First == L->Act){
    L->Act = NULL;
  }

  prvok = L->First->rptr;
  free(L->First);
  L->First = prvok;
  if(L->First == NULL){ // osetrenie prazdneho zoznamu
    L->Last = NULL;
  }
  else{
    L->First->lptr = NULL;
  }
}

void DLDeleteLast (tDLList *L) {
/*
** Zru¹í poslední prvek seznamu L. Pokud byl poslední prvek aktivní,
** aktivita seznamu se ztrácí. Pokud byl seznam L prázdný, nic se nedìje.
**/
  tDLElemPtr prvok;

  if(L->Last == NULL){ //osetrenie prazdneho zoznamu
    return;
  }

  if(L->Last == L->Act){ // ak je posledny prvok aktivny aktivita sa strati
    L->Act = NULL;
  }

  prvok = L->Last->lptr;
  free(L->Last);
  L->Last = prvok;

  if(L->Last == NULL){ // osetrenie prazdneho zoznamu
    L->First = NULL;
  }
  else{
    L->Last->rptr = NULL;
  }
}

void DLPostDelete (tDLList *L) {
/*
** Zru¹í prvek seznamu L za aktivním prvkem.
** Pokud je seznam L neaktivní nebo pokud je aktivní prvek
** posledním prvkem seznamu, nic se nedìje.
**/
  tDLElemPtr prvok;

  if(L->Act != NULL){
    if(L->Act->rptr == L->Last){ //mazanie posledneho prvku
      DLDeleteLast(L);
    }
    else{
      if(L->Act->rptr != NULL){
        prvok = L->Act->rptr;
        L->Act->rptr = prvok->rptr;
        if(prvok == L->Last){
          L->Last = L->Act;
        }
        else{
          prvok->rptr->lptr = L->Act;
        }
        free(prvok);
      }
    }
  }
}

void DLPreDelete (tDLList *L) {
/*
** Zru¹í prvek pøed aktivním prvkem seznamu L .
** Pokud je seznam L neaktivní nebo pokud je aktivní prvek
** prvním prvkem seznamu, nic se nedìje.
**/
  tDLElemPtr prvok;

  if(L->Act == NULL){
    return;
  }
    if(L->Act->lptr == L->First){ //mazanie prveho prvku
      DLDeleteFirst(L);
    }
    else{
      if(L->Act->lptr != NULL){
        prvok = L->Act->lptr;
        L->Act->lptr = prvok->lptr;
        if(prvok == L->First){
          L->First = L->Act;
        }
        else{
          prvok->lptr->rptr = L->Act;
        }
        free(prvok);
      }
    }
}

void DLPostInsert (tDLList *L, int val) {
/*
** Vlo¾í prvek za aktivní prvek seznamu L.
** Pokud nebyl seznam L aktivní, nic se nedìje.
** V pøípadì, ¾e není dostatek pamìti pro nový prvek pøi operaci malloc,
** volá funkci DLError().
**/
  tDLElemPtr prvok;

  if(L->Act == NULL){
    return;
  }

  prvok = (tDLElemPtr) malloc(sizeof(struct tDLElem));

  if(prvok == NULL){
    DLError();
  }

  prvok->rptr = L->Act->rptr;
  prvok->lptr = L->Act;
  prvok->data = val;
  L->Act->rptr = prvok;
  if (L->Act != L->Last){ // Ak aktivny prvok nie je prvy nadviaz zoznam
    prvok->rptr->lptr = prvok;
  }
  else{ // inak prirad prvok ako posledny
    L->Last = prvok;
  }
}

void DLPreInsert (tDLList *L, int val) {
/*
** Vlo¾í prvek pøed aktivní prvek seznamu L.
** Pokud nebyl seznam L aktivní, nic se nedìje.
** V pøípadì, ¾e není dostatek pamìti pro nový prvek pøi operaci malloc,
** volá funkci DLError().
**/
  tDLElemPtr prvok;

  if(L->Act == NULL){
    return;
  }

  prvok = (tDLElemPtr) malloc(sizeof(struct tDLElem));

  if(prvok == NULL){
    DLError();
  }

  prvok->lptr = L->Act->lptr;
  prvok->rptr = L->Act;
  prvok->data = val;
  L->Act->lptr = prvok;
  if (L->Act != L->First){ // Ak aktivny prvok nie je prvy nadviaz zoznam
    prvok->lptr->rptr = prvok;
  }
  else{ // inak prvok pridaj ako prvy
    L->First = prvok;
  }
}

void DLCopy (tDLList *L, int *val) {
/*
** Prostøednictvím parametru val vrátí hodnotu aktivního prvku seznamu L.
** Pokud seznam L není aktivní, volá funkci DLError ().
**/
  if(L->Act == NULL){
    DLError ();
    return;
  }

  *val = L->Act->data;
}

void DLActualize (tDLList *L, int val) {
/*
** Pøepí¹e obsah aktivního prvku seznamu L.
** Pokud seznam L není aktivní, nedìlá nic.
**/
  if(L->Act == NULL){
    return;
  }
  else{
    L->Act->data = val;
  }
}

void DLSucc (tDLList *L) {
/*
** Posune aktivitu na následující prvek seznamu L.
** Není-li seznam aktivní, nedìlá nic.
** V¹imnìte si, ¾e pøi aktivitì na posledním prvku se seznam stane neaktivním.
**/
  if(L->Act == NULL){
    return;
  }
  else{
    L->Act = L->Act->rptr;
  }
}


void DLPred (tDLList *L) {
/*
** Posune aktivitu na pøedchozí prvek seznamu L.
** Není-li seznam aktivní, nedìlá nic.
** V¹imnìte si, ¾e pøi aktivitì na prvním prvku se seznam stane neaktivním.
**/
  if(L->Act == NULL){
    return;
  }
  else{
    L->Act = L->Act->lptr;
  }
}

int DLActive (tDLList *L) {
/*
** Je-li seznam L aktivní, vrací nenulovou hodnotu, jinak vrací 0.
** Funkci je vhodné implementovat jedním pøíkazem return.
**/

	return L->Act?1:0;
}

/* Konec c206.c*/
