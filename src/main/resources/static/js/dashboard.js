$(function () {
    $("#repayListGrid").jqGrid({
        url: baseURL + 'tx/txbase/list',
        postData: {'status': 2,
                    'repayStartDate': moment().format('YYYY-MM-DD'),
                    'repayEndDate': moment().format('YYYY-MM-DD')},
                datatype: "json",
                colModel: [
        			{ name: 'txId', index: "tx_id", key: true, hidden: true },
                    { label: '出借人', name: 'lenderName', width: 75, formatter: function(value, options, row){
                        return value + '<br />' + row.lenderMobile + '<br />' + row.lenderIdNo;
                    }},
        			{ label: '借款人', name: 'borrowerName', width: 75 , formatter: function(value, options, row){
                        return value + '<br />' + row.borrowerMobile + '<br />' + row.borrowerIdNo;
                    }},
                    { label: '借款金额(元)', name: 'amount', align: 'center', width: 50 }
                ],
        		viewrecords: true,
        		autowidth: true,
                height: 'auto',
                rowNum: 10,
        		rowList : [10,30,50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth:true,
                multiselect: true,
                pager: "#repayListGridPager",
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
                	$("#repayListGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
    });


    $("#overdueListGrid").jqGrid({
        url: baseURL + 'tx/txbase/list',
        postData: {'status': 4,
                    'repayStartDate': moment().subtract(1, 'days').format('YYYY-MM-DD'),
                    'repayEndDate': moment().subtract(1, 'days').format('YYYY-MM-DD')},
                datatype: "json",
                colModel: [
        			{ name: 'txId', index: "tx_id", key: true, hidden: true },
        			{ label: '借款人', name: 'borrowerName', align: 'center', width: 75},
        			{ label: '手机', name: 'borrowerMobile', align: 'center', width: 75},
                    { label: '借款金额(元)', name: 'amount', align: 'center', width: 50 }
                ],
        		viewrecords: true,
        		autowidth: true,
                height: 'auto',
                rowNum: 10,
        		rowList : [10,30,50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth:true,
                multiselect: true,
                pager: "#overdueListGridPager",
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
                	$("#overdueListGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
    });

    vm.init();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
	    todayAuthSum: {},
	    todayTxSum: {},
	    historyAuthSum: {},
	    historyTxSum: {},
		q: {name: null}
	},
	methods: {
		init: function () {
			$.get(baseURL + "tx/txbase/sum/today", function(r){
			    if (r.sum) {
			        vm.todayTxSum = r.sum;
			    }
            });
			$.get(baseURL + "tx/txbase/sum/history", function(r){
			    if (r.sum) {
			        vm.historyTxSum = r.sum;
			    }

            });
			$.get(baseURL + "autha/request/sum/today", function(r){
			    if (r.sum) {
                    vm.todayAuthSum = r.sum;
                }
            });
			$.get(baseURL + "autha/request/sum/history", function(r){
			    if (r.sum) {
                    vm.historyAuthSum = r.sum;
                }
            });

            vm.initChart();
		},
		initChart: function() {
            var recycling = echarts.init(document.getElementById('recycling'));
            var app = {};
            option = null;
            option = {
                color: ['#ed7d31', '#4472c4'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data: ['放款笔数统计', '回款笔数统计']

                },
                grid: {
                    top: '60px',
                    left: '3%',
                    right: '4%',
                    bottom: '11%',
                    containLabel: true
                },
                calculable: true,
                xAxis: [{
                    type: 'category',
                    data: []
                }],
                yAxis: [{
                    type: 'value',
                    nameLocation: 'middle',
                    nameGap: 30,
                    nameTextStyle: {
                        fontWeight: 'bold',
                        fontSize: '14'
                    }
                }],
                dataZoom: [{
                    textStyle: {
                        color: '#8392A5'
                    },
                    handleSize: '80%',
                    dataBackground: {
                        areaStyle: {
                            color: '#8392A5'
                        },
                        lineStyle: {
                            opacity: 0.8,
                            color: '#8392A5'
                        }
                    },
                    handleStyle: {
                        color: '#fff',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    }
                }, {
                    type: 'inside'
                }],
                series: [{
                    name: '放款笔数统计',
                    type: 'bar',
                    data: [],
                    markPoint: {
                        data: [{
                            type: 'max',
                            name: '最大值'
                        }, {
                            type: 'min',
                            name: '最小值'
                        }]
                    }
                }, {
                    name: '回款笔数统计',
                    type: 'bar',
                    data: [],
                    markPoint: {
                        data: [{
                            type: 'max',
                            name: '最大值'
                        }, {
                            type: 'min',
                            name: '最小值'
                        }]
                    }
                }]
            };

            if (option && typeof option === "object") {
                recycling.setOption(option, true);
            }

            window.addEventListener('resize', function() {
                recycling.resize();
            });

            $.get(baseURL + "tx/txbase/sum/month", function(r){
            			    var periodArr = new Array();
            			    var borrowedAmountArr = new Array();
            			    var repaidAmountArr = new Array();
                            for (var i = 0; i < r.sum.length; i++) {
                                var month = r.sum[i];
                                periodArr[i] = month.period;
                                borrowedAmountArr[i] = month.borrowedAmount;
                                repaidAmountArr[i] = month.repaidAmount;
                            }

                recycling.setOption({
                                    xAxis: {
                                        data: ['2019/7/1', '2019/7/2','2019/7/3', '2019/7/4','2019/7/5', '2019/7/6','2019/7/7', '2019/7/8','2019/7/9', '2019/7/10','2019/7/11', '2019/7/12','2019/7/13', '2019/7/14','2019/7/15','2019/7/16','2019/7/17', '2019/7/18', '2019/7/19', '2019/7/20', '2019/7/21', '2019/7/22', '2019/7/23', '2019/7/24', '2019/7/25','2019/7/26', '2019/7/27', '2019/7/28', '2019/7/29', '2019/7/30', '2019/7/31']
                                    },
                                    series: [{
                                        // 根据名字对应到相应的系列
                                        name: '放款笔数统计',
                                        data: borrowedAmountArr
                                    },{
                                        name: '回款笔数统计',
                                        data: repaidAmountArr
                                    }]
                                });
                        });
		},
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

//---注册统计
// var registrations = echarts.init(document.getElementById('registrations'));
// option02 = {
//             color: ['#4297ff'],
//             tooltip: {
//                 trigger: 'axis',
//                 axisPointer: { // 坐标轴指示器，坐标轴触发有效
//                     type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
//                 }
//             },
//
//             grid: {
// 				top: '60px',
//                 left: '3%',
//                 right: '4%',
//                 bottom: '11%',
//                 containLabel: true
//             },
//             calculable: true,
//             xAxis: [{
//                 type: 'category',
//                 data: ['2016/11/8', '2016/11/9', '2016/11/10', '2016/11/11', '2016/11/12', '2016/11/13', '2016/11/14', '2016/11/15', '2016/11/16', '2016/11/17', '2016/11/18', '2016/11/19', '2016/11/20', '2016/11/21', '2016/11/22', '2016/11/23', '2016/11/24', '2016/11/25', '2016/11/26', '2016/11/27']
//             }],
//             yAxis: [{
//                 type: 'value',
//                 nameLocation: 'middle',
//                 nameGap: 30,
//                 nameTextStyle: {
//                     fontWeight: 'bold',
//                     fontSize: '14',
//                 }
//             }],
//             dataZoom: [{
//                 textStyle: {
//                     color: '#8392A5'
//                 },
//                 handleSize: '80%',
//                 dataBackground: {
//                     areaStyle: {
//                         color: '#8392A5'
//                     },
//                     lineStyle: {
//                         opacity: 0.8,
//                         color: '#8392A5'
//                     }
//                 },
//                 handleStyle: {
//                     color: '#fff',
//                     shadowBlur: 3,
//                     shadowColor: 'rgba(0, 0, 0, 0.6)',
//                     shadowOffsetX: 2,
//                     shadowOffsetY: 2
//                 }
//             }, {
//                 type: 'inside'
//             }],
//             series: [{
//                 name: '注册统计',
//                 type: 'bar',
//                 data: [35, 20, 26, 38, 19, 40, 26, 20, 26, 38, 19, 40, 26, 20, 26, 38, 19, 40, 26, 20],
//                 markPoint: {
//                     data: [{
//                         type: 'max',
//                         name: '最大值'
//                     }, {
//                         type: 'min',
//                         name: '最小值'
//                     }]
//                 }
//             }]
//         };
//
// registrations.setOption(option02);
//
//
// //---推广渠道
// var channel = echarts.init(document.getElementById('channel'));
// option03 = {
//     tooltip : {
//         trigger: 'axis'
//     },
//     legend: {
//         data:['推广渠道01','推广渠道02','推广渠道03','推广渠道04','推广渠道05']
//     },
// 	grid: {
// 			top: '60px',
// 			left: '3%',
// 			right: '4%',
// 			bottom: '11%',
// 			containLabel: true
// 	},
// 	 dataZoom: [{
//                 textStyle: {
//                     color: '#8392A5'
//                 },
//                 handleSize: '80%',
//                 dataBackground: {
//                     areaStyle: {
//                         color: '#8392A5'
//                     },
//                     lineStyle: {
//                         opacity: 0.8,
//                         color: '#8392A5'
//                     }
//                 },
//                 handleStyle: {
//                     color: '#fff',
//                     shadowBlur: 3,
//                     shadowColor: 'rgba(0, 0, 0, 0.6)',
//                     shadowOffsetX: 2,
//                     shadowOffsetY: 2
//                 }
//             }, {
//                 type: 'inside'
//             }],
//     calculable : true,
//     xAxis : [
//         {
//             type : 'category',
//             boundaryGap : false,
//             data: ['2016/11/8', '2016/11/9', '2016/11/10', '2016/11/11', '2016/11/12', '2016/11/13', '2016/11/14']
//         }
//     ],
//     yAxis : [
//         {
//             type : 'value'
//         }
//     ],
//     series : [
//         {
//             name:'推广渠道01',
//             type:'line',
//             stack: '总量',
//             data:[120, 132, 101, 134, 90, 230, 210]
//         },
//         {
//             name:'推广渠道02',
//             type:'line',
//             stack: '总量',
//             data:[220, 182, 191, 234, 290, 330, 310]
//         },
//         {
//             name:'推广渠道03',
//             type:'line',
//             stack: '总量',
//             data:[150, 232, 201, 154, 190, 330, 410]
//         },
//         {
//             name:'推广渠道04',
//             type:'line',
//             stack: '总量',
//             data:[320, 332, 301, 334, 390, 330, 320]
//         },
//         {
//             name:'推广渠道05',
//             type:'line',
//             stack: '总量',
//             data:[820, 932, 901, 934, 1290, 1330, 1320]
//         }
//     ]
// };
// channel.setOption(option03);

window.onresize = function(){
    // recycling.resize();
	// channel.resize();
};