$(function () {
    vm.getConfigList();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        title:'机审条件配置',
		config: {},
        configList:{},
		configIdList: []
	},
	methods: {
		saveOrUpdate: function () {
			var url = "opt/machine_auth_config/save";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.config),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(){
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
        getConfigList: function(){
            $.get(baseURL + "opt/machine_auth_config/info", function(r){
                vm.config = r.config;
            });
        }
	}
});