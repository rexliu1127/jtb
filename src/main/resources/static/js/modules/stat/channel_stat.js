$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'opt/channel/stat',
        datatype: "json",
        postData: {
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate},
        colModel: [
            { label: 'channelId', name: 'channelId', index: 'channel_id', width: 50, hidden: true, key: true },
            { label: '渠道名称', name: 'channelName', index: 'channel_name', width: 80, align: 'center'},
            { label: '负责人', name: 'ownerName', index: 'owner_name', width: 80, align: 'center'},
            { label: '注册人数', name: 'userCount', index: 'user_count', width: 80 , align: 'center'},
            { label: '进件数', name: 'totalCount', index: 'total_count', width: 80 , align: 'center'},
            { label: '审核数', name: 'processedCount', index: 'processed_count', width: 80, align: 'center' },
            { label: '审核率', name: 'processedCount', index: 'processed_per', width: 80, align: 'center', formatter: function(value, options, row){
                var total = row.totalCount;
                if (total) {
                    return (value / total * 100).toFixed(1) + "%";
                } else {
                    return '0';
                }
            } },
            { label: '放款数', name: 'approvedCount', index: 'approved_count', width: 80, align: 'center' },
            { label: '放款通过率', name: 'approvedCount', index: 'approved_per', width: 80 , align: 'center', formatter: function(value, options, row){
                                                                                                      var total = row.processedCount;
                                                                                                      if (total) {
                                                                                                          return (value / total * 100).toFixed(1) + "%";
                                                                                                      } else {
                                                                                                          return '0';
                                                                                                      }
            } }
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
		q: {
		    applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
            applyEndDate: moment().format('YYYY-MM-DD')
            }
	},
	methods: {
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
                applyEndDate: moment().format('YYYY-MM-DD')
            };
            var applyTimePicker = $('#applyTimeRange').data('daterangepicker');
            applyTimePicker.setStartDate(vm.q.applyStartDate);
            applyTimePicker.setEndDate(vm.q.applyEndDate);
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData: {'applyStartDate': vm.q.applyStartDate,
                                       'applyEndDate': vm.q.applyEndDate},
                page:page
            }).trigger("reloadGrid");
		}
	}
});
