
//其他按钮
function commitBtn(){
    if (!vm.audit.status) {
        alert('请选择处理状态');
        return;
    };
    $.ajax({
        type: "POST",
        url: baseURL + "autha/request/status/" + vm.uuid,
        data: {status: vm.audit.status, userRemark: vm.audit.userRemark, adminRemark: vm.audit.adminRemark},
        dataType: "json",
        success: function(r){
            if(r.code == 0){
                layer.close(index);
                layer.alert('处理成功', function(){
                    if (parent.opener && parent.opener.vm) {
                        parent.opener.vm.reload();
                    }

                    location.reload();
                });
            }else{
                layer.alert(r.msg);
            }
        }
    });
    var index = $(this).index();
    $(".dialog").eq(index).remove();
};
//拒绝按钮
function refuseBtn(){
    $.ajax({
        type: "POST",
        url: baseURL + "autha/request/status/" + vm.uuid,
        data: {status: 3},
        dataType: "json",
        success: function(r){
            if(r.code == 0){
                layer.alert('处理成功', function(){
                    if (parent.opener && parent.opener.vm) {
                        parent.opener.vm.reload();
                    }

                    location.reload();
                });
            }else{
                layer.alert(r.msg);
            }
        }
    });
};
function savetransaction(){

    if(validator()){
        return ;
    }

    $.ajax({
        type: "POST",
        url: baseURL + "tx/txlender/transaction",
        data: {rowID: vm.transactionEvt.txlenderID,amount:vm.transactionEvt.amount,beginDate:vm.transactionEvt.beginDate,borrowerID:vm.authUser.userId,
            repayDate:vm.transactionEvt.repayDate,rate:vm.transactionEvt.rate,usageType:vm.transactionEvt.usageType},
        dataType: "json",
        success: function(r){
            if(r.code == 0){
                layer.close(index);
                layer.alert('打借条成功', function(){
                    if (parent.opener && parent.opener.vm) {
                        parent.opener.vm.reload();
                    }

                    location.reload();
                });
            }else{
                layer.alert(r.msg);
            }
        }
    });
    var index = $(this).index();
    $(".dialog").eq(index).remove();
};

function validator() {

    if(isBlank(vm.transactionEvt.txlenderID)){
        alert("出借人不能为空");
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

//申请单
function loadRequestDetails() {
    window.location.href=baseURL +'modules/auth/new_request_detail.html?id=' +vm.uuid;
}

//运营商报告
function loadBqsMnoReport() {
    window.location.href=baseURL +"modules/auth/mno_report.html?type=mobile&id="+vm.uuid;
}
//通话详情
function loadMobileDetails() {
    window.location.href=baseURL +'modules/auth/mobile_details.html?id=' +vm.uuid;
}
//综合报告
function loadComprehensiveDetails()
{
    window.location.href=baseURL +'modules/auth/midou_comprehensive.html?id=' +vm.uuid;
}

//有盾多头报告
function loadYouDunDetails()
{

    window.location.href=baseURL +'modules/auth/youdun_report.html?id=' +vm.uuid;
}

//米兜报告
function loadMidouDetails()
{
    window.location.href=baseURL +'modules/auth/midou_report.html?id=' +vm.uuid;
}
//loadJietiaoDetails 借条报告
function loadJietiaoDetails()
{
    window.location.href=baseURL +'modules/auth/jietiao_report.html?id=' +vm.uuid;
}

//电商报告
function loadTaobaoAlipayDetails()
{
    window.location.href=baseURL +'modules/auth/taobaoalipay_report.html?id=' +vm.uuid;
}

//通讯录
function  loadContactInfoDetails()
{
    window.location.href=baseURL +'modules/auth/contactInfo_report.html?id=' +vm.uuid;

}
//跟踪日志
function  loadHistoryDetails() {
    window.location.href=baseURL +'modules/auth/histories_report.html?id=' +vm.uuid;
}