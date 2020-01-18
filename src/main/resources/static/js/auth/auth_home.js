//rem布局
(function (doc, win) {
    var docEl = doc.documentElement,
      resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
      recalc = function () {
          var clientWidth = docEl.clientWidth;
          if (!clientWidth) return;
          docEl.style.fontSize = 20 * (clientWidth / 320) + 'px';
      };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
//rem布局结束
$(window).load(function(){
document.getElementById("J-tab").addEventListener('touchend',function(){  
	sdrag = false;  
});  
document.getElementById("J-tab").addEventListener('touchstart',authentication.selectmouse);  
document.getElementById("J-tab").addEventListener('touchmove',authentication.movemouse);  
authentication.init();
});
var authentication = {
    init: function() {
		authentication.isdrag=false; 
		authentication.tx;
		authentication.x;
		authentication.Jday = $("#J-tab");
		authentication.w=0;
		authentication.Jday.find("li").each(function(v){
			authentication.w+=($(this).width());
		});
		authentication.Jday.width(authentication.w);
		$(".advancedBTN").click(function(){
			$(this).toggleClass("open");
			if($(this).hasClass("open")){
				$(this).html("收展<i></i>");
				$(".advanced").animate({height:"20.9rem"});
			}else{
				$(this).html("提升额度认证<i></i>");
				$(".advanced").animate({height:"15rem"});
			}
		});
		$(".submitBtn").delegate(".showCS","click",function(){
			$("#sucessBox").show();
		}); 
		$(".laybg").click(function(){
			$(this).parent("div").hide();
		});
	},
	movemouse:function(e){   
	  if (authentication.isdrag){ 
		  var n = authentication.tx + e.touches[0].pageX - authentication.x;
		  if(n>0){ n = 0;}
		  else if(n < -(authentication.Jday.width() - $(".basiclist").width())){ return ;}
		   authentication.Jday.css("left",n);  
		   return false; 
	  }
	},
	selectmouse:function(e){   
	   authentication.isdrag = true;   
	   authentication.tx = parseInt(authentication.Jday.position().left+0);   
	   authentication.x = e.touches[0].pageX;    
	   return false;   
	} 
}