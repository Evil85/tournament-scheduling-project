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