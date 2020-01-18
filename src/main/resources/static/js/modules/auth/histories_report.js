$(function () {
    vm.load();

    $('#beginDateRange').daterangepicker({
        singleDatePicker: true,
        timePicker24Hour : true,
        autoApply: true,
        "locale": {
            format: 'YYYY-MM-DD',
            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                '七月', '八月', '九月', '十月', '十一月', '十二月'
            ],
            applyLabel: "应用",
            cancelLabel: "取消",
            resetLabel: "重置",
        }
    },function(start, end, label) {
        vm.transactionEvt.beginDate = start.format('YYYY-MM-DD');

    });

    $('#repayDateRange').daterangepicker({
        singleDatePicker: true,
        timePicker24Hour : true,
        autoApply: true,
        "locale": {
            format: 'YYYY-MM-DD',
            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                '七月', '八月', '九月', '十月', '十一月', '十二月'
            ],
            applyLabel: "应用",
            cancelLabel: "取消",
            resetLabel: "重置",
        }
    }, function(start, end, label) {
        vm.transactionEvt.repayDate = start.format('YYYY-MM-DD');
    });

});

var options = {
};

var viewer;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        count: 1,
        uuid: T.p('id'),
        type:T.p('type'),
        userId:T.p('userId'),
        authUserVO:{},
        histories:{},
        audit: {},
        errMsg:{},
        txlenderList: {},
        transactionEvt: {
            beginDate:null,
            repayDate :null,
            rate:24,
            usageType : 1
        },
        request: {
            status: {}
        }
	},
	methods: {
        load: function () {
            $.get(baseURL + 'autha/report/histories/historiesReport/' +vm.uuid, function(r){
                if(r.code!=0){
                    vm.errMsg=r.msg;
                    $("#nullHtml").show();
                    $("#bodyHtml").hide();
                    return ;
                }else{
                    $("#bodyHtml").show();
                }
                vm.request = r.authRequestVO;
                vm.authUserVO = r.authUserVO;
                vm.count = r.count;
                vm.histories = r.histories;
                vm.txlenderList = r.txlenderList;

            });
        }
	}
});
