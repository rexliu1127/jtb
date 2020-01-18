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
        audit: {},
        txlenderList: {},
        transactionEvt: {
            beginDate:null,
            repayDate :null,
            rate:24,
            usageType : 1
        },
        errorMsg:{},
        authTjMobileVO:{
            financialBlacklistByIdCard:{},
            financialBlacklistByPhone:{},
            checkSearchInfo:{},
            checkBlackInfo:{},
            contactsDistribution:{},
            silentDays:{},
            nightActivities:{},
        },
        request: {
            status: {}
        }
	},
	methods: {
        load: function () {
            $.get(baseURL + "autha/report/newTjJson/"+vm.type+"/"+vm.uuid, function(r){
                if(r.code!=0){

                    $("#nullHtml").show();
                    $("#bodyHtml").hide();
                    vm.errorMsg=r.msg;
                    $("#directory-nav").hide();
                    return;
                    //alert(r.msg);
                }else{
                    $("#bodyIframe").attr("src",r.url);

                    return ;
                    DirectoryNav.prototype = {
                        init:function(){
                            this.make();
                            this.setArr();
                            this.bindEvent();
                        },
                        make:function(){
                            //生成导航目录结构,这是根据需求自己生成的。如果你直接在页面中输出一个结构那也挺好不用 搞js
                            $("body").append('<div class="directory-nav" id="directoryNav"><ul></ul><span class="cur-tag"></span><span class="c-top"></span><span class="c-bottom"></span><span class="line"></span></div>');
                            var $hs = this.$h,
                                $directoryNav = $("#directoryNav"),
                                temp = [],
                                index1 = 0,
                                index2 = 0;
                            $hs.each(function(index){
                                var $this = $(this),
                                    text = $this.text();
                                idname = $this.attr("id")
                                if(this.tagName.toLowerCase()=='h5'){
                                    index1++;
                                    if(index1%2==0) index2 = 0;
                                    temp.push('<li class="l1"><span class="c-dot"></span>'+index1+'. <a class="l1-text"  href="#'+idname+'">'+text+'</a></li>');
                                }else{
                                    index2++;
                                    temp.push('<li class="l2">'+index1+'.'+index2+' <a class="l2-text">'+text+'</a></li>');

                                }
                            });
                            $directoryNav.find("ul").html(temp.join(""));

                            //设置变量
                            this.$pageNavList = $directoryNav;
                            this.$pageNavListLis = this.$pageNavList.find("li");
                            this.$curTag = this.$pageNavList.find(".cur-tag");
                            this.$pageNavListLiH = this.$pageNavListLis.eq(0).height();

                            if(!this.opts.scrollTopBorder){
                                this.$pageNavList.show();
                            }
                        },
                        setArr:function(){
                            var This = this;
                            this.$h.each(function(){
                                var $this = $(this),
                                    offT = Math.round($this.offset().top);
                                This.offArr.push(offT);
                            });
                        },
                        posTag:function(top){
                            this.$curTag.css({top:top+'px'});
                        },
                        ifPos:function(st){
                            var offArr = this.offArr;
                            //console.log(st);
                            var windowHeight = Math.round(this.$win.height() * this.opts.scrollThreshold);
                            for(var i=0;i<offArr.length;i++){
                                if((offArr[i] - windowHeight) < st) {
                                    var $curLi = this.$pageNavListLis.eq(i),
                                        tagTop = $curLi.position().top;
                                    $curLi.addClass("cur").siblings("li").removeClass("cur");
                                    this.curIndex = i;
                                    this.posTag(tagTop+this.$pageNavListLiH*0.5);
                                    //this.curIndex = this.$pageNavListLis.filter(".cur").index();
                                    this.opts.scrollChange.call(this);
                                }
                            }
                        },
                        bindEvent:function(){
                            var This = this,
                                show = false,
                                timer = 0;
                            this.$win.on("scroll",function(){
                                var $this = $(this);
                                clearTimeout(timer);
                                timer = setTimeout(function(){
                                    This.scrollIng = true;
                                    if($this.scrollTop()>This.opts.scrollTopBorder){
                                        if(!This.$pageNavListLiH) This.$pageNavListLiH = This.$pageNavListLis.eq(0).height();
                                        if(!show){
                                            This.$pageNavList.fadeIn();
                                            show = true;
                                        }
                                        This.ifPos( $(this).scrollTop() );
                                    }else{
                                        if(show){
                                            This.$pageNavList.fadeOut();
                                            show = false;
                                        }
                                    }
                                },This.opts.delayDetection);
                            });

                            this.$pageNavList.on("click","li",function(){
                                var $this = $(this),
                                    index = $this.index();
                                This.scrollTo(This.offArr[index]);
                            })
                        },
                        scrollTo: function(offset,callback) {
                            var This = this;
                            $('html,body').animate({
                                scrollTop: offset
                            }, this.opts.scrollSpeed, this.opts.easing, function(){
                                This.scrollIng = false;
                                //修正弹两次回调 蛋疼
                                callback && this.tagName.toLowerCase()=='body' && callback();
                            });
                        }
                    };
                    //实例化
                    var directoryNav = new DirectoryNav($("h5,h6"),{
                        scrollTopBorder:0
                        //滚动条距离顶部多少的时候显示导航，如果为0，则一直显示
                    });
                    $("#bodyHtml").show();
                }
                //vm.authMnoBqsReportVO = r.authMnoBqsReportVO;
                vm.request = r.request;
                vm.count = r.count;
                vm.authTjMobileVO = r.authTjMobileVO;
                vm.txlenderList = r.txlenderList;
                setOption01(r.authTjMobileVO.specialCateEchart);
                setOption02(r.authTjMobileVO.specialCateEchart);
                setOption03(r.authTjMobileVO.contactAreaAnalysisEchartTop10);
                setOption04(r.authTjMobileVO.contactAreaAnalysisEchartTop10);
                setOption05(r.authTjMobileVO.monthlyConsumption);
                setOption06(r.authTjMobileVO.monthlyConsumption);
                setOption07(r.authTjMobileVO.telAreaAnalysisTop10);
            });
        }
	}
});

