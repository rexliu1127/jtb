$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/user/list',
        datatype: "json",
        colModel: [
			{ name: 'userId', index: "user_id", key: true, hidden: true },
            { label: '用户信息', name: 'name', width: 75, formatter: function(value, options, row){
                return (value ? value : '') + '<br />' + row.mobile + '<br />' + (row.idNo ? row.idNo : '');
            }},
			{ label: '总借款额(元)', name: 'totalBorrowedAmount', align: 'center', width: 75 },
            { label: '总借款笔数', name: 'totalBorrowedCount', align: 'center', width: 50 },
            { label: '总还款额(元)', name: 'totalRepayAmount', align: 'center', width: 75 },
            { label: '当前逾期额(元)', name: 'currentOverdueAmount', align: 'center', width: 75 },
            { label: '当前逾期笔数', name: 'currentOverdueCount', align: 'center', width: 50 }
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			keyword: null
		},
		showList: true,
		title:null,
		user:{
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'keyword': vm.q.keyword},
                page:page
            }).trigger("reloadGrid");
		}
	}
});
