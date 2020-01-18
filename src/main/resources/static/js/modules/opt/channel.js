function openBarcodeLayer(name, channelKey) {
    $("#barcodeImage").attr('src', baseURL + 'opt/channel/qrCode/' + channelKey + ".png");

    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: name + "(推广二维码)",
        area: ['350px', '300px'],
        shadeClose: false,
        content: jQuery("#barcodeLayer")
    });
}
function openH5Url(name, channelKey) {
    var url = location.href;
    var old_url = url;
    window.open(url.substring(0, old_url.replace("//",'').indexOf("/")+2) + baseURL + "auth/invite.html?channelId=" + channelKey, '_blank');
}

$(function () {

    $("#jqGrid").jqGrid({
        url: baseURL + 'opt/channel/list',
        datatype: "json",
        colModel: [
            {label: '渠道ID', name: 'channelId', index: 'channel_id', width: 50, hidden: true, key: true},
            { label: '所属部门', name: 'deptName', width: 75, align: 'center' },
            {label: '渠道名称', name: 'name', index: 'name', width: 50, align: 'center'},
            {label: '产品名称', name: 'productName', index: 'productName', width: 50, align: 'center'},
            {
                label: 'H5链接', name: '', index: '', width: 100, align: 'center',
                formatter: function (value, option, row) {
                    var url = location.href;
                    var old_url = url;
                    var titleName=row.productName;
                    var name="";
                    if(titleName){
                        name=titleName;
                    }else{
                        name=row.name;
                    }
                    return '<button type="button" class="btn btn-info btn-xs open-barcode"' +
                        ' onclick="javascript:openH5Url(\'' + name + '\',\''
                        + row.channelKey + '\')">查看</button>'
                        + '<button type="button" class="btn btn-link btn-copy" data-clipboard-text="'
                        + (url.substring(0, old_url.replace("//",'').indexOf("/")+2) + baseURL + "auth/invite.html?channelId=" + row.channelKey)
                        + '">复制链接</button>';
                }
            },
            {
                label: '二维码', name: '', index: '', width: 50, align: 'center',
                formatter: function (value, option, row) {
                    var url = location.href;
                    var titleName=row.productName;
                    var name="";
                    if(titleName){
                        name=titleName;
                    }else{
                        name=row.name;
                    }
                    return '<button type="button" class="btn btn-primary btn-xs open-barcode"' +
                        ' onclick="javascript:openBarcodeLayer(\'' + name + '\',\''
                        + row.channelKey + '\')">查看</button>';
                }
            },
            { label: '审核人', name: 'auditorUserName', width: 50, align: 'center' },
            { label: '负责人', name: 'ownerName', width: 50, align: 'center' },
            { label: '创建时间', name: 'createTime', index: 'create_time', width: 50, align: 'center'},
            { label: '状态', name: 'status', width: 50, align: 'center', formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">正常</span>';
                }}
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

    var clipboard = new Clipboard('.btn-copy');

    clipboard.on('success', function(e) {
        alert('复制成功!');
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
        showList: true,
        title: null,
        auditorList: [],
        channel: {
            deptId:null,
            deptName:null,
            auditorIdList:[]
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增渠道";
            vm.channel = {
                deptId:null,
                deptName:null,
                auditorUserId: null,
                auditorIdList:[]
            };

            // 获取审核员列表
            vm.getAuditorList();
            vm.getDept();
        },
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.channel.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.channel.deptName = node.name;
                }
            })
        },
        update: function (event) {
            var channelId = getSelectedRow();
            if(channelId == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            // 获取审核员列表
            vm.getAuditorList(function() {
                vm.getInfo(channelId);
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.channel.channelId == null ? "opt/channel/save" : "opt/channel/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.channel),
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
            var channelIds = getSelectedRows();
            if(channelIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "opt/channel/delete",
                    contentType: "application/json",
                    data: JSON.stringify(channelIds),
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
        getInfo: function(channelId){
            $.get(baseURL + "opt/channel/info/"+channelId, function(r){
                var channel = r.channel;
                channel.imagePath = baseURL + 'opt/channel/img/' + channel.imagePath;
                channel.logoPath=baseURL+'opt/channel/img/'+channel.logoPath;
                vm.channel = channel;

                vm.getDept();
            });
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
                    vm.channel.deptId = node[0].deptId;
                    vm.channel.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
        }
    }

});

new AjaxUpload('#upload', {
    action: baseURL + 'opt/channel/upload?token=' + token,
    name: 'file',
    autoSubmit:true,
    responseType:"json",
    onSubmit:function(file, extension){
        if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
            alert('只支持jpg、png、gif格式的图片！');
            return false;
        }
    },
    onComplete : function(file, r){
        if(r.code == 0){
            vm.channel.imagePath = baseURL + 'opt/channel/img/' + r.url;
            $('#channelImg').attr('src', vm.channel.imagePath);
        }else{
            alert(r.msg);
        }
    }
});
//上传logo
new AjaxUpload('#uploadLogo', {
    action: baseURL + 'opt/channel/upload?token=' + token,
    name: 'file',
    autoSubmit:true,
    responseType:"json",
    onSubmit:function(file, extension){
        if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
            alert('只支持jpg、png、gif格式的图片！');
            return false;
        }
    },
    onComplete : function(file, r){
        if(r.code == 0){
            vm.channel.logoPath = baseURL + 'opt/channel/img/' + r.url;
            $('#logoImg').attr('src', vm.channel.logoPath);
        }else{
            alert(r.msg);
        }
    }
});