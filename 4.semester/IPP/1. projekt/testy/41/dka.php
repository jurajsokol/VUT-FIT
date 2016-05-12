<?php
#Projekt do predmetu IPP: Determinizace konečného automatu
#DKA:xsokol08
#Autor: Juraj Sokol

function array_rm($item, $array){
	$key = array_search($item, $array);
	if($key!==false){
    unset($array[$key]);
	}
	return array_values($array);
}

function write2stderr($text){
	$STDERR = fopen('php://stderr', 'w+');
	fwrite($STDERR, $text);
	fclose($STDERR);
}

/* vytvorí štruktúru pre aoutomat */
function automat_init(){
	return array("Q"=>array(),
							 "E"=>array(),
						 	 "P"=>array(),
						 	 "s"=>null,
						 	 "F"=>array());
}

/* prevedie automat na normálnu formu výstupu */
function automat2string($automat, $assoc){
	/* Q */
	$Q = array();
	foreach($automat["Q"] as $q){
		if(is_array($q)){
			array_push($Q, implode("_", $q));
		}
		else{
			array_push($Q, $q);
		}
	}
	sort($Q, SORT_STRING);
	$Q = implode(", ", $Q);
	/* E */
	$E = array();
	foreach($automat["E"] as $e){
		array_push($E, "'".$e."'");
	}
	sort($E, SORT_STRING);
	$E = implode(", ", $E);
	/* P */
	$P = array();
	foreach($automat["P"] as $q1 => $v){
		if($assoc){
			foreach($v as $z => $q2){
				natsort($q2);
				array_push($P, $q1." '".$z."' -> ".implode("_", $q2));
			}
		}
		else{
			foreach($v as $a)
			array_push($P, $q1." '".$a[0]."' -> ".$a[1]);
		}
	}
	sort($P, SORT_STRING);
	$P = implode(",\n", $P);
	if($P != ""){
		$P .= "\n";
	}
	/* F */
	$F = array();
	foreach($automat["F"] as $f){
		if(is_array($f)){
			array_push($F, implode("_", $f));
		}
		else{
			array_push($F, $f);
		}
	}
	sort($F, SORT_STRING);
	$F = implode(", ", $F);

	return "(\n{".$Q."},\n{".$E."},\n{\n".$P."},\n".$automat["s"][0].",\n{".$F."}\n)";
}

function write2file($text, $file){
	$f = fopen($file, "w");
	if($f == false){
		write2stderr( "Error in opening file");
		exit(3);
	}
	fwrite($f, $text);
	fclose($f);
}

/* fumkcia vytvorí epsilonový uzáver **
** algoritmus na slajdoch IFJ 4 8/36 */
function eps_uzaver($automat, $state){
	$Q = array($state);
	$Q1 = array(); // byvaly stav
	$z = array(); // pomocny zasobnik
	do{
		$Q1 = $Q;
		if(!empty($automat["P"])){
			if(array_key_exists($state, $automat["P"])){
				foreach($automat["P"][$state] as $p){
					if($p[0] == ""){
						array_push($Q, $p[1]);
						$Q = array_unique($Q);
						array_push($z, $p[1]); // dam na zasobnik
						$z = array_unique($z); // ak sa tam uz nachadza tak ho odstrani
					}
					$state = array_pop($z);
				}
			}
		}
	}while($Q != $Q1);
	return $Q;
}

/* funkcia odstráni epsilon prechody **
** algoritmus na slajdoch IFJ 4 10/36 */
function odstran_eps_prechody($automat){
	$eps_free = automat_init();
	$eps_free["s"] = $automat["s"];
	$eps_free["Q"] = $automat["Q"];
	$eps_free["E"] = $automat["E"];
	$eps_free["P"] = array();
	$eps_free["F"] = array();
	foreach($automat["Q"] as $q){
		$eps = eps_uzaver($automat, $q);
		$eps_free["P"][$q] = array();
		foreach($eps as $e){
			if(!empty($automat["P"])){
				if(array_key_exists($e, $automat["P"])){
					foreach($automat["P"][$e] as $p){
						if($p[0] != ""){
							array_push($eps_free["P"][$q], $p);
						}
					}
				}
			}
		}
		foreach($eps as $e){
				if(in_array($e, $automat["F"])){
					if(!in_array($e, $eps_free["F"])){
						array_push($eps_free["F"], $e);
					}
				}
		}
	}
	return $eps_free;
}

