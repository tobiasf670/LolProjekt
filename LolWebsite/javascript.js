//REST CALLS
var baseREST = "http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:8080/LolRest/webresources/lolsoapaccess"
	
	function getChampionImg{
	
	var xhttp = new XMLHttpReqyest();
	xhttp.open("GET", baseREST+"getchamppic"+"/"+"s144826", false);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
	
	var res = xhttp.responseText;
	res = res.replace(/ /g, "");
	
	return res;
}

function guessChamp() {
	var xhttp = new XMLHttpRequest();
	var text = document.getElementById("answer").value;
	
	if(text == null)alert("sometext");
	xhttp.open("GET", baseREST + "guesschamp" + "/" + "s144826/" + text, false);
	xhttp.setRequestHeader("Centent-type", "text/plain");
	xhttp.send();
	
	return alert("GUESSRES:" + xhttp.responseText);
}

function skipHero() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", baseREST+"skip"+"/"+"s144826", false);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
	
	alert("SKIPRS:" + xhttp.responseText);
	return xhttp.responseText;
	
}