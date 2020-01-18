$(function () {
    vm.load();
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
        authUser: {},
        audit: {},
        request: {
		    status: {}
        },
        contacts: [],
        searchContacts: [],
        histories: []
	},
	methods: {
        load: function () {
            $.get(baseURL + "autha/request/info/"+vm.uuid, function(r){
                vm.request = r.request;
                vm.authUser = r.user;
                vm.count = r.count;
                vm.histories = r.histories;
                vm.ydEnabled = r.ydEnabled;

                if (vm.request.idUrl1 && !vm.request.idUrl1.startsWith("http")) {
                    vm.request.idUrl1 = baseURL + 'userimg/' + vm.request.idUrl1;
                }
                if (vm.request.idUrl2 && !vm.request.idUrl2.startsWith("http")) {
                    vm.request.idUrl2 = baseURL + 'userimg/' + vm.request.idUrl2;
                }
                if (vm.request.idUrl3 && !vm.request.idUrl3.startsWith("http")) {
                    vm.request.idUrl3 = baseURL + 'userimg/' + vm.request.idUrl3;
                }

                if (vm.authUser.idUrl1 && !vm.authUser.idUrl1.startsWith("http")) {
                    vm.authUser.idUrl1 = baseURL + 'userimg/' + vm.authUser.idUrl1;
                }
                if (vm.authUser.idUrl2 && !vm.authUser.idUrl2.startsWith("http")) {
                    vm.authUser.idUrl2 = baseURL + 'userimg/' + vm.authUser.idUrl2;
                }
                if (vm.authUser.idUrl3 && !vm.authUser.idUrl3.startsWith("http")) {
                    vm.authUser.idUrl3 = baseURL + 'userimg/' + vm.authUser.idUrl3;
                }

                if (vm.request.name) {
                    document.title = '申请单: ' + vm.request.name;
                } else {
                    document.title = '申请单: ' + vm.authUser.mobile;
                }

                vm.$nextTick(function () {
                    viewer = new Viewer(document.getElementById('idImages'));
                });

                $.get(baseURL + "autha/request/contact/"+r.request.userId, function(r){
                    newPage(r.contacts);
                    vm.contacts = r.contacts;
                    //作为临时存储通讯录内容的变量 以便搜索时使用
                    vm.searchContacts = r.contacts;
                });
            });
        },
        transactionRun: function () {
            layer.open({
                type: 2,
                skin: 'layui-layer-molv',
                title: '打借条',
                shadeClose: false,
                area: ['800px', '400px'],
                content: baseURL + 'modules/tx/request_transaction.html?userId=' + vm.authUser.userId,

                cancel: function (index) {
                    layer.close();
                }
            });
        },
        allocate: function () {
            vm.audit = {status: 1};
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "处理申请单",
                area: ['550px', '330px'],
                shadeClose: false,
                content: jQuery("#auditLayer"),
                btn: ['确定','取消'],
                btn1: function (index) {
                    if (!vm.audit.status) {
                        alert('请选择处理状态');
                        return;
                    }
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
                }
            });
        }
	}
});

var reportLoaded = false;
function loadReport() {
    var iframe = $('reportFrame');
    if (!reportLoaded) {
        window.open(baseURL + 'autha/report/tj/ds_all/' + vm.request.requestId + '?token=' + token, 'reportFrame');
        reportLoaded = true;
        iframe.css('height', '100%');
    }
}

var mobileReportLoaded = false;
function loadMobileReport() {
    var iframe = $('mobileFrame');
    if (!mobileReportLoaded) {
        // $.get(baseURL + "tj/report_url/mobile/"+vm.request.userId, function(r){
        //     if (r.code === 0) {
        //         window.open(r.url, 'mobileFrame');
        //         mobileReportLoaded = true;
        //         iframe.css('height', '100%');
        //     } else {
        //         alert(r.msg);
        //     }
        // });

        window.open(baseURL + 'autha/report/tj/mobile/' + vm.request.userId + '?token=' + token, 'mobileFrame');
        mobileReportLoaded = true;

    }
}
var loadReportLoaded = false;
function loadOtherReport() {
    var iframe = $('otherFrame');
    if (!loadReportLoaded) {
        window.open(baseURL + 'autha/report/yx/loan_all/' + vm.request.requestId + '?token=' + token, 'otherFrame');
        loadReportLoaded = true;
        iframe.css('height', '100%');
    }
}

var other2Loaded = false;
function loadOther2Report() {
    var iframe = $('other2Frame');
    if (!other2Loaded) {
        window.open(baseURL + 'autha/report/tj/other/' + vm.request.requestId + '?token=' + token, 'other2Frame');
        other2Loaded = true;
        iframe.css('height', '100%');
    }
}

var callLogLoaded = false;
function loadMobileCallLogs() {
    var iframe = $('callLogFrame');
    if (!callLogLoaded) {
        window.open(baseURL + 'autha/report/tj/callLog/' + vm.request.userId + '?token=' + token, 'callLogFrame');
        callLogLoaded = true;
        iframe.css('height', '100%');
    }
}
//综合报告
var comprehensiveLoaded = false;
function loadComprehensiveReport() {
    var iframe = $('comprehensiveFrame');
    if (!comprehensiveLoaded) {
        window.open(baseURL + 'autha/report/tj/comprehensive/' + vm.request.requestId + '?token=' + token, 'comprehensiveFrame');
        comprehensiveLoaded = true;
        iframe.css('height', '100%');
    }
}

