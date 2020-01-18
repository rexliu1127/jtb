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
        userId: T.p('userId'),
        count: 1,
        ydEnabled: false,
        user: {},
        audit: {},
        request: {
		    status: {}
        },
        contacts: [],
        histories: []
	},
	methods: {
        load: function () {
            $.get(baseURL + "autha/user/info/"+vm.userId, function(r){
                vm.request = r.request;
                vm.user = r.user;
                vm.count = r.count;

                if (vm.request.idUrl1 && !vm.request.idUrl1.startsWith("http")) {
                    vm.request.idUrl1 = baseURL + 'userimg/' + vm.request.idUrl1;
                }
                if (vm.request.idUrl2 && !vm.request.idUrl2.startsWith("http")) {
                    vm.request.idUrl2 = baseURL + 'userimg/' + vm.request.idUrl2;
                }
                if (vm.request.idUrl3 && !vm.request.idUrl3.startsWith("http")) {
                    vm.request.idUrl3 = baseURL + 'userimg/' + vm.request.idUrl3;
                }

                if (vm.user.idUrl1 && !vm.user.idUrl1.startsWith("http")) {
                    vm.user.idUrl1 = baseURL + 'userimg/' + vm.user.idUrl1;
                }
                if (vm.user.idUrl2 && !vm.user.idUrl2.startsWith("http")) {
                    vm.user.idUrl2 = baseURL + 'userimg/' + vm.user.idUrl2;
                }
                if (vm.user.idUrl3 && !vm.authUser.idUrl3.startsWith("http")) {
                    vm.user.idUrl3 = baseURL + 'userimg/' + vm.user.idUrl3;
                }

                vm.$nextTick(function () {
                    viewer = new Viewer(document.getElementById('idImages'));
                });

                $.get(baseURL + "autha/request/contact/"+ vm.userId, function(r){
                    vm.contacts = r.contacts;
                });
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
                                    location.reload();
                                    parent.parent.vm.reload();
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
        window.open(baseURL + 'autha/report/tj/ds_all_user/' + vm.userId + '?token=' + token, 'reportFrame');
        reportLoaded = true;
        iframe.css('height', '100%');
    }
}

var mobileReportLoaded = false;
function loadMobileReport() {
    var iframe = $('mobileFrame');
    if (!mobileReportLoaded) {
        $.get(baseURL + "tj/report_url/mobile/"+vm.userId, function(r){
            if (r.code === 0) {
                window.open(r.url, 'mobileFrame');
                mobileReportLoaded = true;
                iframe.css('height', '100%');
            } else {
                alert(r.msg);
            }
        });

    }
}

var loadReportLoaded = false;
function loadOtherReport() {
    var iframe = $('otherFrame');
    if (!loadReportLoaded) {
        window.open(baseURL + 'autha/report/yx/loan_all_user/' + vm.userId + '?token=' + token, 'otherFrame');
        loadReportLoaded = true;
        iframe.css('height', '100%');
    }
}

var other2Loaded = false;
function loadOther2Report() {
    var iframe = $('other2Frame');
    if (!other2Loaded) {
        window.open(baseURL + 'autha/report/tj/other_user/' + vm.userId + '?token=' + token, 'other2Frame');
        other2Loaded = true;
        iframe.css('height', '100%');
    }
}