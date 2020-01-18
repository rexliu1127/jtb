$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysmerchant/list',
        datatype: "json",
        colModel: [			
			{ label: '公司ID', name: 'merchantNo', index: 'merchant_no', width: 80, align: 'center', key: true },
			{ label: '公司名称', name: 'name', index: 'name', align: 'center', width: 80 },
            { label: '状态', name: 'status',align: 'center', align: 'center', width: 40, formatter: function(value, options, row){
                return value === 0 ?
                    '<span class="label label-danger">禁用</span>' :
                    '<span class="label label-success">正常</span>';
            }},
			{ label: '创建时间', name: 'createTime', index: 'create_time',align: 'center', width: 80 }
        ],
		viewrecords: true,
        height: 'auto',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		sysMerchant: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysMerchant = {};
		},
		update: function (event) {
			var merchantNo = getSelectedRow();
			if(merchantNo == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(merchantNo)
		},
		saveOrUpdate: function (event) {
			var url = vm.sysMerchant.merchantNo == null ? "sys/sysmerchant/save" : "sys/sysmerchant/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysMerchant),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var merchantNos = getSelectedRows();
			if(merchantNos == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/sysmerchant/delete",
                    contentType: "application/json",
				    data: JSON.stringify(merchantNos),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(merchantNo){
			$.get(baseURL + "sys/sysmerchant/info/"+merchantNo, function(r){
                vm.sysMerchant = r.sysMerchant;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});