var needSmsCode = 0;

$(function () {
    $('#save').on('click', function () {
        var mobile = $('#mobile').val();
        var mobilePass = $('#mobilePass').val();
        var verifyCode = $('#verifyCode').val();
        var authCode = $('#authCode').val();

        if (!mobile) {
            $.toptip('请输入手机号码', 'error');
            return;
        }

        if (!mobilePass) {
            $.toptip('请输入QQ同步助手登录密码', 'error');
            return;
        }
        if (needSmsCode && !verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }
        if (!authCode) {
            $.toptip('请输入验证码', 'error');
            return;
        }

        $.showLoading('加载中');
        if (needSmsCode) {
            $.ajax({
                type: "POST",
                url: baseURL + "auth/login_qq_sync2",
                dataType: 'json',
                data: {mobile: mobile, verifyCode: verifyCode, csrfToken: $('#csrfToken').val()},
                success: function(r){
                    $.hideLoading();
                    if(r.code == 0){
                        $.toptip('认证成功!', 'success');
                        setTimeout(function() {
                            location.href = 'apply.html';
                        }, 3000);
                    }else{
                        $.toptip(r.msg, 'error');
                        setTimeout(function() {
                            location.reload();
                        }, 3000);
                    }
                }
            });
        } else {
            $.ajax({
                type: "POST",
                url: baseURL + "auth/login_qq_sync1",
                dataType: 'json',
                data: {mobile: mobile, password: mobilePass, captcha: authCode},
                success: function(r){
                    $.hideLoading();
                    if(r.code == 0){
                        if (r.refresh) {
                            $.toptip('登录中断，请稍后重试', 'success');
                            setTimeout(function() {
                                location.href = baseURL + 'auth/qq_contact_sync.html';
                            }, 1000);
                        } else {
                            needSmsCode = 1;
                            $("#csrfToken").val(r.csrfToken);
                            $("#verifyCodeDiv").show();
                            $.toptip(r.msg, 'success');
                        }

                    }else{
                        $.toptip(r.msg, 'error');
                        setTimeout(function() {
                            location.href = baseURL + 'auth/qq_contact_sync.html';
                        }, 3000);
                    }
                }
            });
        }
    });

});