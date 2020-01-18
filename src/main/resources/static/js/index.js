//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{},index:0},
    template:[
        '<li :class="{active: (item.type===0 && index === 0)}">',
        '<a v-if="item.type === 0" href="javascript:;">',
        '<i v-if="item.icon != null" :class="item.icon"></i>',
        '<span>{{item.name}}</span>',
        '<i class="fa fa-angle-up pull-right"></i>',
        '</a>',
        '<ul v-if="item.type === 0" class="treeview-menu">',
        '<menu-item :item="item" :index="index" v-for="(item, index) in item.list"></menu-item>',
        '</ul>',
        '<a v-if="item.type === 1" :href="\'#\'+item.url">' +
        '<i v-if="item.icon != null" :class="item.icon"></i>' +
        '<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
        '</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});
}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);

var vm = new Vue({
	el:'#rrapp',
	data:{
		user:{},
		merchant: {},
		menuList:{},
        funinfo:{},
		main:"main.html",
		password:'',
		newPassword:'',
        navTitle:"欢迎页"
	},
	methods: {
		getMenuList: function () {
			$.getJSON(baseURL + "sys/menu/nav", function(r){
				vm.menuList = r.menuList;
                window.permissions = r.permissions;
			});
		},
		getUser: function(){
			$.getJSON(baseURL + "sys/user/info?merchant=1", function(r){
				vm.user = r.user;
				vm.merchant = r.merchant;
				vm.funinfo = r.funinfo;

				if (vm.user.merchantNo === '00') {
                    vm.merchant = {
                        "merchant": "00",
                        "name": "宝信通智能管理",
                        "logo": "images/logokk.png"
                    };
				}
				if(!vm.funinfo){
                    vm.funinfo = {
                        "remainingSum":"00"
                    };
                }
                if (vm.merchant.logo) {
                    $('#logoImg').attr('src', vm.merchant.logo);
                    $("#merchantLogo").attr("src", vm.merchant.base64);
                    $('#merchantName').hide();
                }

                document.title = vm.merchant.name;
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改密码",
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: baseURL + "sys/user/password",
					    data: data,
					    dataType: "json",
					    success: function(r){
							if(r.code == 0){
								layer.close(index);
								layer.alert('修改成功', function(){
									location.reload();
								});
							}else{
								layer.alert(r.msg);
							}
						}
					});
	            }
			});
		},
        logout: function () {
            $.ajax({
                type: "POST",
                url: baseURL + "sys/logout",
                dataType: "json",
                success: function(r){
                    //删除本地token
                    localStorage.removeItem("token");
                    //跳转到登录页面
                    location.href = baseURL + 'login.html';
                }
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        },
        updateMerchant: function(){
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "设置",
                area: ['550px', '270px'],
                shadeClose: false,
                content: jQuery("#setupLayer"),
                btn: ['修改','取消'],
                btn1: function (index) {
                    var url = "sys/sysmerchant/update";
                    $.ajax({
                        type: "POST",
                        url: baseURL + url,
                        contentType: "application/json",
                        data: JSON.stringify(vm.merchant),
                        success: function(r){
                            if(r.code === 0){
                                layer.close(index);
                                alert('修改成功', function(index){
                                    location.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        clickOnLogo: function () {
            if (vm.user.merchantNo === '00') {
                location.href = 'index.html';
            } else {
                vm.updateMerchant();
            }
        },
        funinfoView:function ()
        {
            //获取账户余额总表的商户编号，如果为空说明该商户没有充值记录，所以不可以点击查询充值记录
            if(vm.funinfo.merchantNo){
                location.href = 'index.html#modules/sys/sysfundetails.html';
            }
        },
        logoChanged: function () {
            console.log("logo changed");
            var fileElem = document.getElementById("uploadLogo");
            try {
                var file = fileElem.files[0];
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

                        if (result.base64 && result.base64.length > 200 * 1024 * 4 / 3) {
                            alert('上传二维码不能大于200K');
                            return;
                        }
                        $("#merchantLogo").attr("src", result.base64);
                        vm.merchant.logo = result.base64;
                    }
                };
                reader.readAsDataURL(fileElem.files[0]);
            } catch(e) {
                alert('上传出错: ' + e.message);
            }
        }
	},
	created: function(){
		this.getMenuList();
		this.getUser();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});



function routerList(router, menuList){
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;
				
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
                $(".sidebar-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}

