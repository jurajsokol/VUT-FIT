#!/usr/bin/env bash

# =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# IPP - dka - doplňkové testy - 2015/2016
# =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# Činnost:
# - vytvoří výstupy studentovy úlohy v daném interpretu na základě sady testů
# =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# Popis (README):
#
# Struktura skriptu _stud_tests.sh (v kódování UTF-8):
# Každý test zahrnuje až 4 soubory (vstupní soubor, případný druhý vstupní
# soubor, výstupní soubor, soubor logující chybové výstupy *.err vypisované na
# standardní chybový výstup (pro ilustraci) a soubor logující návratový kód
# skriptu *.!!!). Pro spuštění testů je nutné do stejného adresáře zkopírovat i
# váš skript. V komentářích u jednotlivých testů jsou uvedeny dodatečné
# informace jako očekávaný návratový kód.
# Tyto doplňující testy obsahují i několik testů rozšíření (viz konec skriptu).
#
# Proměnné ve skriptu _stud_tests.sh pro konfiguraci testů:
#  INTERPRETER - využívaný interpret
#  EXTENSION - přípona souboru s vaším skriptem (jméno skriptu je dáno úlohou)
#  LOCAL_IN_PATH/LOCAL_OUT_PATH - testování různých cest ke vstupním/výstupním
#    souborům
#
# Další soubory archivu s doplňujícími testy:
# V adresáři ref-out najdete referenční soubory pro výstup (*.out nebo *.xml),
# referenční soubory s návratovým kódem (*.!!!) a pro ukázku i soubory s
# logovaným výstupem ze standardního chybového výstupu (*.err). Pokud některé
# testy nevypisují nic na standardní výstup nebo na standardní chybový výstup,
# tak může odpovídající soubor v adresáři chybět nebo mít nulovou velikost.
#
# Doporučení a poznámky k testování:
# Tento skript neobsahuje mechanismy pro automatické porovnávání výsledků vašeho
# skriptu a výsledků referenčních (viz adresář ref-out). Pokud byste rádi
# využili tohoto skriptu jako základ pro váš testovací rámec, tak doporučujeme
# tento mechanismus doplnit.
# Dále doporučujeme testovat různé varianty relativních a absolutních cest
# vstupních a výstupních souborů, k čemuž poslouží proměnné začínající
# LOCAL_IN_PATH a LOCAL_OUT_PATH (neomezujte se pouze na zde uvedené triviální
# varianty).
# Výstupní soubory mohou při spouštění vašeho skriptu již existovat a pokud není
# u zadání specifikováno jinak, tak se bez milosti přepíší!
# Výstupní soubory nemusí existovat a pak je třeba je vytvořit!
# Pokud běh skriptu skončí s návratovou hodnotou různou od nuly, tak není
# vytvoření souboru zadaného parametrem --output nutné, protože jeho obsah
# stejně nelze považovat za validní.
# V testech se jako pokaždé určitě najdou nějaké chyby nebo nepřesnosti, takže
# pokud nějakou chybu najdete, tak na ni prosím upozorněte ve fóru příslušné
# úlohy (konstruktivní kritika bude pozitivně ohodnocena). Vyhrazujeme si také
# právo testy měnit, opravovat a případně rozšiřovat, na což samozřejmě
# upozorníme na fóru dané úlohy.
#
# =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' #no color

TASK=dka
INTERPRETER="php -d open_basedir=\"\""
EXTENSION=php
#INTERPRETER=python3
#EXTENSION=py

# cesty ke vstupním a výstupním souborům
LOCAL_IN_PATH="./" # (simple relative path)
LOCAL_IN_PATH2="" #Alternative 1 (primitive relative path)
LOCAL_IN_PATH3=`pwd`"/" #Alternative 2 (absolute path)
LOCAL_OUT_PATH="./" # (simple relative path)
LOCAL_OUT_PATH2="" #Alternative 1 (primitive relative path)
LOCAL_OUT_PATH3=`pwd`"/" #Alternative 2 (absolute path)
# cesta pro ukládání chybového výstupu studentského skriptu
LOG_PATH="./"

function cleaner {
  rm -f ./${1}/${1}.!!!
  rm -f ./${1}/${1}.out
  rm -f ./${1}/${1}.err
}

function dff_ret_val {
  cd ./${1}
  if diff ./${1}.!!! ./ref-out/${1}.!!!
	then
		echo -e -n "[   ${GREEN}OK${NC}   ]"
	else
		echo -e -n "[ ${RED}FAILED${NC} ]"
	fi
  echo " ... ${1}"
  cd ../
}

