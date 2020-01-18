$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysfun/list',
        datatype: "json",
        colModel: [
            { name: 'id', index: "id", key: true, hidden: true },
            { label: '商户编号', name: 'merchantNo', index: 'merchant_no',align: 'center', width: 80 },
            { label: '商户名称', name: 'merchantName', index: 'merchant_name',align: 'center', width: 80 },
            { label: '总额(元)', name: 'totalAmount', index: 'total_amount', align: 'center',width: 80 },
			{ label: '可用余额(元)', name: 'remainingSum', index: 'remaining_sum',align: 'center', width: 80 },
			{ label: '是否有风险', name: 'isrist', index: 'isrist', align: 'center',width: 80,formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-danger">有风险</span>' :
                        '<span class="label label-success">无风险</span>';
                } }


        ],
		viewrecords: true,
        height: 385,
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
        q:{
            merchantname: null,
            merchantNo: null

        },
		showList: true,
		title: null,
		sysFun: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysFun = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.sysFun.id == null ? "sys/sysfun/save" : "sys/sysfun/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysFun),
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
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/sysfun/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
        updateRist: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要设置该商户为风险商户？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/sysfun/updateRist",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get(baseURL + "sys/sysfun/info/"+id, function(r){
                vm.sysFun = r.sysFun;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'merchantname': vm.q.merchantname,'merchantNo': vm.q.merchantNo},
                page:page
            }).trigger("reloadGrid");
		}
	}
});