/* vytvorí deterministický konečný automat **
** upravená verzia, ktorá sa lepsie implementovala **
** algoritmus na slajdoch IFJ 4 24/36 */
function determinization($automat){
	if(empty($automat["P"])){
		return $automat;
	}
	$dka = automat_init();
	$dka["E"] = $automat["E"];
	$dka["E"] = array_rm("", $dka["E"]);
	$automat = odstran_eps_prechody($automat);
	$dka["s"] = array($automat["s"]);
	$Qn = array($dka["s"]);
	$dka["P"] = array();
	$dka["Q"] = array();
	$dka["F"] = array();
	do{
		$Q1 = reset($Qn);
		$Qn = array_rm($Q1, $Qn);
		if(!in_array($Q1, $dka["Q"])){
			natsort($Q1);
			array_push($dka["Q"], $Q1);
		}
		$Q2 = array();

		foreach($Q1 as $q1){ // iteruje kazdym stavom
			foreach($automat["P"][$q1] as $p){ // iteruje každým prvidlom, ktoré stav má
				if(!array_key_exists($p[0], $Q2)){
					$Q2[$p[0]] = array();
				}
				array_push($Q2[$p[0]], $p[1]); // pridá druhý stav k vstupnému znaku
			}
		}
		if(!empty($Q2)){
			natsort($Q1);
			foreach($Q2 as $k => $v){
				natsort($v);
			}
			$dka["P"][implode("_", $Q1)] = $Q2;
		}
		foreach($Q2 as $v){
			if(!in_array($v, $dka["Q"])){
				$Qn = $Qn + $Q2;
			}
		}

		foreach($Q1 as $q1){
			if(in_array($q1, $automat["F"])){
				if(!in_array($Q1, $dka["F"])){
					array_push($dka["F"], $Q1);
				}
			}
		}
	}while(!empty($Qn)); // until Q new = ∅
	return $dka;
}

/* nacita automat, odstráni medzery a komentáre */
function nacitaj_automat($f, $l){
	$a = "";
	// opening file
	$file = fopen("$f", "r");
	if($file == false){
		write2stderr( "Error in opening file");
		exit(2);
	}

	while($line = fgets($file)){
			$a .= preg_replace("/(?<!\')\s|\s(?!\')|#(?!\').*\Z/", "", $line); // odstráni nadbytočné medzery a komentáre
	}

	fclose($file);
	if($l){
		return strtolower($a);
	}
	else{
		return $a;
	}
}


function create_automat($input, $l){

	$a = nacitaj_automat($input, $l);

	// kontrola správnosti automatu
	if(!preg_match("/\({.*},{.*},{.*},.*,{.*}\)$/", $a)){ // skontroluje správnosť automatu
		write2stderr("nesprávne zapísaný automat");
		exit(40);
	}

	$a = preg_split("/\({|}\)|},{|},|,{/", $a); // rozdeli autamat na časti
	$automat = automat_init();

	// spracovanie stavov
	$automat["Q"] = preg_split("/,/", $a[1]);
	if(!empty(preg_grep("/^\d.*|^_.*$|.*_$/", $automat["Q"]))){ // stav nesmie zacinat _, cislom a koncit _
		exit(40);
	}
	$automat["Q"] = array_unique($automat["Q"]);
	// spracovanie vstupných znakov abecedy
	$automat["E"] = array();
	if($a[2] == ""){
		write2stderr("Prazdna mnozina abecedy\n");
		exit(41);
	}
	$a[2] = $a[2].",";
	do{
		if(!preg_match("/^\'.*\',/", $a[2], $m)){
			exit(40);
		}
		array_push($automat["E"], substr($m[0], 1, -2));
		$a[2] = preg_replace("/^\'.*\',/", "", $a[2]);
	}while($a[2] != "");
	$automat["E"] = preg_split("/\',\'/", $automat["E"][0]);
	$automat["E"] = array_unique($automat["E"]);
	// spracovanie pravidiel
	if($a[3] != ""){
		$P = preg_split("/,(?!\')|(?<!\'),/", $a[3]);
		$automat["P"] = array();
		foreach($P as $v){
			if(!preg_match("/.+\'.*\'->.+/", $v)){ // skontroluje správnosť pravidla automatu
				write2stderr("Nespravne zapisane pravidlo: ".$v."\n");
				exit(40);
			}
			preg_match("/(?<=\').*(?=\')/", $v, $m); // vyberie vstupny znak
			$x = preg_split("/\'.*\'->/", $v); // oddeli stavy
			$p = array($x[0]=>array($m, $x[1]));
			if(!in_array($x[0], $automat["Q"]) || !in_array($x[1], $automat["Q"])){
				write2stderr("pravidlo obsahuje neplatne stavy : ".$v."\n");
				exit(41);
			}
			if(!in_array($m[0], $automat["E"]) && $m[0] != ""){
				write2stderr("pravidlo obsahuje neplatny vstupny znak : ".$v."\n");
				exit(41);
			}
			if(array_key_exists($x[0], $automat["P"])){
				if(!in_array(array($m[0], $x[1]), $automat["P"][$x[0]])){
					array_push($automat["P"][$x[0]], array($m[0], $x[1]));
				}
			}
			else{
				$automat["P"][$x[0]] = array(array($m[0], $x[1]));
			}
		}
	}
	else{
		$automat["P"] = array();
	}
	// pociatocny stav
	if(!in_array($a[4], $automat["Q"])){
		write2stderr("Počiatočný stav nie je v množine stavov.");
		exit(41);
	}
	$automat["s"] = $a[4];

	// spracovanie koncovych stavov
	if($a[5] != ""){
		$automat["F"] = preg_split("/,/", $a[5]);
		foreach($automat["F"] as $f){
			if(!in_array($f, $automat["Q"])){
				write2stderr("Koncovy stav ".$f." nie je v množine stavov\n");
				exit(41);
			}
		}
		$automat["F"] = array_unique($automat["F"]);
	}
	return $automat;
}

