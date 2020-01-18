function openBarcodeLayer(name, txUuid) {
    $("#barcodeImage").attr('src', baseURL + 'tx/txbase/qrCode/' + txUuid + ".png");

    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: name + "(借条二维码)",
        area: ['350px', '300px'],
        shadeClose: false,
        content: jQuery("#barcodeLayer")
    });
}
function openH5Url(lenderName, txUuid) {
    var url = location.href;
    window.open(url.substring(0, url.indexOf(baseURL)) + baseURL + "rcpt/confirm_transaction/" + txUuid+".html", '_blank');
}

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/txbase/list',
        postData: {'status': vm.q.status,
            'borrowerValue': vm.q.borrowerValue,
            'lenderValue': vm.q.lenderValue,
            'beginDateStart': vm.q.beginDateStart,
            'beginDateEnd': vm.q.beginDateEnd,
            'repayDateStart': vm.q.repayDateStart,
            'repayDateEnd': vm.q.repayDateEnd},
        datatype: "json",
        colModel: [
			{ name: 'txId', index: "tx_id", key: true, hidden: true },
            { label: '出借人', name: 'lenderName', width: 75, align: 'center', formatter: function(value, options, row){
                if (row.lenderMobile) {
                    return value + '<br />' + row.lenderMobile + '<br />' + row.lenderIdNo;
                }
                return '';
            }},
			{ label: '借款人', name: 'borrowerName', width: 75 ,align: 'center', formatter: function(value, options, row){
                //if (row.borrowerMobile) {
                    return value;// + '<br />' + row.borrowerMobile + '<br />' + row.borrowerIdNo;
               // }
                //return '';
            }},
            { label: '借款金额(元)', name: 'amount', align: 'center', width: 50 },
			{ label: '未还金额(元)', name: 'outstandingAmount', align: 'center', width: 40 },
            { label: '开始日期', name: 'beginDate', align: 'center',width: 50 },
            { label: '还款日期', name: 'repayDate', align: 'center',width: 50 },
            { label: '展期情况(次)', name: 'extensionCount', align: 'center',width: 50 },
            {
                label: 'H5链接', name: '', index: '', width: 100, align: 'center',
                formatter: function (value, option, row) {
                    if(row.status.value == 1)  //待确定状态才可以查看
                    {
                        var url = location.href;
                        return '<button type="button" class="btn btn-info btn-xs open-barcode"' +
                            ' onclick="javascript:openH5Url(\'' + row.lenderName + '\',\''
                            + row.txUuid + '\')">查看</button>'
                            + '<button type="button" class="btn btn-link btn-copy" data-clipboard-text="'
                            + (url.substring(0, url.indexOf(baseURL)) + baseURL + "rcpt/confirm_transaction/" + row.txUuid+".html")
                            + '">复制链接</button>';
                    }
                    else {
                        return '';
                    }

                }
            },
            {
                label: '二维码', name: '', index: '', width: 50, align: 'center',
                formatter: function (value, option, row) {

                    if(row.status.value == 1)  //待确定状态才可以查看
                    {
                        var url = location.href;
                        return '<button type="button" class="btn btn-primary btn-xs open-barcode"' +
                            ' onclick="javascript:openBarcodeLayer(\'' + row.lenderName + '\',\''
                            + row.txUuid + '\')">查看</button>';
                    }else
                    {
                        return '';
                    }

                }
            },
			{ label: '状态', name: 'status', align: 'center',width: 40, formatter: function(value, options, row){
                if (value.value === 0) {
                    //
                    return '<span class="label label-info">' + value.displayName + '</span>';
                } else if (value.value === 1) {
                    // 待确定
                    return '<span class="label label-default">' + value.displayName + '</span>';
                } else if (value.value === 2) {
                    // 还款中
                    return '<span class="label label-info">' + value.displayName + '</span>';
                } else if (value.value === 3) {
                    // 已还清
                    return '<span class="label label-success">' + value.displayName + '</span>';
                } else if (value.value === 4) {
                    // 已逾期
                    return '<span class="label label-danger">' + value.displayName + '</span>';
                } else if (value.value === 5 || value.value === 6) {
                    // 待支付，已驳回
                    return '<span class="label label-default">' + value.displayName + '</span>';
                }
			}},
			{ label: '合同', name: 'txId', align: 'center',width: 40, formatter: function(value, options, row){
			    var status = row.status.value;
			    if (status == 2 || status == 3 || status == 4) {
                    return '<button type="button" onclick="openTxAgreement(\'' + value + '\')" class="btn btn-link">查看</button>'
                        + '<button type="button" onclick="downloadTxAgreement(\'' + value + '\')" class="btn btn-link">下载</button>';
			    } else {
			        return '';
			    }

            }},
            { label: '操作', name: 'txId', index: 'tx_uuid', align: 'center',width: 40, formatter: function(value, options, row){
                var html = '';
                var status = row.status.value;

                if (hasPermission('tx:txbase:update') && (status === 2 || status === 4)) {
                    html = '<button type="button" onclick="vm.completeOneTx(\'' + value + '\')" class="btn btn-link">核销</button>';
                }
                return html + '<button type="button" onclick="openTx(\'' + value + '\')" class="btn btn-link">详情</button>';
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

    $('#beginDateRange').daterangepicker({
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
                "startDate": vm.q.beginDateStart,
                "endDate": vm.q.beginDateEnd
            }, function(start, end, label) {
                vm.q.beginDateStart = start.format('YYYY-MM-DD');
                vm.q.beginDateEnd = end.format('YYYY-MM-DD');
            });

    $('#repayDateRange').daterangepicker({
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
            "startDate": moment(),
            "endDate": moment()
        }, function(start, end, label) {
            vm.q.repayDateStart = start.format('YYYY-MM-DD');
            vm.q.repayDateEnd = end.format('YYYY-MM-DD');
        });


    $('#repayDateRange').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
    });

    $('#repayDateRange').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });


    var clipboard = new Clipboard('.btn-copy');

    clipboard.on('success', function(e) {
        alert('复制成功!');
    });

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			borrowerValue: null,
			lenderValue: null,
			status:null,
			beginDateStart: moment().startOf('month').format('YYYY-MM-DD'),
			beginDateEnd: moment().format('YYYY-MM-DD'),
			repayDateStart: null,
            repayDateEnd: null
		},
		showList: true,
		title:null,
		txbase:{
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        completeOneTx: function (txId) {
            confirm('确定要核销该记录吗？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "tx/txbase/complete",
                    contentType: "application/json",
                    data: JSON.stringify([txId]),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        completeTx: function (event) {
            var txIds = getSelectedRows();
            if(txIds == null){
                return ;
            }

            confirm('确定要核销选中的记录吗？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "tx/txbase/complete",
                    contentType: "application/json",
                    data: JSON.stringify(txIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
		markStatus2: function (event) {
			var txIds = getSelectedRows();
            if(txIds == null){
                return ;
            }

			confirm('确定要将选中的记录标记为未还款吗？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "tx/txbase/status/CONFIRMED",
                    contentType: "application/json",
				    data: JSON.stringify(txIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		del: function (event) {
			var txIds = getSelectedRows();
            if(txIds == null){
                return ;
            }

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "tx/txbase/delete",
                    contentType: "application/json",
				    data: JSON.stringify(txIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
        reset: function () {
            vm.q = {
                status: null,
                borrowerValue: null,
                lenderValue: null,
                beginDateStart: moment().startOf('month').format('YYYY-MM-DD'),
                beginDateEnd: moment().format('YYYY-MM-DD'),
                repayDateStart: null,
                repayDateEnd: null
            };
            var beginDatePicker = $('#beginDateRange').data('daterangepicker');
            beginDatePicker.setStartDate(vm.q.beginDateStart);
            beginDatePicker.setEndDate(vm.q.beginDateEnd);
            $('#repayDateRange').val('');
            vm.reload();
        },
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'status': vm.q.status,
                                      'borrowerValue': vm.q.borrowerValue,
                                      'lenderValue': vm.q.lenderValue,
                                      'beginDateStart': vm.q.beginDateStart,
                                      'beginDateEnd': vm.q.beginDateEnd,
                                      'repayDateStart': vm.q.repayDateStart,
                                      'repayDateEnd': vm.q.repayDateEnd},
                page:page
            }).trigger("reloadGrid");
		}
	}
});


function openTx(txId) {
    parent.layer.open({
        type: 2,
        skin: 'layui-layer-molv',
        title: '借条内容',
        shadeClose: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['600px', '380px'],
        content: baseURL + '/tx/txbase/' + txId + '?token=' + token
    });
}
