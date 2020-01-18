function unbindWechat() {
    weui.confirm('注销绑定当前微信账号后,您依然可以用手机号在其他微信或PC端登录. 确认注销绑定吗?',
        {
            title: '提醒',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    var loading = weui.loading('加载中...');
                    $.ajax({
                        type: "POST",
                        url: baseURL + "wx/unbind_wechat",
                        dataType: 'json',
                        success: function(r){
                            if(r.code == 0){
                                loading.hide();
                                weui.toast('注销绑定成功', function () {
                                    if (r.isWechat) {
                                        location.href = baseURL + 'wx/main';
                                    }
                                });
                            }else{
                                loading.hide();
                                weui.topTips(r.msg);
                            }
                        }
                    });
                }
            }]
        });
}