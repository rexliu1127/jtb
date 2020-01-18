
function otherBtnClick(){
    var otherWay=$(".otherWay");
    otherWay.addClass("dialog-show");
};

function iouBTNClick(){
    $(".iou").addClass("dialog-show");
};
// 弹框关闭、移除
function hid(){	
  var index = $(this).index();
  $(".dialog").removeClass("dialog-show");    
};
function remove(){
  var index = $(this).index();
  $(".dialog").eq(index).remove();
};
//END
$(document).ready(function(){
	$('.distinguish table tbody tr:even').addClass('footable-even');
	$('.distinguish table tbody tr:odd').addClass('footable-odd');	
});