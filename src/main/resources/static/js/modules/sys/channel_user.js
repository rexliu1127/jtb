$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/channel_user/list',
        datatype: "json",
        colModel: [
			{ label: '用户ID', name: 'userId', index: "user_id", hidden: true, key: true },
			{ label: '用户名', name: 'username', width: 75 },
            { label: '用户姓名', name: 'name', width: 75 },
            { label: '所属部门', name: 'deptName', width: 75 },
			{ label: '邮箱', name: 'email', width: 90 },
			{ label: '手机号', name: 'mobile', width: 100 },
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 80}
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


var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			username: null
		},
		showList: true,
		title:null,
		roleList:{},
        channelList:{},
		user:{
			status:1,
            deptId:null,
            deptName:null,
			roleIdList:[],
            channelIdList:[]
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增渠道帐号";
            vm.roleList = {};
            vm.channelList = {};
			vm.user = {status:1,
                deptId:null,
                deptName:null,
                roleIdList:[],channelIdList:[]};
			
			//获取角色信息
			this.getRoleList();

            //获取渠道信息
            this.getChannelList();

            vm.getDept();
		},
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
		update: function () {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改子渠道帐号";
			
			vm.getUser(userId);
			//获取角色信息
			this.getRoleList();

            //获取渠道信息
            this.getChannelList();
		},
		del: function () {
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/channel_user/delete",
                    contentType: "application/json",
				    data: JSON.stringify(userIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(){
                                vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function () {
            if(vm.validator()){
                return ;
            }

			var url = vm.user.userId == null ? "sys/channel_user/save" : "sys/channel_user/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.user),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getUser: function(userId){
			$.get(baseURL + "sys/channel_user/info/"+userId, function(r){
				vm.user = r.user;
				vm.user.password = null;

                vm.getDept();
			});
		},
		getRoleList: function(){
		},
        getChannelList: function(){
            $.get(baseURL + "sys/channel_user/select_channel", function(r){
                vm.channelList = r.list;
            });
        },
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
		},
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        validator: function () {
            if(isBlank(vm.user.username)){
                alert("用户名不能为空");
                return true;
            }

            if(vm.user.userId == null && isBlank(vm.user.password)){
                alert("密码不能为空");
                return true;
            }

            if(!isBlank(vm.user.email) && !validator.isEmail(vm.user.email)){
                alert("邮箱格式不正确");
                return true;
			}
        }
	}
});