$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysfunrecord/list',
        datatype: "json",
        colModel: [
            { name: 'id', index: "id", key: true, hidden: true },
            { label: '商户编号', name: 'merchantNo', index: 'merchant_no', align: 'center', width: 80 },
            { label: '商户名称', name: 'merchantName', index: 'merchant_name', align: 'center', width: 80 },
            { label: '可用余额(元)', name: 'availableAmount', index: 'available_amount', align: 'center', width: 80 },
            { label: '本次充值金额(元)', name: 'funAmount', index: 'fun_amount', align: 'center', width: 80 },
            { label: '当前可用余额(元)', name: 'currentAvailableAmount', index: 'current_available_amount', align: 'center', width: 80 },
			{ label: '充值时间', name: 'createTime', index: 'create_time',  align: 'center',width: 80 }

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
        },
        loadComplete: function (data) {
            var merchantNo=data.merchantNo;
            if(merchantNo!='00'){
               // $("#jqGrid").setGridParam().hideCol("merchantNo").trigger("reloadGrid");
               // $("#jqGrid").setGridParam().hideCol("merchantName").trigger("reloadGrid");
                $("#searchTable").hide();
            }
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            merchantname: null,
            merchantno: null
        },
		showList: true,
		title: null,
        merchantList: [],
		sysFunRecord: {},
        selectedMerchant: '',
        selecting: false
    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysFunRecord = {};
            vm.selectedMerchant = '';

			//查询有效商户
            vm.getMerchantList();
		},
        selectMerchant: function(op) {
            vm.selectedMerchant = op.name + '(' + op.merchantNo + ')';
            vm.selecting = false;
            vm.sysFunRecord.merchantNo=op.merchantNo;
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

			var url = vm.sysFunRecord.id == null ? "sys/sysfunrecord/save" : "sys/sysfunrecord/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysFunRecord),
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
				    url: baseURL + "sys/sysfunrecord/delete",
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
            vm.selectedMerchant = '';
			$.get(baseURL + "sys/sysfunrecord/info/"+id, function(r){
                vm.sysFunRecord = r.sysFunRecord;
                vm.selectedMerchant = vm.sysFunRecord.merchantNo;
            });
		},
        getMerchantList: function(callback){

            if (vm.merchantList && vm.merchantList.length) {
                if (callback) {
                    callback();
                }

                return;
            }

            $.get(baseURL + "sys/sysfunrecord/merchantList", function(r){
                vm.merchantList = r.list;

                if (callback) {
                    callback();
                }
            });
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'merchantname': vm.q.merchantname,'merchantno': vm.q.merchantno},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {

            if(isBlank(vm.sysFunRecord.merchantNo)){
                alert("商户不能为空");
                return true;
            }

            if(isBlank(vm.sysFunRecord.funAmount) ){
                alert("充值金额不能为空");
                return true;
            }


            if(!isPositiveInteger(vm.sysFunRecord.funAmount))
			{
                alert("充值金额为正整数");
                return true;
			}

            if(parseInt(vm.sysFunRecord.funAmount) < 100)
			{
                alert("充值金额续不能小于100");
                return true;
			}

        }
	},
    computed: {
        filteredMerList: function () {
            if (!this.selecting || this.selectedMerchant.length === 0) {
                return this.merchantList;
            }

            var result= [];
            for (var i = 0; i < this.merchantList.length; i++) {
                if ((this.merchantList[i].name + '(' + this.merchantList[i].merchantNo + ')').indexOf(this.selectedMerchant) > -1) {
                    result.push(this.merchantList[i]);
                }
            }
            return result;
        }
    }
});