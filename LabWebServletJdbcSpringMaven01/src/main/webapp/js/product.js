var request = null;
function callProductIdServlet(method, id, mode, contextPath) {
	var path = contextPath+"/pages/product.view";
	var data = "action="+mode+"&id="+id;
	if(method=="GET") {
		sendGetRequest(path, data);
	} else if(method=="POST") {
		sendPostRequest(path, data);
	}
}
function processJson(text) {
	var json = JSON.parse(text);
	
	var textNode = document.createTextNode(json[0].text);
	var messageNode = document.getElementById("message");
	messageNode.appendChild(textNode);
	
	if(json[0].hasMoreData) {
		document.forms[0].id.value = json[1].id;
		document.forms[0].name.value = json[1].name;
		document.forms[0].price.value = json[1].price;
		document.forms[0].make.value = json[1].make;
		document.forms[0].expire.value = json[1].expire;
	}
}
function callbackMethod() {
	if(request.readyState==4) {
		if(request.status==200) {
			processJson(request.responseText);
//			processText(request.responseText);
//			processXml(request.responseXML);
			document.getElementById("img").style.display = "none";
		} else {
			console.log("Error! Status Code="+ request.status);
		}
	}
}
function processText(text) {
	var show = text;
	var index = text.indexOf(":");
	if(index!=-1) {
		show = text.substring(0, index);
		var temp = text.substring(index+1);
		var array = temp.split(",");		
		document.forms[0].id.value = array[0];
		document.forms[0].name.value = array[1];
		document.forms[0].price.value = array[2];
		document.forms[0].make.value = array[3];
		document.forms[0].expire.value = array[4];
	}
	
	var textNode = document.createTextNode(show);
	var messageNode = document.getElementById("message");
	messageNode.appendChild(textNode);
}
function processXml(dom) {
	var show = dom.getElementsByTagName("text")[0].firstChild.nodeValue;
	var textNode = document.createTextNode(show);
	var messageNode = document.getElementById("message");
	messageNode.appendChild(textNode);
	
	var hasMoreData = dom.getElementsByTagName("hasMoreData")[0].firstChild.nodeValue;
	if(hasMoreData=="true") {
		document.forms[0].id.value =
			dom.getElementsByTagName("id")[0].firstChild.nodeValue;
		document.forms[0].name.value =
			dom.getElementsByTagName("name")[0].firstChild.nodeValue;
		document.forms[0].price.value =
			dom.getElementsByTagName("price")[0].firstChild.nodeValue;
		document.forms[0].make.value =
			dom.getElementsByTagName("make")[0].firstChild.nodeValue;
		document.forms[0].expire.value =
			dom.getElementsByTagName("expire")[0].firstChild.nodeValue;
	}
}

var request = null;
function sendGetRequest(path, data) {
	request = getXMLHttpRequest();
	request.onreadystatechange = callbackMethod;
	request.open("GET", path+"?"+data+"&dummy="+  new Date().getTime());
	request.send(null);
}
function sendPostRequest(path, data) {
	request = getXMLHttpRequest();
	request.onreadystatechange = callbackMethod;
	request.open("POST", path);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.send(data);
}
