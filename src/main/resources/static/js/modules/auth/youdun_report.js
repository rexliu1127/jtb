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
        youduInfoVO:{},
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
            $.get(baseURL + 'autha/report/yd/youdunReport/' +vm.uuid, function(r){
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
                vm.youduInfoVO = r.youduInfoVO;
                vm.txlenderList = r.txlenderList;

                var score  = r.youduInfoVO.score;
                var apptime = r.youduInfoVO.lastModifiedTime;
                var riskEvaluation = r.youduInfoVO.riskEvaluation;

                Meter.setOptions({
                    element: 'creditValues',
                    centerPoint: {
                        x: 150,
                        y: 150
                    },
                    radius: 150,
                    data: {
                        value: score,
                        title: riskEvaluation,
                        subTitle: '评估时间：'+apptime,
                        area: [{
                            min: 350, max: 550, text: '较差'
                        },{
                            min: 550, max: 600, text: '中等'
                        },{
                            min: 600, max: 650, text: '良好'
                        },{
                            min: 650, max: 700, text: '优秀'
                        },{
                            min: 700, max: 950, text: '极好'
                        }]
                    }
                }).init();

                var allLoanInfo = r.youduInfoVO.allLoanInfo;
                var actualCountInfo = r.youduInfoVO.actualCountInfo;
                var repaymentCcountInfo =  r.youduInfoVO.repaymentCcountInfo;
                var behavior = echarts.init(document.getElementById('behavior'));

                var allLoanInfoList = [];
                var actualCountInfoList = [];
                var repaymentCcountInfoList = [];
                for(var  i=0;i<allLoanInfo.length;i++)
                {
                    allLoanInfoList.push(allLoanInfo[i]);
                }
                for(var  i=0;i<actualCountInfo.length;i++)
                {
                    actualCountInfoList.push(actualCountInfo[i]);
                }
                for(var  i=0;i<repaymentCcountInfo.length;i++)
                {
                    repaymentCcountInfoList.push(repaymentCcountInfo[i]);
                }
                option = {
                    color: ['rgba(32, 142, 255, 0.6)', 'rgba(0, 70, 184, 0.6)', 'rgba(255, 142, 17, 0.6)'],
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data:['申请借款','借款','还款']
                    },
                    grid: {
                        left: '0%',
                        right: '0%',
                        bottom: '0%',
                        containLabel: true
                    },
                    xAxis : [
                        {
                            type : 'category',
                            data : ['总计','近6月','近3月','近1月'],

                        }
                    ],
                    yAxis : [
                        {
                            type : 'value',
                            splitLine: {
                                show: false
                            }
                        }
                    ],
                    series : [
                        {
                            name:'申请借款',
                            type:'bar',
                            data:allLoanInfoList
                        },
                        {
                            name:'借款',
                            type:'bar',
                            data:actualCountInfoList
                        },
                        {
                            name:'还款',
                            type:'bar',
                            data:repaymentCcountInfoList
                        }
                    ]
                };
                behavior.setOption(option);

                var graphDate = [];
                var linksData = [];
                var graphPe = new Object();
                graphPe.category = 0;
                graphPe.name = '用户';
                graphDate.push(graphPe);


                var deviceCount = r.youduInfoVO.linkDeviceCount;  //设备数
                for(var  a=0;a<deviceCount;a++)
                {
                    var numDeviceCount = a +1 ;
                    var nameDeviceCount = '设备'+numDeviceCount;
                    var graphVODeviceCount = new Object();
                    graphVODeviceCount.category = 0;
                    graphVODeviceCount.name = nameDeviceCount;
                    graphDate.push(graphVODeviceCount);

                    var linkVoDeviceCount = new Object();
                    linkVoDeviceCount.source = nameDeviceCount;
                    linkVoDeviceCount.target='用户';
                    linksData.push(linkVoDeviceCount)
                }

                var bankCount = r.youduInfoVO.userHaveBankcardCount;  //银行卡
                for(var  b=0;b<bankCount;b++)
                {
                    var numbankCount = b+1 ;
                    var namebankCount = '银行卡'+numbankCount;
                    var graphVObankCount = new Object();
                    graphVObankCount.category = 0;
                    graphVObankCount.name = namebankCount;
                    graphDate.push(graphVObankCount);

                    var linkVographVObankCount = new Object();
                    linkVographVObankCount.source = namebankCount;
                    linkVographVObankCount.target='用户';
                    linksData.push(linkVographVObankCount)
                }

                var mobileCount = r.youduInfoVO.mobileCount;  //手机号码
                for(var  c=0;i<mobileCount;c++)
                {
                    var nummobileCount = c+1 ;
                    var namemobileCount = '手机号'+nummobileCount;
                    var graphVOmobileCount = new Object();
                    graphVOmobileCount.category = 0;
                    graphVOmobileCount.name = namemobileCount;
                    graphDate.push(graphVOmobileCount);

                    var linkVomobileCount = new Object();
                    linkVomobileCount.source = namemobileCount;
                    linkVomobileCount.target='用户';
                    linksData.push(linkVomobileCount)
                }

                //--------------关联图谱
                var linkedView = echarts.init(document.getElementById('linkedView'));
                var option01 ={

                    "legend": {
                        "x": "left",
                        "data": ["正常用户", "商户黑名单", "有盾&商户黑名单", "有盾黑名单"]
                    },
                    "series": [{
                        "type": "graph",
                        "layout": "force",
                        "ribbonType": false,
                        "categories": [{
                            //授信状态设置
                            "name": "用 户",itemStyle: {normal: {color: "#0475ff",}}},
                            {
                                "name": "正常用户",itemStyle: {normal: {color: "#6cc788",}}},
                            {
                                "name": "商户黑名单",itemStyle: {normal: {color: "#F00",}}},
                            {
                                "name": "有盾&商户黑名单",itemStyle: {normal: {color: "#5c5c5c",}}},
                            {
                                "name": "有盾黑名单",itemStyle: {normal: {color: "#ff8e11",}}}
                        ],
                        "force": {
                            "repulsion": 150,
                            "gravity": 0.0001,
                            "edgeLength": [100, 100],
                            "layoutAnimation": true
                        },
                        "symbolSize": 70,
                        "itemStyle": {
                            "normal": {
                                "label": {
                                    "show": true,
                                    "textStyle": {
                                        "color": "#E0FFFF",
                                        "fontSize": 12
                                    }
                                },
                                "linkStyle": {
                                    "type": "curve"
                                }
                            }
                        },
                        "emphasis": {
                            "label": {
                                "show": true,
                                "textStyle": {
                                    "color": "#DDDDDD",
                                    "fontSize": 12
                                }
                            },
                            "linkStyle": {}
                        },
                        "useWorker": false,
                        "gravity": 1.1,
                        "scaling": 2.1,
                        "roam": "move",
                        "data": graphDate,
                        //设备关联给谁 target 指向“用户”  links每｛｝代表一条设备
                        "links":linksData
                    }]
                };
                linkedView.setOption(option01);

            });
        }
	}
});

