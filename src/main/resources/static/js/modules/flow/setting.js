$(function(){
	
	function queryLastSetting(){
		$.ajax({
			url:baseURL+"flow/queryLastSetting",
			type:"POST",
			dataType:"JSON",
			success:function(resp){
				if(resp.code==0){
					if(resp.setting&&resp.setting.status==0){
						$("#btn-start").attr("disabled",true);
						$("input[name='status']").attr("disabled",true);
						$("#flowCount").val(resp.setting.flowCount).attr("disabled",true);
						$("#btn-tips").show().css("font-weight",100);
						$("#statusOpen").attr("checked",true);
						queryChannelRender(resp.setting.channelId,function(){
							$("#targetChannel").attr("disabled",true);
						});
					}else{
						queryChannelRender(null);
					}
					$("#remainingSum").text(resp.remainingSum);
					$("#remainingSumInput").val(resp.remainingSum);
					$("#cpa").text(resp.cpa);
					$("#cpaInput").val(resp.cpa);
					
				}else{
					alert(resp.msg);
				}
			},
			error:function(){
				alert("系统忙");
			}
		})
	}
	
	function queryChannelRender(targetChannelId,callback){
		$.get(baseURL + "opt/channel/list?_search=false&limit=100&page=1&sidx=&order=asc", function(r){
			var options = [];
            var list = r.page.list;
            if(list&&list.length){
            	for(var i=0;i<list.length;i++){
            		var obj = list[i];
            		var channelName = obj.name;
            		var channelId = obj.channelId;
            		options.push('<option value="'+channelId+'">'+channelName+'</option>')
            	}
            	$("#targetChannel").html(options.join(""));
            	if(targetChannelId){
            		$("#targetChannel").val(targetChannelId);
            	}
            	if(callback){
            		callback();
            	}
            }
        });
	}
	
	function registerBtnEvent(){
		$("#btn-start").on("click",function(){
			var isOpen = $('#statusOpen').is(':checked');
			if(!isOpen){
				alert("如需要购买流量，请打开开关")
				return;
			}
			var flowCount = $("#flowCount").val();
			if(!flowCount||flowCount<100){
				alert("流量需求数最低100起")
				return;
			}
			var cpaAmt = $("#cpaInput").val();
			var remain = $("#remainingSumInput").val();
			var planAmt = flowCount*cpaAmt;
			if(planAmt>remain){
				alert("当前需求量预计需要消费"+planAmt+"元,您的余额不足，请充值");
				return;
			}
			if(confirm("当前需求量预计需要消费"+planAmt+"元,是否继续?",function(){
				$.ajax({
					url:baseURL+"flow/saveFlowSetting",
					type:"POST",
					data:{
						flowCount:flowCount,
						channelId:$("#targetChannel").val()
					},
					success:function(resp){
						if(resp.code==0){
							alert("保存成功",function(){
								location.reload();
							});
						}else{
							alert(resp.msg);
						}
					},
					error:function(){
						alert("系统忙,请重试");
					}
				});
			}));
		})
	}
	queryLastSetting();
	registerBtnEvent();
})