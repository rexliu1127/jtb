$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'autha/request/list',
        postData: {'status': vm.q.status,
            'channelId': vm.q.channelId,
            /*'auditStartDate': vm.q.auditStartDate,
            'auditEndDate': vm.q.auditEndDate,*/
            'applyStartDate': vm.q.applyStartDate,
            'applyEndDate': vm.q.applyEndDate,
            'allocateStatus': vm.q.allocateStatus,
            'name': vm.q.name,
            'mobile': vm.q.mobile,
            'assignee': vm.q.assignee},
        datatype: "json",
        colModel: [
			{ label: 'requestId', name: 'requestId', index: 'request_id', width: 40, hidden: true, key: true },
            { label: '公司名称', name: 'merchantName', index: 'merchant_name',align: 'center', width: 80},
            { label: '所属部门', name: 'deptName', width: 75,align: 'center'},
			{ label: '渠道', name: 'channelName', index: 'channel_name', width: 80,align: 'center'},
			{ label: '用户信息', name: 'mobile', index: 'user_id', width: 80,align: 'center', formatter: function(value, options, row){
                return '<h3>' + (row.name ? row.name : '-') + '</h3><span>' + value + '</span>';
            }},
			{ label: '审核人', name: 'processorName', index: 'processor_name', width: 40 ,align: 'center', formatter: function(value, options, row){
                return !value ? (!row.processorUserName ? '' : row.processorUserName) : value;
            }},
			{ label: '审核时间', name: 'updateTime', index: 'update_time', width: 80 ,align: 'center'},
            { label: '申请时间', name: 'createTime', index: 'create_time', width: 80 ,align: 'center'},
			{ label: '处理员', name: 'assigneeName', index: 'assigneeName', width: 40 , align: 'center', formatter: function(value, options, row){
                return !value ? (!row.assigneeUserName ? '' : row.assigneeUserName) : value;
            }},
			{ label: '状态', name: 'status', index: 'status', width: 40 , align: 'center', formatter: function(value, options, row){
			    if (value.value === -1) {
                    return '<span class="label label-default">' + value.displayName + '</span>';
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
        },
        loadComplete: function (data) {
            var merchantNo=data.merchantno;
            if(merchantNo=='00'){
                $("#jqGrid").setGridParam().hideCol("requestUuid").trigger("reloadGrid");
            }
        }
    });

   /* $('#auditTimeRange').daterangepicker({
        autoUpdateInput: false,
        autoApply: true,
        locale: {
            format: 'YYYY-MM-DD'
        },
        ranges: {
            '当天': [moment(), moment()],
            '一星期': [moment().subtract(6, 'days'), moment()],
            '当前月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "alwaysShowCalendars": true,
        "startDate": moment().subtract(6, 'days'),
        "endDate": moment()
    }, function(start, end, label) {
        vm.q.auditStartDate = start.format('YYYY-MM-DD');
        vm.q.auditEndDate = end.format('YYYY-MM-DD');
    });

    $('#auditTimeRange').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
    });

    $('#auditTimeRange').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });*/

    $('#applyTimeRange').daterangepicker({
        autoUpdateInput: false,
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
    $('#applyTimeRange').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
    });

    $('#applyTimeRange').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });
    vm.init();
    vm2.getAuditorList();
    vm3.getMerchantList();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		authRequest: {},
        selectedIdList: [],
        requestStatusList: [],
        channelList: [],
        q: {
		    status: null
        }
	},
	methods: {
        init: function () {
            $.get(baseURL + "autha/request/status/list", function(r){
                vm.requestStatusList = r.list;
            });
            $.get(baseURL + "opt/channel/list?_search=false&limit=100&page=1&sidx=&order=asc", function(r){
                vm.channelList = r.page.list;
            });
        },
        allocate: function () {
            var requestIds = getSelectedRows();
            if (!requestIds) {
                return;
            }
            vm.selectedIdList = requestIds;

            vm2.auditorId = $("#auditorId option:first").val();
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "分配",
                area: ['550px', '220px'],
                shadeClose: false,
                content: jQuery("#allocationLayer"),
                btn: ['分配','取消'],
                cancel: function (index) {
                    vm.reload();
                },
                btn1: function (index) {
                    if (!vm2.auditorId) {
                        alert('请选择审核人!');
                        return;
                    }
                    $.ajax({
                        type: "POST",
                        url: baseURL + "autha/request/allocate",
                        data: {requestIds: vm.selectedIdList, auditorUserId: vm2.auditorId},
                        dataType: "json",
                        success: function(r){
                            if(r.code == 0){
                                layer.close(index);
                                alert('成功分配' + r.successCount + '条记录');
                                vm.reload();
                            }else{
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        recommend: function () {
            var requestIds = getSelectedRows();
            if (!requestIds) {
                return;
            }
            vm.selectedIdList = requestIds;


            vm3.merchantId = $("#merchantId option:first").val();
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "推荐",
                area: ['550px', '220px'],
                shadeClose: false,
                content: jQuery("#targetMerchantLayer"),
                btn: ['推荐','取消'],
                cancel: function (index) {
                },
                btn1: function (index) {
                    if (!vm3.merchantId) {
                        alert('请选择目标公司!');
                        return;
                    }
                    $.ajax({
                        type: "POST",
                        url: baseURL + "autha/request/recommend",
                        data: {requestIds: vm.selectedIdList, merchantNo: vm3.merchantId},
                        dataType: "json",
                        success: function(r){
                            if(r.code == 0){
                                layer.close(index);
                                alert('成功推荐' + r.successCount + '条记录' + (r.errorCount ? (', ' + r.errorCount + '条记录失败') : ''));
                                vm.reload();
                            }else{
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            });

        },
        process: function () {
            var requestId = getSelectedRow();
            if (!requestId) {
                return;
            }

        },
        exp: function () {
	        if (!$("#jqGrid").getGridParam("selrow")) {
                confirm('确定要导出当前搜索条件的所有记录吗？', function(){
                    window.open(baseURL + "autha/request/export?token=" + token + '&' + jQuery.param(vm.q), 'downloadFrame');
                    return true;
                });
            } else {
                var requestIds = getSelectedRows();
                confirm('确定要导出选中的记录吗？', function(){
                    window.open(baseURL + "autha/request/export_by_id?token=" + token + '&requestIds=' + requestIds, 'downloadFrame');
                });
            }
        },
		query: function () {
            // console.log(vm.q);
			vm.reload();
		},
        reset: function () {
            vm.q = {
                status: null,
                channelId: null,
                /*auditStartDate: null,
                auditEndDate: null,*/
                /*applyStartDate: moment().subtract(6, 'days').format('YYYY-MM-DD'),
                applyEndDate: moment().format('YYYY-MM-DD'),*/
                applyStartDate:null,
                applyEndDate: null,
                allocateStatus: -1,
                name: null,
                mobile: null,
                assignee: null
            };
            //修改申请时间默认为空
            /*var applyTimePicker = $('#applyTimeRange').data('daterangepicker');
            applyTimePicker.setStartDate(vm.q.applyStartDate);
            applyTimePicker.setEndDate(vm.q.applyEndDate);*/
            $('#applyTimeRange').val('');
            //$('#auditTimeRange').val('');
            vm.reload();
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'status': vm.q.status,
                        'channelId': vm.q.channelId,
                       /* 'auditStartDate': vm.q.auditStartDate,
                        'auditEndDate': vm.q.auditEndDate,*/
                        'applyStartDate': vm.q.applyStartDate,
                        'applyEndDate': vm.q.applyEndDate,
                        'allocateStatus': vm.q.allocateStatus,
                        'name': vm.q.name,
                        'mobile': vm.q.mobile,
                        'assignee': vm.q.assignee

                    },
                page:page
            }).trigger("reloadGrid");

			vm2.getAuditorList();
		}
	}
});

var vm2 = new Vue({
    el:'#allocationLayer',
    data:{
        auditorList: [],
        auditorId: null
    },
    methods: {
        getAuditorList: function(callback){
            if (vm.auditorList && vm.auditorList.length) {
                if (callback) {
                    callback();
                }

                return;
            }

            $.get(baseURL + "opt/channel/auditorList", function(r){
                vm2.auditorList = r.list;

                if (callback) {
                    callback();
                }
            });
        }
    }
});


var vm3 = new Vue({
    el:'#targetMerchantLayer',
    data:{
        merchantList: [],
        merchantId: null
    },
    methods: {
        getMerchantList: function(callback){
            if (vm.merchantList && vm.merchantList.length) {
                if (callback) {
                    callback();
                }

                return;
            }

            $.get(baseURL + "sys/sysmerchant/list?limit=200", function(r){
                vm3.merchantList = r.page.list;

                if (callback) {
                    callback();
                }
            });
        }
    }
});

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