//通话号码数量
function setOption01(specialCateEchart){
    var callamount = echarts.init(document.getElementById('callamount'));
    var obj=[];
    for(var i=0;i<specialCateEchart.length;i++){
        obj.push({value: specialCateEchart[i].talkCnt, name: specialCateEchart[i].cate});
    }

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
                data:obj.sort(function (a, b) { return a.value - b.value}),
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
}
//通话时长
function setOption02(specialCateEchart){
    var callduration = echarts.init(document.getElementById('callduration'));
    var obj=[];
    for(var i=0;i<specialCateEchart.length;i++){
        obj.push({value: specialCateEchart[i].talkSeconds, name: specialCateEchart[i].cate});
    }
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
                data:obj.sort(function (a, b) { return a.value - b.value}),
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
}

//联系人地区top10数量分布
function setOption03(contactAreaAnalysisEchartTop10){
    var districtAmount = echarts.init(document.getElementById('districtAmount'));
    var area=[];
    var areaJson=[];
    for(var i=0;i<contactAreaAnalysisEchartTop10.length;i++){
        area.push(contactAreaAnalysisEchartTop10[i].area);
        areaJson.push({value: contactAreaAnalysisEchartTop10[i].contactPhoneCnt, name: contactAreaAnalysisEchartTop10[i].area});
    }
    option03 = {
        title : {
            text: '联系人地区TOP10数量分布',
            x:'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },

        series: [
            {
                name:'地区占据',
                type:'pie',
                radius: ['40%', '55%'],
                label: {

                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                data:areaJson
            }
        ]
    };
    /*option03 = {
        color: ['rgb(255,255,175)','rgb(149,196,113)','rgb(78,139,183)','rgb(145, 199, 174)','rgb(212, 130, 101)','rgb(97, 160, 168)','rgb(47, 69, 84)','rgb(194, 53, 49)','rgb(78,139,183)','rgb(89, 178, 224)'],
        title: {
            text: '联系人地区TOP10数量分布',
            left: 'center',
            top: 20,
            textStyle: {
                color: '#666'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            data:area
        },
        grid: {
            left: '0',
            right: '50',
            bottom: '0',
            containLabel: true
        },
        series: [
            {
                name:'访问来源',
                type:'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data:areaJson
            },

        ]
    };*/
    districtAmount.setOption(option03);
}
//联系人地区top10通话时长分布
function setOption04(contactAreaAnalysisEchartTop10){
    var districtDuration = echarts.init(document.getElementById('districtDuration'));
    var area=[];
    var call_cnt=[];
    var called_cnt=[];
    var call_seconds=[];
    var called_seconds=[];
    for(var i=0;i<contactAreaAnalysisEchartTop10.length;i++){
        area.push(contactAreaAnalysisEchartTop10[i].area);
        call_cnt.push(contactAreaAnalysisEchartTop10[i].callCnt);
        called_cnt.push(contactAreaAnalysisEchartTop10[i].calledCnt);
        call_seconds.push(contactAreaAnalysisEchartTop10[i].callSeconds);
        called_seconds.push(contactAreaAnalysisEchartTop10[i].calledSeconds);
    }
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
                data : area
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
                data:call_cnt
            },
            {
                name:'被叫次数',
                type:'bar',
                stack: '次数',
                data:called_cnt
            },
            {
                name:'主叫时长',
                type:'bar',
                stack: '时长',
                data:call_seconds
            },
            {
                name:'被叫时长',
                type:'bar',
                stack: '时长',
                data:called_seconds
            }
        ]
    };
    districtDuration.setOption(option04);
}
//消费金额情况
function setOption05(monthlyConsumption){
    var expenses = echarts.init(document.getElementById('expenses'));
    var month=[];
    var callConsumption=[];
    for(var i=0;i<monthlyConsumption.length;i++){
        month.push(monthlyConsumption[i].month);
        callConsumption.push(monthlyConsumption[i].callConsumption);
    }

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
            data: month
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
                data:callConsumption,
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
}

