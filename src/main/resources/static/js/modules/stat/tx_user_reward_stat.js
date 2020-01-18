$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/reward/stat',
        datatype: "json",
        colModel: [
            { label: 'userId', name: 'userId', index: 'user_id', width: 50, hidden: true, key: true },
            { label: '月份', name: 'period', index: 'period', width: 80},
            { label: '一级推荐人', name: 'name', index: 'name', align: 'center', width: 80},
            { label: '二级推荐注册数', name: 'level2InviteeCount', index: 'level2_user_count', align: 'center', width: 80 },
            { label: '三级推荐注册数', name: 'level3InviteeCount', index: 'level3_user_count', align: 'center', width: 80 },
            { label: '有效推荐人数', name: 'validInviteeCount', index: 'valid_invitee_count', align: 'center', width: 80 },
            { label: '当月所得奖励', name: 'reward', index: 'userReward', align: 'center', width: 80 },
            { label: '当前账户余额', name: 'balance', index: 'user_balance', align: 'center', width: 80 }
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

    vm.initSumStat();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q: {name: null},
		stat: {
		    totalTxAmount: 0,
		    totalTxUserInvitee: 0,
		    validUserInvitee: 0
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		initSumStat: function () {
                    $.get(baseURL + 'tx/reward/sum', function(r){
                        vm.stat = r.stat;
                    });
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
