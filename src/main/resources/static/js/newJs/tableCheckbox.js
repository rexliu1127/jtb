(function($){
	$.fn.tableCheck = function(allCheckboxClass){
		var allCheck = $(this).find("th").find(':checkbox');
		var checks = $(this).find('td.pick').find(':checkbox');
		var defaults = {
			selectedRowClass:"active",
		}
		var settings = $.extend(defaults,allCheckboxClass);
		if(allCheckboxClass)
			settings.selectedRowClass = allCheckboxClass;
		/*所有checkbox初始化*/
		$(this).find(":checkbox").prop("checked",false);
		/*全选/反选*/
		allCheck.click(function(){
			var set = $(this).parents('table').find('td.pick').find(':checkbox');
			if($(this).prop("checked")){
				$.each(set,function(i,v){
					$(v).prop("checked",true);
					$(v).parents('tr').addClass(settings.selectedRowClass);
				});
			}else{
				$.each(set,function(i,v){
					$(v).prop("checked",false);
					$(v).parents('tr').removeClass(settings.selectedRowClass);
				});
			}
		});

		/* 监听全选事件 */
		checks.click(function(e){
			e.stopPropagation();//阻止冒泡
			var leng = $(this).parents("table").find('td.pick').find(':checkbox:checked').length;
			/*勾选后该行active*/
			if($(this).prop('checked')){
				$(this).parents('tr').addClass(settings.selectedRowClass);
			}else{
				$(this).parents('tr').removeClass(settings.selectedRowClass);
			}
			if(leng == checks.length){
				allCheck.prop('checked',true);
			}else{
				allCheck.prop("checked",false);
			}			 
		});
		/*点击table触发复选框*/
		$(this).find("td.pick").click(function(){
			var _tr = $(this).parents('tr');
			_tr.find(":checkbox").trigger("click");
		});		
	}
})(jQuery);