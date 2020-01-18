
  function clkSQ(){
	
		var mobile = $("#txt-tel").val();
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
		
		var mobile = $("#txt-tel").val();
		
		if(isMobile(mobile)){

			if(flag){
				return ;
			}

			flag = true;


				//setClass(obj,"yzm-gray");
				
				//obj.setAttribute("class","yzm-gray");

				var url = "";
				if(type == "login"){
					url = URL_SMS_LOGIN;
				}else{
					url = URL_SMS_REGISTER;
				}
				
				GET(url+"/"+mobile,"",function(result){

					if(result.code == 0){

						var Countdown = 90;
			
						var timmer =setInterval(function(){
							
							Countdown--;
							
							obj.innerHTML=Countdown+"s";
							
							if(Countdown==0)
							{
								clearInterval(timmer);
								
								obj.innerHTML="获取验证码";
								
								//obj.setAttribute("class","btn-before");
								setClass(obj,"btn-before");

								flag = false;
							}
						},1000);
						
					}else{

						flag = false;
						obj.innerHTML="获取验证码";
								
						//obj.setAttribute("class","btn-before");
						setClass(obj,"btn-before");
						
						alert(result.msg);
					}
					
				});
		}else{
			 alert("手机号格式不正确");
		}
}


