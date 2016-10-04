function getXMLHttpRequest() {
	if(window.XMLHttpRequest) {
		return new XMLHttpRequest(); //IE7, Firefox, Safari, ...
	} else if(window.ActiveXObject) {
		try {
			return new ActiveXObject("Microsoft.XMLTTP");
		} catch(ex) {
			var theVersions = new Array("MSXML2.XMLHTTP.6.0",
					"MSXML2.XMLHTTP.5.0", "MSXML2.XMLHTTP.4.0",
					"MSXML2.XMLHTTP.3.0","MSXML2.XMLHTTP");
			for(i=0; i<theVersions.length && !xmlHttp; i++){
				try{
					return new ActiveXObject(theVersions[i]);
				} catch(e){
					
				}
			}
		}
	} else {
		alert("Fail to Get XMLHttpRequest");
		return null;
	}
}