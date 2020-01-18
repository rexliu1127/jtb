/*register 验证手机号*/
      function clkSQ(){
			
    		var tel =document.getElementById("text-tel").value;
    		if(tel==""){
//  		 if(!(/^1[34578]\d{9}$/.test(tel))){
    			
    			alert("请输入正确手机号");
    			return false;
    		}
    		
    		var yzm =document.getElementById("txt-yzm").value;
    		
    		if(yzm==""){
    			
    			alert("请输入验证码");
    			return false;
    		}
    		
    		

    		
//  	    var telLogin =document.getElementById("text-tel-2").value;
//  		
//  		 if(!(/^1[34578]\d{9}$/.test(telLogin))){
//  			
//  			alert("请输入正确手机号");
//  			return false;
//  		}
    		 
    		return true;
		}
		
/*register 发送验证码*/
		function clksend(obj){
			
			obj.setAttribute("class","yzm-gray");
			
			var Countdown =5;
			
			var timmer =setInterval(function(){
				
				Countdown--;
				
				obj.innerHTML=Countdown+"s";
				
				if(Countdown==0)
				{
					clearInterval(timmer);
					
					obj.innerHTML="获取验证码";
					
					obj.setAttribute("class","btn-before");
				}
			},1000)	
	}


//        function clkdl(){
//			
//  		var tel2 =document.getElementById("text-tel-2").value;
//  		
//  		 if(!(/^1[34578]\d{9}$/.test(tel2))){
//  			
//  			alert("请输入正确手机号");
//  			return false;
//  		}
//  		
//  		var yzm2 =document.getElementById("text-yzm-2").value;
//  		
//  		if(yzm2==""){
//  			
//  			alert("请输入验证码");
//  			return;
//  		}
//  		
//  	    window.location.href="../creditLine/creditLine.html"
//		}
		
//		function clksend(obj){
//			
//			obj.setAttribute("class","yzm-gray");
//			
//			var Countdown =5;
//			
//			var timmer =setInterval(function(){
//				
//				Countdown--;
//				
//				obj.innerHTML=Countdown+"s";
//				
//				if(Countdown==0)
//				{
//					clearInterval(timmer);
//					
//					obj.innerHTML="验证码";
//					
//					obj.setAttribute("class","yzm-light");
//				}
//			},1000)	
//	    }

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