//各月通话时长
function setOption06(monthlyConsumption){
    var duration = echarts.init(document.getElementById('duration'));
    var month=[];
    var callSeconds=[];
    var calledSeconds=[];
    for(var i=0;i<monthlyConsumption.length;i++){
        month.push(monthlyConsumption[i].month);
        callSeconds.push(monthlyConsumption[i].callSeconds);
        calledSeconds.push(monthlyConsumption[i].calledSeconds);
    }

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
            data: month
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
                data:callSeconds
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
                data:calledSeconds
            }
        ]
    };
    duration.setOption(option06);
}

//通话地区Top10通话时长分布情况
function setOption07(telAreaAnalysisTop10){
    var regionalTime = echarts.init(document.getElementById('regionalTime'));
    var area=[];
    var callSeconds=[];
    var calledSeconds=[];
    for(var i=0;i<telAreaAnalysisTop10.length;i++){
        area.push(telAreaAnalysisTop10[i].area);
        callSeconds.push(telAreaAnalysisTop10[i].callSeconds);
        calledSeconds.push(telAreaAnalysisTop10[i].calledSeconds);
    }

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
            data: area
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
                data:callSeconds
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
                data:calledSeconds
            }
        ]
    };
    regionalTime.setOption(option07);
}
/*
       * 仿百度百科右侧导航代码 - 页面目录结构导航 v0.01
       * 只写了两级，无限级别也可以，是逻辑上的级别，html结构全是同一级别
       * 滑标动画用的css3过渡动画，不支持的浏览器就没动画效果了
       * 和百度百科比起来还是比较弱，没有实现右边也可以滚动的功能
       */
function DirectoryNav($h,config){
    this.opts = $.extend(true,{
        scrollThreshold:0.5,    //滚动检测阀值 0.5在浏览器窗口中间部位
        scrollSpeed:700,        //滚动到指定位置的动画时间
        scrollTopBorder:0,    //滚动条距离顶部多少的时候显示导航，如果为0，则一直显示
        easing: 'swing',        //不解释
        delayDetection:200,     //延时检测，避免滚动的时候检测过于频繁
        scrollChange:function(){}
    },config);
    this.$win = $(window);
    this.$h = $h;
    this.$pageNavList = "";
    this.$pageNavListLis ="";
    this.$curTag = "";
    this.$pageNavListLiH = "";
    this.offArr = [];
    this.curIndex = 0;
    this.scrollIng = false;
    this.init();
}