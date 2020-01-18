$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'autha/staff/list',
        datatype: "json",
        colModel: [			
			{ label: 'staffId', name: 'staffId', index: 'staff_id', width: 50, key: true, hidden: true},
			{ label: '客服类型', name: 'staffType', index: 'staff_type', width: 80 , align:'center', formatter: function(value, options, row){
                    // return value.value === 0 ?
                    //     '<span class="label label-success">微信客服</span>' :
                    //     '<span class="label label-success">QQ客服</span>';
                    return '<span class="label label-success">微信客服</span>\'';
                }},
			{ label: '微信号码', name: 'staffNo', index: 'staff_no', width: 80, align:'center' },
			{ label: '关联用户', name: 'processorName', index: 'processor_name', width: 80, align:'center' },
			{ label: '最后更新时间', name: 'updateTime', index: 'update_time', width: 80, align:'center' }
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

    vm.init();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        auditorList: [],
		authStaff: {

		}
	},
	methods: {
		query: function () {
			vm.reload();

		},
		add: function(){
			vm.showList = false;
			vm.title = "新增客服";
			vm.authStaff = {};

            // 获取审核员列表
            vm.getAuditorList();
		},
		update: function (event) {
			var staffId = getSelectedRow();
			if(staffId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            // 获取审核员列表
            vm.getAuditorList(function() {
                vm.getInfo(staffId);
            });
		},
		saveOrUpdate: function (event) {
			var url = vm.authStaff.staffId == null ? "autha/staff/save" : "autha/staff/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.authStaff),
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
			var staffIds = getSelectedRows();
			if(staffIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "autha/staff/delete",
                    contentType: "application/json",
				    data: JSON.stringify(staffIds),
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
		getInfo: function(staffId){
			$.get(baseURL + "autha/staff/info/"+staffId, function(r){
                vm.authStaff = r.authStaff;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        getAuditorList: function(callback){
            if (vm.auditorList && vm.auditorList.length) {
                if (callback) {
                    callback();
                }

                return;
            }

            $.get(baseURL + "opt/channel/auditorList", function(r){
                vm.auditorList = r.list;

                if (callback) {
                    callback();
                }
            });
        },
        addImgFileListener: function(obj, fun) {
            $(obj).change(function() {
                try {
                    var file = this.files[0];
                    var reader = new FileReader();
                    reader.onload = function() {
                        var img = new Image();
                        img.src = reader.result;
                        img.onload = function() {
                            var w = img.width,
                                h = img.height;
                            var canvas = document.createElement('canvas');
                            var ctx = canvas.getContext('2d');
                            $(canvas).attr({
                                width: w,
                                height: h
                            });
                            ctx.drawImage(img, 0, 0, w, h);
                            var base64 = canvas.toDataURL('image/jpeg', 0.5);
                            var result = {
                                url: window.URL.createObjectURL(file),
                                base64: base64,
                                clearBase64: base64.substr(base64.indexOf(',') + 1),
                                suffix: base64.substring(base64.indexOf('/') + 1, base64.indexOf(';')),
                            };
                            fun(result);
                        }
                    };
                    reader.readAsDataURL(this.files[0]);
                } catch(e) {
                    alert('上传出错: ' + e.message);
                }
            });
        },
        init: function () {
            vm.addImgFileListener("#upload", function(data) {
                if (data.base64 && data.base64.length > 200 * 1024 * 4 / 3) {
                    alert('上传二维码不能大于200K');
                    return;
                }
                $("#staffBarcode").attr("src", data.base64);
                vm.authStaff.staffBarcode = data.base64;
            });
        }
	}
});

