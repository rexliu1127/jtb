$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'opt/channel/flowSpreadList',
        postData: {
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate,
            'channelId': vm.q.channelId
        },
        datatype: "json",
        colModel: [
            { label: '日期', name: '_date', width: 80,align: 'center'},
            { label: '渠道来源', name: 'channel_name', width: 75,align: 'center', },
			{ label: '当日推送量', name: '_count',  width: 80,align: 'center'}
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
    		$.get(baseURL + "opt/channel/getFlowChannel", function(r){
    			var options = ['<option value="">全部</option>'];
                var list = r.list;
                if(list&&list.length){
                	for(var i=0;i<list.length;i++){
                		var obj = list[i];
                		var channelName = obj.name;
                		var channelId = obj.channelId;
                		options.push('<option value="'+channelId+'">'+channelName+'</option>')
                	}
                }
                $("#targetChannel").html(options.join(""));
                $("#targetChannel").on("change",function(){
                	var v = $(this).val();
                	vm.q.channelId=v==''?null:v;
                })
            });
        },
		query: function () {
			vm.reload();
		},
        reset: function () {
            vm.q = {
                applyStartDate:null,
                applyEndDate: null,
                channelId:null
            };
            $('#applyTimeRange').val('');
            $("#targetChannel").val('');
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate,
                        'channelId':vm.q.channelId
                    },
                page:page
            }).trigger("reloadGrid");
		}
	}
});

