$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/txlender/list',
        datatype: "json",
        colModel: [
            { name: 'id', index: "id", key: true, hidden: true },
            { label: '姓名', name: 'name', index: 'name', align: 'center',width: 80 },
            { label: '手机号码', name: 'mobile', index: 'mobile', align: 'center',width: 80 },
			{ label: '商户编号', name: 'merchantNo', index: 'merchant_no', align: 'center',width: 80 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', align: 'center', width: 80 },
            { label: '状态', name: 'status', width: 50, align: 'center', formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">正常</span>';
            }}
             ,
             { label: '操作', name: 'id', index: 'id', width: 40, align: 'center', formatter: function(value, options, row){

                     var status = row.status;
                     if((status == 1)  && hasPermission('auth:txlender:update') )
			 		{
                         return '<button type="button" onclick="openRequest(\'' + value + '\')" class="btn btn-link">打借条</button>';
			 		}
             	   return '';
             }}
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
            queryname: null
        },
        transaction: {},
		showList: true,
		title: null,
		txLender: {
            status:1
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.txLender = {
                status:1
			};
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

            if(vm.validator()){
                return ;
            }

			var url = vm.txLender.id == null ? "tx/txlender/save" : "tx/txlender/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.txLender),
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
				    url: baseURL + "tx/txlender/delete",
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
			$.get(baseURL + "tx/txlender/info/"+id, function(r){
                vm.txLender = r.txLender;
            });
		},

		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.queryname},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {

            if(isBlank(vm.txLender.name)){
                alert("姓名不能为空");
                return true;
            }

            if(isBlank(vm.txLender.mobile) ){
                alert("电话不能为空");
                return true;
            }

        }
	}
});


function  openRequest(uuid)
{
        parent.layer.open({
            type: 2,
            skin: 'layui-layer-molv',
            title: '打借条',
            shadeClose: false,
            area: ['800px', '480px'],
            content: baseURL + 'modules/tx/txlender_transaction.html?id=' + uuid,

            cancel: function (index) {

                vm.reload();
            }
        });
}