function help(){
	Print "--help\n\ttáto nápoveda\n--input=filename\n\tzadaný vstupní textový soubor v UTF-8 s popisem konečného automatu.\n--output=filename\n\ttextový výstupní soubor (opět v UTF-8) s popisem výsledného ekviva-
	lentního 9 konečného automatu v předepsaném formátu.\n-e, --no-epsilon-rules\n\tpro pouhé odstranění ε-pravidel vstupního konečného automatu. Parametr nelze kombinovat s parametrem -d (resp. --determinization).\n-d, --determinization\n\tprovede determinizaci bez generování nedostupných stavů (viz algoritmus IFJ, 4. přednáška, snímek 24/36). Parametr nelze kombinovat s parametrem -e (resp.
	--no-epsilon-rules).\n-i, --case-insensitive\n\tnebude brán ohled na velikost znaků při porovnávání symbolů či stavů (tj. a = A, ahoj = AhOj nebo A b = a B); ve výstupu potom budou všechna velká písmena
	převedena na malá.\n";
	exit(0);
}

// funkcia spracuje prikazovy riadok
function term_opts(){
	$options = "edi";
	$longopt = array("help", "input:", "output:", "no-epsilon-rules", "determinization", "case-insensitive");
	$opt = getopt($options, $longopt);

	if(array_key_exists("help", $opt) && count($opt) == 1){
		help();
	}

	if(array_key_exists("input", $opt)){
		$i = $opt["input"];
	}
	else{
		$i = "php://stdin";
	}
	if(array_key_exists("output", $opt)){
		$o = $opt["output"];
	}
	else{
		$o = "php://stdout";
	}

	if(array_key_exists("no-epsilon-rules", $opt) || array_key_exists("e", $opt)){
		if(array_key_exists("determinization", $opt) || array_key_exists("d", $opt)){
			exit(1);
		}
	}

	if(array_key_exists("case-insensitive", $opt) || array_key_exists("i", $opt)){
		$a = create_automat($i, true);
	}
	else{
		$a = create_automat($i, false);
	}

	if(array_key_exists("no-epsilon-rules", $opt) || array_key_exists("e", $opt)){
		if(array_key_exists("determinization", $opt) || array_key_exists("d", $opt)){
			write2stderr("Err: Nelze kombinovat --no-epsilon-rules|-e a --determinization|-d!");
			exit(1);
		}
		write2file(automat2string(odstran_eps_prechody($a), false), $o);
		exit(0);
	}
	elseif(array_key_exists("determinization", $opt) || array_key_exists("d", $opt)){
			if(array_key_exists("no-epsilon-rules", $opt) || array_key_exists("e", $opt)){
				write2stderr("Err: Nelze kombinovat --no-epsilon-rules|-e a --determinization|-d!");
				exit(1);
			}
		write2file(automat2string(determinization($a), true), $o);
		exit(0);
	}
	else{
		write2file(automat2string($a, false), $o);
		exit(0);
	}
}

term_opts();

?>
