$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'opt/channel/order_list',
        postData: {'status': vm.q.status,
            'channelId': vm.q.channelId,
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate},
        datatype: "json",
        colModel: [
			{ label: 'requestId', name: 'requestId', index: 'request_id', width: 40, hidden: true, key: true },
			{ label: '渠道', name: 'channelName', index: 'channel_name', width: 80 , align: 'center'},
			{ label: '用户信息', name: 'mobile', index: 'user_id', width: 80, formatter: function(value, options, row){
                return row.name + '<br />' + value;
            }},
            { label: '申请时间', name: 'createTime', index: 'create_time', width: 80 , align: 'center'},
			{ label: '状态', name: 'status', index: 'status', width: 40, align: 'center' , formatter: function(value, options, row){
                    if (!value || value.value === -1) {
                        return '<span class="label label-default">未提交</span>';
                    } else if (value.value === 0) {
                    // 待审核
                    return '<span class="label label-info">' + value.displayName + '</span>';
                } else if (value.value === 1) {
                    // 审核中
                    return '<span class="label label-warning">' + value.displayName + '</span>';
                } else if (value.value === 2) {
                    // 已放款
                    return '<span class="label label-success">' + value.displayName + '</span>';
                } else if (value.value === 3) {
                    // 拒绝受理
                    return '<span class="label label-danger">' + value.displayName + '</span>';
                } else if (value.value === 4) {
                    // 已结清
                    return '<span class="label label-success">' + value.displayName + '</span>';
                } else if (value.value === 5 || value.value === 6 || value.value === 7) {
                    // 逾期, 失联, 取消
                    return '<span class="label label-warning">' + value.displayName + '</span>';
                }
            }}
            // ,
            // { label: '操作', name: 'requestUuid', index: 'request_uuid', width: 40, formatter: function(value, options, row){
            //     return '<button type="button" onclick="openRequest(\'' + value + '\')" class="btn btn-link">详情</button>';
            // }}
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

    vm.init();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		authRequest: {},
        selectedIdList: [],
        requestStatusList: [],
        channelList: [],
        q: {
		    applyStartDate: moment().subtract(6, 'days').format('YYYY-MM-DD'),
            applyEndDate: moment().format('YYYY-MM-DD')
        }
	},
	methods: {
	    init: function () {
            $.get(baseURL + "autha/request/status/list", function(r){
                vm.requestStatusList = r.list;
            });
            $.get(baseURL + "opt/channel/list?_search=false&limit=100&page=1&sidx=&order=asc", function(r){
                vm.channelList = r.page.list;
            });
        },
		query: function () {
            // console.log(vm.q);
			vm.reload();
		},
        reset: function () {
            vm.q = {
                applyStartDate: moment().subtract(6, 'days').format('YYYY-MM-DD'),
                applyEndDate: moment().format('YYYY-MM-DD')
            };
            $('#auditTimeRange').val('');
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'status': vm.q.status,
                        'channelId': vm.q.channelId,
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate},
                page:page
            }).trigger("reloadGrid");

		}
	}
});


function openRequest(uuid) {
    parent.layer.open({
        type: 2,
        skin: 'layui-layer-molv',
        title: '处理申请单',
        shadeClose: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['90%', '95%'],
        content: baseURL + 'modules/auth/request_detail.html?id=' + uuid,
        cancel: function (index) {
        }
    });
}
