$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'flowAllocationRecord/list',
        postData: {
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate
        },
        datatype: "json",
        colModel: [
            { label: '日期', name: '_date', width: 80,align: 'center'},
            { label: '流量需求数', name: 'total', width: 75,align: 'center', },
            { label: '流量数', name: 'total', width: 75,align: 'center', },
			{ label: '待审核', name: 'progress',  width: 80,align: 'center'},
			{ label: '已放款', name: 'payment',  width: 80,align: 'center'},
			{ label: '已拒绝', name: 'reject',  width: 80,align: 'center'},
			{ label: '已结清', name: 'finish',  width: 80,align: 'center'}
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
		    status: null
        }
	},
	methods: {
        init: function () {
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
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate
                    },
                page:page
            }).trigger("reloadGrid");
		}
	}
});

