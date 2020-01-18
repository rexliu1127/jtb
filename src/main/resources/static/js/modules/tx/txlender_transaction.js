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
        uuid: T.p('id'),
        count: 1,
        ydEnabled: false,
        transactionEvt: {
            beginDate:null,
            repayDate :null,
            borrowername:null,
            rate:24,
            usageType : 1
        },
        audit: {},
        request: {
            status: {}
        },
        contacts: [],
        histories: []
    },
    methods: {
        load: function () {

        },
        savetransaction:function(){

            if(vm.validator()){
                return ;
            }

            $.ajax({
                type: "POST",
                url: baseURL + "tx/txlender/transaction",
                data: {rowID: vm.uuid,amount:vm.transactionEvt.amount,beginDate:vm.transactionEvt.beginDate,borrowername:vm.transactionEvt.borrowername,
                    repayDate:vm.transactionEvt.repayDate,rate:vm.transactionEvt.rate,usageType:vm.transactionEvt.usageType},
                dataType: "json",
                success: function(r){
                    if(r.code == 0){
                        layer.close();
                        layer.alert('打借条成功', function(){
                            location.reload();
                            parent.parent.vm.reload();
                        });
                    }else{
                        layer.alert(r.msg);
                    }
                }
            });
        },
        validator: function () {

            if(isBlank(vm.transactionEvt.borrowername)){
                alert("借款人姓名不能为空");
                return true;
            }


            if(isBlank(vm.transactionEvt.amount)){
                alert("借款金额不能为空");
                return true;
            }

            if(isBlank(vm.transactionEvt.rate) ){
                alert("年利化率不能为空");
                return true;
            }

        }
    }
});
