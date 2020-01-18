$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/log/merchantLogList',
        postData: {
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate},
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 30,align: 'center', key: true },
            { label: '登录账号', name: 'username',align: 'center', width: 50 },
			{ label: '用户名', name: 'sysUserName',align: 'center', width: 50 },
			{ label: '操作类型', name: 'operation',align: 'center', width: 70 },
			{ label: 'IP地址', name: 'ip',align: 'center', width: 70 },
			{ label: '操作时间', name: 'createDate',align: 'center', width: 90 }
        ],
		viewrecords: true,
        height: 385,
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
			key: null,
            applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
            applyEndDate: moment().endOf('month').format('YYYY-MM-DD')
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                key: null,
                applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
                applyEndDate: moment().endOf('month').format('YYYY-MM-DD')
            };
            var applyTimePicker = $('#applyTimeRange').data('daterangepicker');
            applyTimePicker.setStartDate(vm.q.applyStartDate);
            applyTimePicker.setEndDate(vm.q.applyEndDate);
            vm.reload();
        },
		reload: function (event) {
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'key': vm.q.key,
                    'applyStartDate': vm.q.applyStartDate,
                    'applyEndDate': vm.q.applyEndDate
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});