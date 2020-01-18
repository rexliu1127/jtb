
  function clkSQ(){
	
		var mobile = $("#text-tel").val();
		if(!isMobile(mobile)){
			alert("请输入正确手机号");
			return false;
		}
		
		var yzm = $("#txt-yzm").val();
		
		if(yzm== ""){
			alert("请输入验证码");
			return false;
		}
		return true;
}
		

var flag = false;

/**
 * 发送短信码
 * @param {Object} obj
 * @param {Object} type
 */
function clksend(obj,type){
		
		var mobile = $("#text-tel").val();
		
		if(isMobile(mobile)){

				if(flag){
					return ;
				}

				flag = true;

				alert("===============");
				/*
				//obj.setAttribute("class","yzm-gray");
				
				GET(URL_SMS_LOGIN+"/"+mobile,"",function(result){
					
					if(result.code == 0){

						var Countdown = 20;
			
						var timmer =setInterval(function(){
							
							Countdown--;
							
							obj.innerHTML=Countdown+"s";
							
							if(Countdown==0)
							{
								clearInterval(timmer);
								
								obj.innerHTML="获取验证码";
								
								obj.setAttribute("class","btn-before");

								flag = false;
							}
						},1000);
						
					}else{
						
						obj.innerHTML="获取验证码";
								
						obj.setAttribute("class","btn-before");
						
						alert(result.msg);
						flag = false;
					}
					
				});*/
		}else{
			 alert("手机号格式不正确");
		}
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

 