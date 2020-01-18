$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'autha/user/list',
        postData: {'name': vm.q.name,
            'channelName': vm.q.channelName,
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate},
        datatype: "json",
        colModel: [
			{ label: 'userId', name: 'userId', index: 'user_id', width: 40, hidden: true, key: true },
            { label: '注册时间', name: 'createTime', width: 75, align: 'center' },
            { label: '用户信息', name: 'name', width: 75, formatter: function(value, options, row){
                var html = '';
                if (value) {
                    html += value + '<br/>';
                }

                if (row.mobile) {
                    html += row.mobile + '<br/>';
                }

                if (row.idNo) {
                    html += row.idNo;
                }
                return html;
            }},
            { label: '性别', name: 'sex', width: 20, align: 'center' },
            { label: '年龄', name: 'age', width: 20, align: 'center', formatter: function(value, options, row){
                                                                 if (value) {
                                                                     return value;
                                                                 }
                                                                 return '';
                                                             } },
            { label: '芝麻分', name: 'zhimaPoint', width: 30, align: 'center', formatter: function(value, options, row){
                                                                                                                          if (value) {
                                                                                                                              return value;
                                                                                                                          }
                                                                                                                          return '';
                                                                                                                          }
            },
            { label: '户籍城市', name: 'city', width: 60},
			{ label: '导流渠道', name: 'channelName', index: 'channel_name', width: 50, align: 'center' },
			{ label: '资料认证', name: 'basicAuthNames', index: 'basic_auth', width: 120, formatter: function(value, options, row){
                var html= '';
                vm.basicAuthNames.forEach(function(name) {
                    html += '<span class="auth_icon' + (value.includes(name) ? ' done' : '') + '">' + name + '</span>';
                });
                return html;
            }},
			{ label: '借条平台授权', name: 'loanAuthNames', index: 'loan_auth', width: 60, formatter: function(value, options, row){
                            var html= '';
                            vm.loanAuthNames.forEach(function(name) {
                                html += '<span class="auth_icon' + (value.includes(name) ? ' done' : '') + '">' + name + '</span>';
                            });
                            return html;
            }},
            { label: '状态', name: 'latestRequestStatus', index: 'last_request_status', width: 40 , align: 'center', formatter: function(value, options, row){
                    if (!value || value.value === -1) {
                        return '<span class="label label-default">未提交</span>';
                    } else if (value.value === 0) {
                        // 待审核
                        return '<span class="label label-info">' + value.displayName + '</span>';
                    } else if (value.value === 1) {
                        // 审核中
                        return '<span class="label label-warning">' + value.displayName + '</span>';
                    } else if (value.value === 2) {
                        // 已放款
                        return '<span class="label label-success">' + value.displayName + '</span>';
                    } else if (value.value === 3) {
                        // 拒绝受理
                        return '<span class="label label-danger">' + value.displayName + '</span>';
                    } else if (value.value === 4) {
                        // 已结清
                        return '<span class="label label-success">' + value.displayName + '</span>';
                    } else if (value.value === 5 || value.value === 6 || value.value === 7) {
                        // 逾期, 失联, 取消
                        return '<span class="label label-warning">' + value.displayName + '</span>';
                    }
                }},
            { label: '操作', name: 'requestUuid', index: 'request_uuid', width: 40, align: 'center', formatter: function(value, options, row){
                    return '<button type="button" onclick="openRequest(\'' + value + '\')" class="btn btn-link">详情</button>';
                }}
        ],
		viewrecords: true,
        height: 'auto',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

    $('#applyTimeRange').daterangepicker({
        autoApply: true,
        locale: {
            format: 'YYYY-MM-DD',
            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                '七月', '八月', '九月', '十月', '十一月', '十二月'
            ]
        },
        ranges: {
            '当天': [moment(), moment()],
            '一星期': [moment().subtract(6, 'days'), moment()],
            '当前月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "alwaysShowCalendars": true,
        "startDate": vm.q.applyStartDate,
        "endDate": vm.q.applyEndDate
    }, function(start, end, label) {
        vm.q.applyStartDate = start.format('YYYY-MM-DD');
        vm.q.applyEndDate = end.format('YYYY-MM-DD');
    });

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        q: {
		    name: null,
		    channelName: null,
		    applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
            applyEndDate: moment().endOf('month').format('YYYY-MM-DD')
        },
        basicAuthNames: ['基','紧','运','通','支','淘','京','邮','社','公'],
        loanAuthNames: ['米','今','借','无']
	},
	methods: {
        exp: function () {
	        confirm('确定要导出当前搜索条件的所有记录吗？', function(){
                window.open(baseURL + "autha/user/export?token=" + token + '&' + jQuery.param(vm.q), 'downloadFrame');
                return true;
            });
        },
		query: function () {
            // console.log(vm.q);
			vm.reload();
		},
        reset: function () {
            vm.q = {
                name: null,
                channelName: null,
		        applyStartDate: moment().startOf('month').format('YYYY-MM-DD'),
                applyEndDate: moment().endOf('month').format('YYYY-MM-DD')
            };
            var applyTimePicker = $('#applyTimeRange').data('daterangepicker');
            applyTimePicker.setStartDate(vm.q.applyStartDate);
            applyTimePicker.setEndDate(vm.q.applyEndDate);
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.name,
                        'channelName': vm.q.channelName,
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate

                    },
                page:page
            }).trigger("reloadGrid");

		}
	}
});

function openAuthUser(uuid) {
    parent.layer.open({
        type: 2,
        skin: 'layui-layer-molv',
        title: '客户详情',
        shadeClose: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['90%', '95%'],
        content: baseURL + 'modules/auth/user_detail.html?userId=' + uuid,
        cancel: function (index) {
            vm.reload();
        }
    });
}
function openRequest(uuid) {

    window.open(baseURL + 'modules/auth/new_request_detail.html?id=' + uuid, '_blank');
    // parent.layer.open({
    //     type: 2,
    //     skin: 'layui-layer-molv',
    //     title: '处理申请单',
    //     shadeClose: false,
    //     maxmin: true, //开启最大化最小化按钮
    //     area: ['90%', '95%'],
    //     content: baseURL + 'modules/auth/request_detail.html?id=' + uuid,
    //     cancel: function (index) {
    //         vm.reload();
    //     }
    // });
}