function dff {
  cd ./${1}
  if diff ./${1}.out ./ref-out/${1}.out && diff ./${1}.err ./ref-out/${1}.err && diff ./${1}.!!! ./ref-out/${1}.!!!
	then
		echo -e -n "[   ${GREEN}OK${NC}   ]"
	else
		echo -e -n "[ ${RED}FAILED${NC} ]"
	fi
  echo " ... ${1}"
  cd ../
}

# test01: mini definice automatu; Expected output: test01.out; Expected return code: 0
T=test01
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test02: jednoducha determinizace (z prednasek); Expected output: test02.out; Expected return code: 0
T=test02
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in -d > ${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test03: pokrocila determinizace; Expected output: test03.out; Expected return code: 0
#cleaner test03
#$INTERPRETER $TASK.$EXTENSION -d --input=${LOCAL_IN_PATH}test03/test03.in --output=${LOCAL_OUT_PATH}test03/test03.out 2> ${LOG_PATH}test03/test03.err
#echo -n $? > ./test03/test03.!!!
#dff test03

# test04: diakritika a UTF-8 na vstupu; Expected output: test04.out; Expected return code: 0
T=test04
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION < ${LOCAL_IN_PATH3}${T}/${T}.in > ${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test05: slozitejsi formatovani vstupniho automatu (specialni znaky); Expected output: test05.out; Expected return code: 0
T=test05
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test06: vstup ze zadani; Expected output: test06.out; Expected return code: 0
T=test06
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -d --output=${LOCAL_OUT_PATH3}${T}/${T}.out < ${LOCAL_IN_PATH}${T}/${T}.in 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test07: odstraneni epsilon-prechodu (priklad z IFJ04); Expected output: test07.out; Expected return code: 0
T=test07
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -e --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH2}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

# test08: Chybna kombinace parametru -e a -d; Expected output: test08.out; Expected return code: 1
T=test08
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH3}${T}/${T}.out -d --no-epsilon-rules 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

# test09: chyba vstupniho formatu; Expected output: test09.out; Expected return code: 40
T=test09
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH2}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

# test10: semanticka chyba vstupniho formatu (poc. stav neni v mnozine stavu); Expected output: test10.out; Expected return code: 41
T=test10
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=AlmostMinimalFSM
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Comments
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Determinize
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out -d 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Determinize2
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out -d 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Determinize_long
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --determinization --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Determinize_nonfinishing
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -d --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Determinize_nonfinishing
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -d --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Duplicate_final_state
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -i --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Duplicate_rule
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -i --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Duplicate_state
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -i --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Epsilons
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -e --input=${LOCAL_IN_PATH2}${T}/${T}.in --output=${LOCAL_OUT_PATH2}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Epsilons_long
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION --no-epsilon-rules --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

T=Unicode
cleaner ${T}
$INTERPRETER $TASK.$EXTENSION -d --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff ${T}

echo "Tests returning value 0"
cd 0
T=Help
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --help > ./${T}/${T}.out
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

echo "Tests returning value 1"
cd ../1
T=d_with_e
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION -e -d
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

echo "Tests returning value 2"
cd ../2
T=Nonexistent_input_file
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=garbage 2>${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

echo "Tests returning value 3"
cd ../3
T=Cannot_open_output_file
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH3}${T}/${T}.in --output=testfolder 2>${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

echo "Tests returning value 40"
cd ../40
T=Missing_alphabet
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_braces
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_comma_between_sets
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_comma_in_set
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_final_state_set
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_initial_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=`pwd`"/"${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Missing_rule_set
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Multiple_chars_in_apostrophes
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Multiple_chars_in_rule_input_symbol
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=No_apostrophes_around_symbol
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=No_closing_apostrophe
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=No_opening_apostrophe
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_arrow_split
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_missing_from_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_missing_input_symbol
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_missing_target
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=State_ends_with_underscore
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=State_starts_with_num
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=State_starts_with_underscore
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Three_apostrophes
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

echo "Tests returning value 41"
cd ../41
T=Alphabet_empty
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_unknown_from_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_unknown_symbol
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Rule_unknown_to_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Unknown_final_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Unknown_initial_state
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}

T=Wrong_case
cleaner ${T}
$INTERPRETER ../$TASK.$EXTENSION --input=${LOCAL_IN_PATH}${T}/${T}.in --output=${LOCAL_OUT_PATH}${T}/${T}.out --determinization 2> ${LOG_PATH}${T}/${T}.err
echo -n $? > ./${T}/${T}.!!!
dff_ret_val ${T}
