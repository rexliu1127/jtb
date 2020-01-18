$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'spreadManager/list',
        postData: {
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate,
            'queryMerchantNo':vm.q.queryMerchantNo
        },
        datatype: "json",
        colModel: [
            { label: '日期', name: 'create_time', width: 80,align: 'center'},
            { label: '商户编号', name: 'merchant_no', width: 75,align: 'center', },
            { label: '商户名称', name: 'name', width: 75,align: 'center', },
			{ label: '账户余额', name: 'after_amount',  width: 80,align: 'center'},
			{ label: '当前状态', name: 'status',  width: 80,align: 'center',formatter:function(value, options, row){
				if(value==1){
					return "<span style='color:green'>已完成</span>";
				}else if(value==-1){
					return "<span style='color:green'>已停止</span>";
				}else if(value==0){
					return "<span style='color:blue'>已启用</span>";
				}else{
					return "<span style='color:red'>未知状态</span>";
				}
			}},
			{ label: '需求流量数', name: 'flow_count',  width: 80,align: 'center'},
			{ label: '已完成流量数', name: 'complete_count',  width: 80,align: 'center'},
			{ label: '转化率', name: 'payment_count',  width: 80,align: 'center',formatter:function(value, options, row){
				if(row.complete_count==0){
					return "0.00%";
				}
				var rate = value/row.complete_count*100;
				return rate.toFixed(2)+"%";
			}}
        ],
		viewrecords: true,
        height: 'auto',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25,
        autowidth:true,
        multiselect: false,
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

    $('#applyTimeRange').daterangepicker({
        autoUpdateInput: false,
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
    $('#applyTimeRange').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
    });

    $('#applyTimeRange').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });
    vm.init();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		authRequest: {},
        q: {
		    status: null,
		    queryMerchantNo:null,
        },
        merchantList:null
	},
	methods: {
        init: function () {
        	$.get(baseURL + "sys/sysfunrecord/merchantList", function(r){
    			var options = ['<option value="">全部</option>'];
                var list = r.list;
                if(list&&list.length){
                	for(var i=0;i<list.length;i++){
                		var obj = list[i];
                		var merchantName = obj.name;
                		var merchantNo = obj.merchantNo;
                		options.push('<option value="'+merchantNo+'">'+merchantName+'</option>')
                	}
                }
                $("#targetMerchant").html(options.join(""));
                $("#targetMerchant").on("change",function(){
                	var v = $(this).val();
                	vm.q.queryMerchantNo=v==''?null:v;
                })
            });
        },
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                applyStartDate:null,
                applyEndDate: null
            };
            $('#applyTimeRange').val('');
            $("#targetMerchant").val("");
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate,
                        'queryMerchantNo': vm.q.queryMerchantNo
                    },
                page:page
            }).trigger("reloadGrid");
		}
	}
});

