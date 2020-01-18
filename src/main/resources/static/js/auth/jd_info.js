var step = 0;
var needSmsCode = 0;
var needAuthCode = 0;

$(function () {
    $('#save').on('click', function () {
        var username = $('#mobile').val();
        var mobilePass = $('#mobilePass').val();
        var verifyCode = $('#verifyCode').val();
        var authCode = $('#authCode').val();
        var token = $('#token').val();
        var taskStage = $('#taskStage').val();

        if (!mobilePass) {
            $.toptip('请输入京东登录密码', 'error');
            return;
        }
        if (needSmsCode && !verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }
        if (needAuthCode && !authCode) {
            $.toptip('请输入验证码', 'error');
            return;
        }

        $.showLoading('加载中');
        if (step === 0) {
            initVerify(username, mobilePass, taskStage);
        } else if (step === 1) {
            verifyTask(token, verifyCode, authCode, taskStage);
        }

    });

    var initVerify = function(username, password, taskStage) {
        var token = $('#token').val();
        $.ajax({
            type: "POST",
            url: baseURL + "auth/ds/init_verify",
            dataType: 'json',
            data: {taskId: token, username: username, password: password, taskStage: taskStage, dsType: 'JD', requestType: 'submit'},
            success: function(r){
                if(r.code == 0) {
                    var remoteRes = r.remoteRes;
                    if (remoteRes.code === 100) {
                        $('#token').val(remoteRes.task_id);
                        setTimeout(queryInitVerify, 1000, username, password, taskStage);
                    } else {
                        processRemoteResponse(remoteRes);
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

    var queryInitVerify = function(username, password, taskStage) {
        var token = $('#token').val();
        $.ajax({
            type: "POST",
            url: baseURL + "auth/ds/init_verify",
            dataType: 'json',
            data: {taskId: token, username: username, password: password, taskStage: taskStage, dsType: 'JD', requestType: 'query'},
            success: function(r){
                if(r.code == 0) {
                    var remoteRes = r.remoteRes;
                    if (remoteRes.code === 100) {
                        setTimeout(queryInitVerify, 1000, username, password, taskStage);
                    } else {
                        processRemoteResponse(remoteRes);
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

    var verifyTask = function (taskId, smsCode, authCode, taskStage) {
        $.ajax({
            type: "POST",
            url: baseURL + "auth/ds/submit_verify",
            dataType: 'json',
            data: {taskId: taskId, smsCode: smsCode, authCode: authCode, taskStage: taskStage, requestType: 'submit'},
            success: function(r){
                if(r.code == 0) {
                    var remoteRes = r.remoteRes;
                    if (remoteRes.code === 100) {
                        setTimeout(querySubmitVerify, 1000, taskId, smsCode, authCode, taskStage);
                    } else {
                        processRemoteResponse(remoteRes);
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

    var querySubmitVerify = function (taskId, smsCode, authCode, taskStage) {
        $.ajax({
            type: "POST",
            url: baseURL + "auth/ds/submit_verify",
            dataType: 'json',
            data: {taskId: taskId, smsCode: smsCode, authCode: authCode, taskStage: taskStage, requestType: 'query'},
            success: function(r){
                if(r.code == 0) {
                    var remoteRes = r.remoteRes;
                    if (remoteRes.code === 100) {
                        setTimeout(querySubmitVerify, 1000, taskId, smsCode, authCode, taskStage);
                    } else {
                        processRemoteResponse(remoteRes);
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    };

    var processRemoteResponse = function (remoteRes) {
        if (remoteRes.data) {
            $('#taskStage').val(remoteRes.data.next_stage);
        }
        $('#token').val(remoteRes.task_id);
        if (remoteRes.code === 137 || remoteRes.code === 2007) {
            $.hideLoading();
            $.toast('提交成功!', function () {
                location.href = baseURL + 'auth/apply.html';
            });
        } else if (remoteRes.code === 101) {
            // 请输入图片验证码
            $.hideLoading();
            $.toptip(remoteRes.message, 'success');
            step = 1;
            needAuthCode = 1;
            $('#authCode').val('');
            $('#authCodeImg').attr('src', 'data:image/png;base64,' + remoteRes.data.auth_code);
            $('#authCodeDiv').show();

        } else if (remoteRes.code === 104) {
            // 图片验证码失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');
        } else if (remoteRes.code === 105) {
            // 请输入手机验证码
            $.hideLoading();
            $.toptip(remoteRes.message, 'success');
            step = 1;
            needSmsCode = 1;
            $('#verifyCode').val('');
            $('#verifyCodeDiv').show();
        } else if (remoteRes.code === 108) {
            // 手机验证码失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');
        } else if (remoteRes.code === 112) {
            // 手机密码错误
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');
            setTimeout(function() {
                location.reload();
            }, 1000);
        } else if (remoteRes.code === 113) {
            // 登录失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.reload();
            }, 1000);
        } else if (remoteRes.code === 116) {
            // 身份证或姓名检验失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.reload();
            }, 1000);
        } else if (remoteRes.code === 122) {
            // 图片验证码错误或过期
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');
        } else if (remoteRes.code === 123) {
            // 请输入手机验证码和图片验证码
            $.hideLoading();
            $.toptip(remoteRes.message, 'success');
            step = 1;
            needAuthCode = 1;
            $('#authCode').val('');
            $('#authCodeImg').attr('src', 'data:image/png;base64,' + remoteRes.data.auth_code);
            $('#authCodeDiv').show();

        } else if (remoteRes.code === 124) {
            // 手机验证码错误或过期
            $.hideLoading();
            $.toptip('手机验证码错误或过期', 'error');
        } else if (remoteRes.code === 130) {
            // 验证码识别失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.reload();
            }, 1000);
        } else if (remoteRes.code >= 200 && remoteRes.code <= 207) {
            // 验证码识别失败
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.reload();
            }, 1000);
        } else if (remoteRes.code === 2006) {
            // 任务超时
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.href = 'mobile_info.html';
            }, 1000);
        } else {
            $.hideLoading();
            $.toptip(remoteRes.message, 'error');

            setTimeout(function() {
                location.reload();
            }, 1000);
        }
    };

    var token = $('#token').val();
    if (token) {
        retryTask(token);
    }

});