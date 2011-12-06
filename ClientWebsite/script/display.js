function toggle(id){
	var e = document.getElementById(id);
	if(e.style.display == 'none'){
		e.style.display = 'block';
	} else if(e.style.display == 'block'){
		e.style.display = 'none';
	} else {
		e.style.display = 'none';
	}
}
// this function will load a url into an id's element
function loadPage(id,str) {
	var content;
    if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        content=new XMLHttpRequest();
    } else {// code for IE6, IE5
        content=new ActiveXObject("Microsoft.XMLHTTP");
    }
    content.onreadystatechange = function() {
        if (content.readyState==4 && content.status==200) {
            document.getElementById(id).innerHTML=content.responseText;
        }
    }
    content.open("GET",str,true);
    content.send();
}
