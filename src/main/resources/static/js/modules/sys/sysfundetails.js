$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysfundetails/list',
        datatype: "json",
        colModel: [
            { name: 'id', index: "id", key: true, hidden: true },
            { label: '商户编号', name: 'merchantNo', align: 'center',index: 'merchant_no', width: 80 },
			{ label: '借款人手机', name: 'borrowerPhone', align: 'center',index: 'borrower_phone', width: 80 },
            { label: '用户名称', name: 'userName',align: 'center', index: 'user_name', width: 80 },
            { label: '费用类型', name: 'funType',align: 'center', index: 'fun_type', width: 80,formatter: function(value, options, row){
                    if (value === 1) {

                        return '多头报告';
                    } else if (value === 2) {

                        return '支付宝报告';
                    } else if (value === 3) {
                        return '流量采购';
                    }
                    else
					{
						return '';
					}
                } },
			{ label: '费用(元)', name: 'amount',align: 'center', index: 'amount', width: 80 },
			{ label: '消费时间', name: 'createTime', align: 'center',index: 'create_time', width: 80 }


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
            var merchantNo=data.merchantno;
            if(merchantNo!='00'){
               // $("#jqGrid").setGridParam().hideCol("merchantNo").trigger("reloadGrid");
                $(".hideMerchantNo").hide();
            }
        }
    });

    $('#applyTimeRange').daterangepicker({
        autoApply: true,
        locale: {
            format: 'YYYY-MM-DD',
            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                '七月', '八月', '九月', '十月', '十一月', '十二月'
            ]
        },
        ranges: {
            '当天': [moment(), moment()],
            '一星期': [moment().subtract(6, 'days'), moment()],
            '当前月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "alwaysShowCalendars": true,
        "startDate": vm.q.applyStartDate,
        "endDate": vm.q.applyEndDate
    }, function(start, end, label) {
        vm.q.applyStartDate = start.format('YYYY-MM-DD');
        vm.q.applyEndDate = end.format('YYYY-MM-DD');
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            keyword: null,
            merchantNo: null,
            funtype:null,
            applyStartDate: moment().subtract(6, 'days').format('YYYY-MM-DD'),
            applyEndDate: moment().format('YYYY-MM-DD')
        },
		showList: true,
		title: null,
		sysFunDetails: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                keyword: null,
                merchantNo:null,
                funtype:null,
                applyStartDate: moment().subtract(6, 'days').format('YYYY-MM-DD'),
                applyEndDate: moment().format('YYYY-MM-DD')
            };
            var applyTimePicker = $('#applyTimeRange').data('daterangepicker');
            applyTimePicker.setStartDate(vm.q.applyStartDate);
            applyTimePicker.setEndDate(vm.q.applyEndDate);

            vm.reload();
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysFunDetails = {};
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
        exp: function () {
            if (!$("#jqGrid").getGridParam("selrow")) {
                confirm('确定要导出当前搜索条件的所有记录吗？', function(){
                    window.open(baseURL + "sys/sysfundetails/export?token=" + token + '&' + jQuery.param(vm.q), 'downloadFrame');
                    return true;
                });
            } else {
                var requestIds = getSelectedRows();
                confirm('确定要导出选中的记录吗？', function(){
                    window.open(baseURL + "sys/sysfundetails/export_by_id?token=" + token + '&requestIds=' + requestIds, 'downloadFrame');
                });
            }
        },
		saveOrUpdate: function (event) {
			var url = vm.sysFunDetails.id == null ? "sys/sysfundetails/save" : "sys/sysfundetails/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysFunDetails),
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
				    url: baseURL + "sys/sysfundetails/delete",
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
			$.get(baseURL + "sys/sysfundetails/info/"+id, function(r){
                vm.sysFunDetails = r.sysFunDetails;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'keyword': vm.q.keyword,'merchantNo': vm.q.merchantNo, 'funtype': vm.q.funtype, 'applyStartDate': vm.q.applyStartDate, 'applyEndDate': vm.q.applyEndDate},
                page:page
            }).trigger("reloadGrid");
		}
	}
});