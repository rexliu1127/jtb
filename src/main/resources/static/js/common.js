//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//请求前缀
var baseURL = "/m/";
if (window.location.href.indexOf("/test/") > 0) {
    baseURL = "/test/";
}
// 网站名称
var siteName = '';

//登录token
var token = localStorage.getItem("token");
if(token == 'null'){
    parent.location.href = baseURL + 'login.html';
}

//设置页面title
// document.title = siteName + ' | ' + document.title;

//jquery全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false,
    headers: {
        "token": token
    },
    xhrFields: {
	    withCredentials: true
    },
    complete: function(xhr) {
        //token过期，则跳转到登录页面
        if(xhr.responseJSON.code == 401){
            parent.location.href = baseURL + 'login.html';
        }
    }
});

//jqgrid全局配置
$.extend($.jgrid.defaults, {
    ajaxGridOptions : {
        headers: {
            "token": token
        }
    }
});

//权限判断
function hasPermission(permission) {
    var perm = '';
    if (window.opener && window.opener.parent && window.opener.parent.permissions) {
        perm = window.opener.parent.permissions;
    } else if (window.parent) {
        perm = window.parent.permissions;
    }

    if (perm.indexOf(permission) > -1) {
        return true;
    } else {
        return false;
    }
}

//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(index){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
        parent.layer.close(index);
	});
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}

//判断是否为正整数 true:是
function isPositiveInteger(s){
    var re = /^[0-9]+$/ ;
    return re.test(s)
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


function openTxAgreement(txId) {
    parent.parent.layer.open({
        type: 2,
        skin: 'layui-layer-molv',
        title: '借款协议',
        shadeClose: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['90%', '95%'],
        btn: ['关闭'],
        content: baseURL + 'tx/txbase/agreement?txId=' + txId + "&token=" + token
    });
}

function downloadTxAgreement(txId) {
    window.open(baseURL + 'tx/txbase/download_agreement?txId=' + txId + "&token=" + token);
}

T.luosimao = {
    host: 'cuisb.cn',
    siteKey: 'e1c15d4e87670f358b7924b7d1534f5d'
};

function isLuosimaoCaptchaEnabled() {
    return T.luosimao.host && location.href.indexOf(T.luosimao.host) > 0;
}

//
$(document).ready(function(){
	$('.distinguish table tbody tr:even, .applyFor table tbody tr:even').addClass('footable-even');
	$('.distinguish table tbody tr:odd, .applyFor table tbody tr:odd').addClass('footable-odd');	
});