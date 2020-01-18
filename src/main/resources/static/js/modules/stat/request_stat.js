$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'autha/auditor/stat',
        datatype: "json",
        colModel: [
            { label: 'userId', name: 'userId', index: 'user_id', width: 50, hidden: true, key: true },
            { label: '审核人名称', name: 'name', index: 'name', width: 80 , formatter: function(value, options, row){
                    return value ? value : (row.username ? row.username : '');
                }},
            { label: '待审核数量与占比', name: 'pendingCount', index: 'pending_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '审核中数量与占比', name: 'processingCount', index: 'processing_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '已放款数量与占比', name: 'approvedCount', index: 'approved_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '拒绝受理数量与占比', name: 'rejectedCount', index: 'rejected_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '已结清数量与占比', name: 'completedCount', index: 'completed_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '逾期数量与占比', name: 'overdueCount', index: 'overdue_count', align: 'center', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '总数量', name: 'totalCount', index: 'totalCount', align: 'center', width: 80 }
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

    $("#jqGrid2").jqGrid({
        url: baseURL + 'autha/assignee/stat',
        datatype: "json",
        colModel: [
            { label: 'userId', name: 'userId', index: 'user_id', width: 50, hidden: true, key: true },
            { label: '处理人名称', name: 'name', index: 'name', width: 80 , formatter: function(value, options, row){
                    return value ? value : (row.username ? row.username : '');
                }},
            { label: '待审核数量与占比', name: 'pendingCount', index: 'pending_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '审核中数量与占比', name: 'processingCount', index: 'processing_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '已放款数量与占比', name: 'approvedCount', index: 'approved_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '拒绝受理数量与占比', name: 'rejectedCount', index: 'rejected_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '已结清数量与占比', name: 'completedCount', index: 'completed_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '逾期数量与占比', name: 'overdueCount', index: 'overdue_count', width: 80 , formatter: function(value, options, row){
                    return value + ' / ' + (value * 100.0 / row.totalCount).toFixed(2) + '%';
                }},
            { label: '总数量', name: 'totalCount', index: 'totalCount', width: 80 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager2",
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
            $("#jqGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    $('#assigneeLink').on('click', function () {
        $("#jqGrid2").setGridWidth($("#jqGrid").width());
    });

    vm3.init();
});

var vm = new Vue({
	el:'#auditorApp',
	data:{
		q: {name: null}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData: {name: vm.q.name},
                page:page
            }).trigger("reloadGrid");
		}
	}
});


var vm2 = new Vue({
    el:'#assigneeApp',
    data:{
        q: {name: null}
    },
    methods: {
        query: function () {
            vm2.reload();
        },
        reload: function (event) {
            vm2.showList = true;
            var page = $("#jqGrid2").jqGrid('getGridParam','page');
            $("#jqGrid2").jqGrid('setGridParam',{
                postData: {name: vm2.q.name},
                page:page
            }).trigger("reloadGrid");
        }
    }
});

var vm3 = new Vue({
    el:'#sumApp',
    data:{
        stat: {
            totalPendingCount: 0,
            totalProcessingCount: 0,
            totalApprovedCount: 0,
            totalCompletedCount: 0,
            totalRejectedCount: 0,
            totalOverdueCount: 0,
            totalCount: 1
        }
    },
    methods: {
        init: function () {
            $.get(baseURL + 'autha/request/stat', function(r){
                vm3.stat = r.stat;
            });
        },
        formatNumber: function(value) {
            return value.toFixed(0);
        }
    }
});