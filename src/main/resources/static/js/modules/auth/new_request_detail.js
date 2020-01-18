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
        authUser: {},
        youduInfoVO: {},
        audit: {},
        txlenderList: {},
        transactionEvt: {
            beginDate:null,
            repayDate :null,
            rate:24,
            usageType : 1
        },
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
                vm.youduInfoVO = r.youduInfoVO;
                vm.txlenderList = r.txlenderList;

                /*
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
                }*/


                if (vm.request.name) {
                    document.title = '申请单: ' + vm.request.name;
                } else {
                    document.title = '申请单: ' + vm.authUser.mobile;
                }

                vm.$nextTick(function () {
                    viewer = new Viewer(document.getElementById('idImages'));
                });
                baiduditu(r.request.longitude,r.request.latitude);
                /* $.get(baseURL + "autha/request/contact/"+r.request.userId, function(r){
                     newPage(r.contacts);
                     vm.contacts = r.contacts;
                     //作为临时存储通讯录内容的变量 以便搜索时使用
                     vm.searchContacts = r.contacts;
                 });*/
            });
        },
	}
});

function baiduditu(latitude,longitude){
    // 百度地图API功能
    var map = new BMap.Map("allmap");
    var point = new BMap.Point(latitude,longitude); //根据坐标定位
    var marker = new BMap.Marker(point);

    map.enableScrollWheelZoom(true);     //开启鼠标滚轮
    map.centerAndZoom(point, 19);
    map.addOverlay(marker);
    marker.addEventListener("click", function(){
        this.openInfoWindow(infoWindow);
        //图片加载完毕重绘infowindow
        document.getElementById('imgDemo').onload = function (){
            infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
        }
    });
    //获取坐标 http://api.map.baidu.com/lbsapi/getpoint/index.html
}

