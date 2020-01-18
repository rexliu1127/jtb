$(function () {
    var ue = UE.getEditor('editor');//初始化新闻内容编辑器
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sys_notice/list',
        datatype: "json",
        colModel: [			
			{ label: 'noticeId', name: 'noticeId', index: 'notice_id',hidden:true, width: 50, key: true },
			{ label: '标题', name: 'noticeTitle', index: 'notice_title',align: 'center', width: 80 },
			{ label: '是否发布', name: 'noticeStatus', index: 'notice_status',align: 'center', width: 80,
                formatter: function(value, options, row){
                    if (value== 0) {
                        return '未发布';
                    }else if(value== 1){
                        return '发布';
                    } else {
                        return '';
                    }
                }
			},
			{ label: '内容', name: 'noticeContent',hidden:true, index: 'notice_content', width: 80 },
			{ label: '发布时间', name: 'createTime', index: 'create_time',align: 'center', width: 80 },
			{ label: '发布人', name: 'userName', index: 'userName',align: 'center', width: 80 }
        ],
		viewrecords: true,
        height: 385,
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		sysNotice: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysNotice = {};
		},
		update: function (event) {
			var noticeId = getSelectedRow();
			if(noticeId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(noticeId)
		},
		saveOrUpdate: function (event) {
            if(vm.validator()){
                return ;
            }
            var content=UE.getEditor('editor').getContent();
            vm.sysNotice.noticeContent=content;
			var url = vm.sysNotice.noticeId == null ? "sys/sys_notice/save" : "sys/sys_notice/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysNotice),
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
		del: function (event) {
			var noticeIds = getSelectedRows();
			if(noticeIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/sys_notice/delete",
                    contentType: "application/json",
				    data: JSON.stringify(noticeIds),
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
		getInfo: function(noticeId){
			$.get(baseURL + "sys/sys_notice/info/"+noticeId, function(r){
                vm.sysNotice = r.sysNotice;
                var ue = UE.getEditor('editor');//初始化新闻内容编辑器
                //回显内容
                var cont=vm.sysNotice.noticeContent;
                ue.ready(function(){
                    ue.setContent(cont);
                });
            });
		},
		reload: function (event) {
            var ue = UE.getEditor('editor');//初始化新闻内容编辑器
            ue.ready(function(){
                ue.setContent('');
            });
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
            if(isBlank(vm.sysNotice.noticeStatus)){
                alert("请选择是否发布");
                return true;
            }
        }
	}
});