var step = 0;

$(function () {
    $('#save').on('click', function () {
        var mobilePass = $('#mobilePass').val();

        if (!mobilePass) {
            $.toptip('请输入运营商服务密码', 'error');
            return;
        }

        $.showLoading('加载中');

        if (step === 0) {
            $.ajax({
                type: "POST",
                url: baseURL + "auth/mobile_info",
                dataType: 'json',
                data: {mobilePass: mobilePass},
                success: function(r){
                    if(r.code == 0){
                        createVerifyTask();
                    }else{
                        $.hideLoading();
                        $.toptip(r.msg, 'error');
                    }
                }
            });
        } else if (step === 1) {
            verifyTask();
        }

    });

    var createVerifyTask = function () {
        $.ajax({
            type: "POST",
            url: baseURL + "auth/jxl/create_task",
            dataType: 'json',
            success: function(r){
                if(r.code == 0) {
                    $('#token').val(r.token);
                    $('#website').val(r.website);

                    verifyTask();
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

    var verifyTask = function () {
        var verifyCode = $('#verifyCode').val();
        var token = $('#token').val();
        var website = $('#website').val();
        var type = $('#type').val();
        var queryPwd = $('#queryPwd').val();

        var mobilePass = $('#mobilePass').val();

        if (!mobilePass) {
            $.toptip('请输入运营商服务密码', 'error');
            return;
        }

        if ((type == 'SUBMIT_CAPTCHA' || type == 'RESEND_CAPTCHA') && !verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }
        if (type == 'SUBMIT_QUERY_PWD' && !queryPwd) {
            $.toptip('请输入查询密码', 'error');
            return;
        }

        $.ajax({
            type: "POST",
            url: baseURL + "auth/jxl/verify",
            dataType: 'json',
            data: {token: token, website: website, mobilePass: mobilePass, verifyCode: verifyCode,
                type: type, queryPwd: queryPwd},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    if (r.data.process_code === 10002 || r.data.process_code === 10002) {
                        $('#type').val(r.data.req_msg_tpl.type);
                        $('#verifyCodeDiv').show();
                        step = 1;
                        $.toptip(r.data.content, 'success');
                    } else if (r.data.process_code === 10003) {
                        $.toptip('服务密码错误', 'error');
                    } else if (r.data.process_code === 10004) {
                        $.toptip('短信验证码错误', 'error');

                        setTimeout(function() {
                            location.reload();
                        }, 1000);
                    } else if (r.data.process_code === 10006) {
                        $.toptip('短信验证码失效系统已自动重新下发,请重新输入.', 'error');
                    } else if (r.data.process_code === 10007) {
                        $.toptip('简单密码或初始密码无法登录,请重置密码后重新提交申请.', 'error');

                        setTimeout(function() {
                            location.href = baseURL + 'auth/apply.html';
                        }, 1000);
                    } else if (r.data.process_code === 10008) {
                        $.toptip('提交认证申请成功,请稍后查询认证结果.', 'success');

                        setTimeout(function() {
                            location.href = baseURL + 'auth/apply.html';
                        }, 1000);
                    } else if (r.data.process_code === 100017) {
                        $.toptip('请用本机发送CXXD至10001获取查询详单的验证码.', 'error');
                    } else if (r.data.process_code === 100018) {
                        $.toptip('短信码失效，请用本机发送CXXD至10001获取查询详单的验证码.', 'error');
                    } else if (r.data.process_code === 10022) {
                        $('#type').val(r.data.req_msg_tpl.type);
                        $('#queryPwdDiv').show();
                        step = 1;
                        $.toptip('请输入查询密码', 'success');
                    } else if (r.data.process_code === 10023) {
                        $.toptip('查询密码错误', '');
                    } else if (r.data.process_code === 30000) {
                        $.toptip(r.data.content, 'error');
                        setTimeout(function() {
                            location.reload();
                        }, 1000);
                    } else if (r.data.process_code === 0) {
                        $.toptip('提交申请超时.', 'error');
                        setTimeout(function() {
                            location.reload();
                        }, 1000);
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

});