$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'tx/txcomplain/list',
        datatype: "json",
        colModel: [
			{ label: 'complainId', name: 'complainId', index: 'complain_id', key: true, hidden: true },
            { label: '提交时间', name: 'createTime', index: 'create_time', width: 50 },
            { label: '申诉人信息', name: 'borrowerName', index: 'borrower_name', width: 80, formatter: function(value, options, row){
                return value + '<br />' + row.borrowerMobile + '<br />' + row.borrowerIdNo;
            }},
            { label: '借条信息', name: 'txUuid', index: 'tx_uuid', width: 80, formatter: function(value, options, row){
            	return '编号: ' + value + '<br/>'
					+ "出借人: " + row.lenderName + '<br/>'
				    + '金额: ' + row.amount + '元 <br/>'
                    + '开始日期: ' + row.beginDate + '<br/>'
                    + '结束日期: ' + row.endDate + '<br/>'
                    + '状态: ' + row.txStatusLabel + '';
            }},
			{ label: '申诉原因', name: 'complainTypeDesc', index: 'complain_type', width: 80},
			{ label: '申诉描述', name: 'remark', index: 'remark', width: 80 },
			{ label: '申诉证据', name: 'imagePaths', index: 'image_path', align: 'center', width: 80, formatter: function(value, options, row){
                var html = '';
                if (!value || value.length === 0) {
                	return html;
				}
				html += '<div id="imageView' + row.complainId + '" title="点击查看所有证据图片">'
                for (var i = 0; i < value.length; i++) {
                    html += '<img style="max-width:100px;max-height:100px; ' + (i > 0 ? 'display:none;' : '')
                        + '" src="' + baseURL + 'userimg/' + value[i] + '"/>';
                }
                return html + "</div>";
            }  },
			{ label: '状态', name: 'complainResult', index: 'status', align: 'center', width: 50, formatter: function(value, options, row){
                return '<span class="label ' + (value === 0 ? 'label-info' : (value === 1 ? 'label-warning' : 'label-success')) + '">' + row.complainResultDesc + '</span>';
            } },
			{ label: '处理结果', name: 'processorComment', index: 'processor_comment', width: 80 },
			{ label: '操作', name: '', index: 'update_time', align: 'center', width: 50, formatter: function(value, options, row){
                return '<button type="button" onclick="openHistory(\'' + row.complainId + '\')" class="btn' +
                    ' btn-link">查看记录</button>';
            }  }
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
        afterInsertRow: function (rowid, rowdata, rowelem) {
            console.log(rowid);
            console.log(rowdata);
            console.log(rowelem);
        },
        loadComplete: function (data) {
            if (data.page.list) {
                data.page.list.forEach(function (row) {
                    var complainId = row.complainId;
                    var imageDiv = document.getElementById('imageView' + complainId);
                    if (imageDiv) {
                       new Viewer(imageDiv);
                    }
                });
            }
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
            var grid = $("#jqGrid");

            grid.closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });

        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            keyword: null,
            status:null
        },
		showList: true,
		title: null,
        audit: {},
		txComplain: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (event) {
			var complainId = getSelectedRow();
			if(complainId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(complainId);
		},
		saveOrUpdate: function (event) {
			var url = vm.txComplain.complainId == null ? "tx/txcomplain/save" : "tx/txcomplain/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.txComplain),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
        process: function () {
            var complainId = getSelectedRow();
            if(complainId == null){
                return ;
            }

            vm.audit = {complainId: complainId, status: 1};
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "处理申诉",
                area: ['550px', '330px'],
                shadeClose: false,
                content: jQuery("#layer"),
                btn: ['确定','取消'],
                btn1: function (index) {
                    if (!vm.audit.status) {
                        alert('请选择处理状态');
                        return;
                    }

                    $.ajax({
                        type: "POST",
                        url: baseURL + "tx/txcomplain/process",
                        data: vm.audit,
                        dataType: "json",
                        success: function(r){
                            if(r.code == 0){
                                layer.close(index);
                                alert('处理成功', function(){
                                    vm.reload();
                                });
                            }else{
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
		reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'keyword': vm.q.keyword, 'status': vm.q.status},
                page:page
            }).trigger("reloadGrid");
		}
	}
});

function openHistory(complainId) {
    parent.layer.open({
        type: 2,
        skin: 'layui-layer-molv',
        title: '处理结果',
        shadeClose: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['600px', '600px'],
        content: baseURL + 'modules/tx/complain_result.html?complainId=' + complainId + '&token=' + token
    });
}