
function isMobile(mobile)
{
	if(mobile != undefined && mobile != ""){
	
		if((/^1[34578]\d{9}$/.test(mobile))){
  			
  			return true;
  		}
	}
	
	return false;
}


function setClass(dom,className){

	if(dom.getAttribute("className") == null){
		dom.setAttribute("class",className);
	}else{
		dom.setAttribute("className",className);
	}
}





(function (doc, win) {
	var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		recalc = function () {
			var clientWidth = docEl.clientWidth;
			if (!clientWidth) return;
			if(clientWidth>=750){
				docEl.style.fontSize = '100px';
			}else{
				docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
			}
		};
	if (!doc.addEventListener) return;
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);