function newPage(contacts) {
    var newItems = [];
    var pageTotal=0;
    var dataTotal=0;
    var pageAmount=30;
    var pageSize=5;
    if(contacts){
        dataTotal=contacts.length;
        pageTotal=Math.ceil(dataTotal/pageAmount);
        if(pageTotal<pageSize){
            pageSize=pageTotal;
        }
    }
    new Page({
        id: 'pagination',
        pageTotal: pageTotal, //必填,总页数
        pageAmount: pageAmount,  //每页多少条
        dataTotal: dataTotal, //总共多少条数据
        curPage:1, //初始页码,不填默认为1
        pageSize: pageSize, //分页个数,不填默认为5
        showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
        showSkipInputFlag:true, //是否支持跳转,不填默认不显示
        getPage: function (page) {
            //获取当前页数
            console.log(page);
            function aa(){
                newItems=[];
                if(page*30-30<dataTotal){
                    newItems.push(contacts[page*30-30]);
                }
                if(page*30-29<dataTotal){
                    newItems.push(contacts[page*30-29]);
                }
                if(page*30-28<dataTotal){
                    newItems.push(contacts[page*30-28]);
                }
                if(page*30-27<dataTotal){
                    newItems.push(contacts[page*30-27]);
                }
                if(page*30-26<dataTotal){
                    newItems.push(contacts[page*30-26]);
                }
                if(page*30-25<dataTotal){
                    newItems.push(contacts[page*30-25]);
                }if(page*30-24<dataTotal){
                    newItems.push(contacts[page*30-24]);
                }
                if(page*30-23<dataTotal){
                    newItems.push(contacts[page*30-23]);
                }
                if(page*30-22<dataTotal){
                    newItems.push(contacts[page*30-22]);
                }
                if(page*30-21<dataTotal){
                    newItems.push(contacts[page*30-21]);
                }if(page*30-20<dataTotal){
                    newItems.push(contacts[page*30-20]);
                }if(page*30-19<dataTotal){
                    newItems.push(contacts[page*30-19]);
                }
                if(page*30-18<dataTotal){
                    newItems.push(contacts[page*30-18]);
                }
                if(page*30-17<dataTotal){
                    newItems.push(contacts[page*30-17]);
                }
                if(page*30-16<dataTotal){
                    newItems.push(contacts[page*30-16]);
                }
                if(page*30-15<dataTotal){
                    newItems.push(contacts[page*30-15]);
                }
                if(page*30-14<dataTotal){
                    newItems.push(contacts[page*30-14]);
                }
                if(page*30-13<dataTotal){
                    newItems.push(contacts[page*30-13]);
                }
                if(page*30-12<dataTotal){
                    newItems.push(contacts[page*30-12]);
                }
                if(page*30-11<dataTotal){
                    newItems.push(contacts[page*30-11]);
                }
                if(page*30-10<dataTotal){
                    newItems.push(contacts[page*30-10]);
                }
                if(page*30-9<dataTotal){
                    newItems.push(contacts[page*30-9]);
                }
                if(page*30-8<dataTotal){
                    newItems.push(contacts[page*30-8]);
                }
                if(page*30-7<dataTotal){
                    newItems.push(contacts[page*30-7]);
                }
                if(page*30-6<dataTotal){
                    newItems.push(contacts[page*30-6]);
                }
                if(page*30-5<dataTotal){
                    newItems.push(contacts[page*30-5]);
                }
                if(page*30-4<dataTotal){
                    newItems.push(contacts[page*30-4]);
                }
                if(page*30-3<dataTotal){
                    newItems.push(contacts[page*30-3]);
                }
                if(page*30-2<dataTotal){
                    newItems.push(contacts[page*30-2]);
                }
                if(page*30-1<dataTotal) {
                    newItems.push(contacts[page * 30 - 1]);
                }
                return newItems;
            };
            vm.contacts = aa();
        }
    })
}

var personReportLoaded = false;
function loadPersonReport() {
    if (!vm.ydEnabled) {
        alert('余额不足，请尽快充值!');
        return;
    }

    if (!vm.authUser.authStatus) {
        alert('此用户未有多头数据，可提醒用户重新提交身份证信息进行认证!')
        return;
    }

    var iframe = $('personReportFrame');
    if (!personReportLoaded) {
        if (vm.authUser.authReportUrl) {

            var reportUrl = vm.authUser.authReportUrl;
            if (window.location.href.startsWith("https://") && reportUrl.startsWith("http://")) {
                reportUrl = reportUrl.replace('http://pangang.51baoli.com/esurfingAttachment', 'https://md.cuisb.cn/yd_report');
            }
            window.open(reportUrl, 'personReportFrame');

            personReportLoaded = true;
            iframe.css('height', '100%');
        } else {
            alert('还没有生成多头报告!');
        }
    }
}
function serch() {
    var serchStr=document.getElementById('serch').value;
    var contacts=vm.searchContacts;
    var newContacts=[];
    if (serchStr) {//代表搜索框中有搜索内容
        if(contacts!=null){
            for(var i = 0;i<contacts.length;i++) {
                if(contacts[i].name.search(serchStr)!= -1){//代表姓名包含搜索框中的值
                    newContacts.push(contacts[i]);
                }else if(contacts[i].mobile.search(serchStr)!= -1){//代表手机号包含搜索框中的值
                    newContacts.push(contacts[i]);
                }
            }
            newPage(newContacts);
            vm.contacts=newContacts;
        }
    }else{
        newPage(contacts);
        vm.contacts=contacts;
    }

}