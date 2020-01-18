$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/withdrawal/list',
        datatype: "json",
        colModel: [
			{ name: 'id', index: "id", key: true, hidden: true },
            { label: '提现时间', name: 'createTime', width: 75, align: 'left'},
			{ label: '提现人', name: 'name', width: 75 , align: 'left'},
            { label: '提现金额(元)', name: 'amount', align: 'left', width: 50 },
            { label: '手续费(元)', name: 'feeAmount', align: 'left', width: 50 },
			{ label: '状态', name: 'status', align: 'left',width: 40, formatter: function(value, options, row){
			    if (value.value === 1) {
                                    // 待审核
                                    return '<span class="label label-warning">' + value.displayName + '</span>';
                                } else if (value.value === 2) {
                                    // 已审核
                                    return '<span class="label label-info">' + value.displayName + '</span>';
                                } else if (value.value === 3) {
                                    // 已拒绝
                                    return '<span class="label label-danger">' + value.displayName + '</span>';
                                } else if (value.value === 4) {
                                    // 已放款
                                    return '<span class="label label-success">' + value.displayName + '</span>';
                                } else if (value.value === 5) {
                                    // 提现处理中
                                    return '<span class="label label-info">' + value.displayName + '</span>';
                                } else if (value.value === 6) {
                                    // 提现处理中
                                    return '<span class="label label-danger">' + value.displayName + '</span>';
                                }
			}},
			{ label: '操作员', name: 'approvalUserName', align: 'left', width: 50},
            { label: '操作', name: 'action', index: 'action', align: 'center',width: 40, formatter: function(value, options, row){
                if (row.status.value === 1 && hasPermission('tx:withdrawal:update')) {
                    return '<button type="button" onclick="approve(' + row.id + ', ' + row.amount + ')" class="btn btn-link">审核</button>';
                } else {
                    return "";
                }
            }}
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
        "startDate": moment().subtract(6, 'days'),
        "endDate": moment()
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

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			keyword: null,
			status:null,
			applyStartDate: null,
			applyEndDate: null
		},
		showList: true
	},
	methods: {
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                keyword: null,
                status:null,
                applyStartDate: null,
                applyEndDate: null
            };
            $('#applyTimeRange').val('');
            vm.reload();
        },
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'keyword': vm.q.keyword, 'status': vm.q.status, 'applyStartDate': vm.q.applyStartDate, 'applyEndDate': vm.q.applyEndDate},
                page:page
            }).trigger("reloadGrid");
		}
	}
});


function approve(id, amount) {

    parent.layer.confirm(
        '是否同意付款金额' + amount + '元?',
        {btn: ['同意付款','拒绝付款']},
    	function(index){//确定事件
    		var data = "id="+id+'&status=2';
            $.ajax({
                type: "POST",
                url: baseURL + "tx/withdrawal/status",
                data: data,
                dataType: "json",
                success: function(r){
                    if(r.code == 0){
                        layer.alert('审核提现申请成功', function(){
                            location.reload();
                        });
                    }else{
                		layer.alert(r.msg);
                	}

                    parent.layer.close(index);
                }
            });
        },
        function(index) {
    		var data = "id="+id+'&status=3';
                        $.ajax({
                            type: "POST",
                            url: baseURL + "tx/withdrawal/status",
                            data: data,
                            dataType: "json",
                            success: function(r){
                                if(r.code == 0){
                                    layer.alert('拒绝提现申请成功', function(){
                                        location.reload();
                                    });
                                }else{
                            		layer.alert(r.msg);
                            	}

                                parent.layer.close(index);
                            }
                        });
        }
        );
}
