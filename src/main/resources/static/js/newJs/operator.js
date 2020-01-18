var callamount = echarts.init(document.getElementById('callamount'));
option01 = {
	color: ['rgba(32, 142, 255, 0.6)', 'rgb(255, 127, 61,0.9)', 'rgba(255, 142, 17, 0.6)'],
    title: {
        text: '通话号码数量',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c}条 ({d}%)"
    },
    series : [
        {
            name:'通话号码数量',
            type:'pie',
            radius : '60%',
            center: ['50%', '60%'],
            data:[
                {value:2, name:'银 行'},
                {value:3, name:'快递送餐'},
                {value:4, name:'网络标识'}
            ].sort(function (a, b) { return a.value - b.value}),
            roseType: 'angle',
            label: {
                normal: {
                    textStyle: {
                        color: '#666'
                    }
                }
            },
            labelLine: {
                normal: {
                    lineStyle: {
                        color: '#666'
                    },
                    smooth: 0.8,
                    length: 10,
                    length2: 20
                }
            }
        }
    ]
};
callamount.setOption(option01);

//------------------------------------

var callduration = echarts.init(document.getElementById('callduration'));
option02 = {
	color: ['rgba(32, 142, 255, 0.6)', 'rgb(255, 127, 61,0.9)', 'rgba(255, 142, 17, 0.6)'],
    title: {
        text: '通话时长',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c}分钟 ({d}%)"
    },
    series : [
        {
            name:'通话时长',
            type:'pie',
            radius : '60%',
            center: ['50%', '60%'],
            data:[
                {value:3.62, name:'银 行'},
                {value:3.4, name:'快递送餐'},
                {value:2.00, name:'网络标识'}
            ].sort(function (a, b) { return a.value - b.value}),
            roseType: 'angle',
            label: {
                normal: {
                    textStyle: {
                        color: '#666'
                    }
                }
            },
            labelLine: {
                normal: {
                    lineStyle: {
                        color: '#666'
                    },
                    smooth: 0.8,
                    length: 10,
                    length2: 20
                }
            }
        }
    ]
};
callduration.setOption(option02);
//------------------------------------

var districtAmount = echarts.init(document.getElementById('districtAmount'));
option03 = {
	color: ['rgba(255, 142, 17, 0.9)'],
    title: {
    	text: '联系人地区TOP10数量分布',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }        
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['最高数量']
    },
    grid: {
        left: '0',
        right: '50',
        bottom: '0',
        containLabel: true
    },
    xAxis:  {
        type: 'category',
        boundaryGap: false,
        data: ['湖 南','广 东','全 国','浙 江','北 京','辽 宁','上 海','贵 州','江 西','湖 北']
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}'
        }
    },
    series: [
        {
            name:'月度解决率',
            type:'line',
            data:[46, 30, 20, 17, 4, 2, 2, 2, 2, 1],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ]
};
districtAmount.setOption(option03);

//------------------------------------

var districtDuration = echarts.init(document.getElementById('districtDuration'));
option04  = {
	color: ['rgba(32, 142, 255, 0.6)', 'rgba(255, 142, 17, 0.6)', 'rgb(0, 102, 226, 0.9)', 'rgb(255, 127, 61,0.9)'],
	title: {
        text: '联系人地区TOP10通话时长分布',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }
    },	
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data:['主叫次数','被叫次数','主叫时长','被叫时长']
    },
    grid: {
        left: '0',
        right: '50',
        bottom: '0',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            data : ['湖 南','广 东','全 国','浙 江','北 京','辽 宁','上 海','贵 州','江 西','湖 北']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'主叫次数',
            type:'bar',
            stack: '次数',
            data:[17, 24, 12, 2, 0, 0, 0, 0, 1, 0]
        },
        {
            name:'被叫次数',
            type:'bar',
            stack: '次数',
            data:[56, 70, 14, 17, 5, 2, 2, 2, 12, 1]
        },
        {
            name:'主叫时长',
            type:'bar',
            stack: '时长',
            data:[39.92, 90.57, 27.77, 3.75, 0, 0, 0, 0, 7.4, 0]
        },
        {
            name:'被叫时长',
            type:'bar',
            stack: '时长',
            data:[97.82, 318.58, 11.43, 5.73, 2.75, 0.25, 1.40, 2.40, 114.20, 0.17]
        } 
    ]
};
districtDuration.setOption(option04);

//------------------------------------

var expenses = echarts.init(document.getElementById('expenses'));
option05 = {
	color: ['rgb(0, 102, 226, 0.9)'],
    title: {
    	text: '消费金额情况',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }        
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['最高消费']
    },
    grid: {
        left: '0',
        right: '50',
        bottom: '0',
        containLabel: true
    },
    xAxis:  {
        type: 'category',
        boundaryGap: false,
        data: ['2018-12','2018-11','2018-10','2018-09','2018-08','2018-07']
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}'
        }
    },
    series: [
        {
            name:'月度解决率',
            type:'line',
            data:[58, 85.5, 83, 83.5, 155, 83.4],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ]
};
expenses.setOption(option05);

//------------------------------------

var duration = echarts.init(document.getElementById('duration'));
option06  = {
	color: ['rgb(0, 102, 226, 0.9)', 'rgb(255, 127, 61,0.9)'],
	title: {
    	text: '各月通话时长',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }        
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data: ['主叫时长', '被叫时长']
    },
    grid: {
        left: '0',
        right: '50',
        bottom: '0',
        containLabel: true
    },
    xAxis:  {
        type: 'value'
    },
    yAxis: {
        type: 'category',
        data: ['2018-12','2018-11','2018-10','2018-09','2018-08','2018-07']
    },
    series: [
        {
            name: '主叫时长',
            type: 'bar',
            stack: '总时长',
            label: {
                normal: { 
                    position: 'insideRight'
                }
            },
            data:[4.77, 56.45, 18.95, 30.62, 28.12, 30.50]
        },
        {
            name: '被叫时长',
            type: 'bar',
            stack: '总时长',
            label: {
                normal: { 
                    position: 'insideRight'
                }
            },
            data:[51.27, 43.28, 214.97, 89.48, 65.02, 91.43]
        }
    ]
};
duration.setOption(option06);

//------------------------------------

var regionalTime = echarts.init(document.getElementById('regionalTime'));
option07  = {
	color: ['rgb(0, 102, 226, 0.9)', 'rgb(255, 127, 61,0.9)'],
	title: {
    	text: '通话地区TOP10通话时长分布情况',
        left: 'center',
        top: 20,
        textStyle: {
            color: '#666'
        }        
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data: ['主叫时长', '被叫时长']
    },
    grid: {
        left: '0',
        right: '50',
        bottom: '0',
        containLabel: true
    },
    xAxis:  {
        type: 'value'
    },
    yAxis: {
        type: 'category',
        data: ['长 沙','益 阳','怀 化']
    },
    series: [
        {
            name: '主叫时长',
            type: 'bar',
            stack: '总时长',
            label: {
                normal: { 
                    position: 'insideRight'
                }
            },
            data:[4.77, 56.45, 18.95]
        },
        {
            name: '被叫时长',
            type: 'bar',
            stack: '总时长',
            label: {
                normal: { 
                    position: 'insideRight'
                }
            },
            data:[51.27, 43.28, 214.97]
        }
    ]
};
regionalTime.setOption(option07);