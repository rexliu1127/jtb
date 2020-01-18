$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sys_fun_type/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 50, hidden: true, key: true },
            { label: '商户编号', name: 'merchantNo',align: 'center', width: 80 },
			{ label: '公司名称', name: 'merchantName',align: 'center', width: 80 },
			{ label: '充值类型', name: 'funType',align: 'center', width: 80 ,
                formatter: function(value, options, row){
                    if (value== 1) {
                        return '多头报告';
                    }else if(value== 2){
                        return '支付宝报告';
					} else {
                        return '';
                    }
                }
			},
			{ label: '单笔费用', name: 'singleFee',align: 'center', width: 80 },
			{ label: '创建时间', name: 'createTime',align: 'center', width: 80 }
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
            merchantNo: null
        },
		selecting: false,
		showList: true,
		title: null,
		selectedMerchant: '',
        mrchantList: [],
		sysFunType: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.selectedMerchant = '';
			vm.sysFunType = {};
            // 获取有效商户列表
            vm.getMrchantList();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            // 获取有效商户列表
            vm.getMrchantList(function() {
                vm.getInfo(id);
            });
		},
		selectMerchant: function(op) {
			vm.selectedMerchant = op.name + '(' + op.merchantNo + ')';
			vm.selecting = false;
            vm.sysFunType.merchantNo=op.merchantNo;
		},
		saveOrUpdate: function (event) {
            if(vm.validator()){
                return ;
            }
			var url = vm.sysFunType.id == null ? "sys/sys_fun_type/save" : "sys/sys_fun_type/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysFunType),
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
				    url: baseURL + "sys/sys_fun_type/delete",
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
        // 获取有效商户列表
        getMrchantList: function(callback){
            if (vm.mrchantList && vm.mrchantList.length) {
                if (callback) {
                    callback();
                }

                return;
            }

            $.get(baseURL + "sys/sys_fun_type/mrchantList", function(r){
                vm.mrchantList = r.list;

                if (callback) {
                    callback();
                }
            });
        },
		getInfo: function(id){
			vm.selectedMerchant = '';
			$.get(baseURL + "sys/sys_fun_type/info/"+id, function(r){
                vm.sysFunType = r.sysFunType;
				vm.selectedMerchant = vm.sysFunType.merchantNo;
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'merchantNo': vm.q.merchantNo},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
            if(isBlank(vm.sysFunType.funType)){
                alert("充值类型不能为空");
                return true;
            }

            if(isBlank(vm.sysFunType.singleFee) || isNaN(vm.sysFunType.singleFee)){
                alert("单笔费用不能为空且必须是数字");
                return true;
            }

        }
	},
	computed: {
		filteredMerList: function () {
			if (!this.selecting || this.selectedMerchant.length == 0) {
				return this.mrchantList;
			}

			var result= [];
			for (var i = 0; i < this.mrchantList.length; i++) {
				if ((this.mrchantList[i].name + '(' + this.mrchantList[i].merchantNo + ')').indexOf(this.selectedMerchant) > -1) {
					result.push(this.mrchantList[i]);
				}
			}
			return result;
		}
	}
});