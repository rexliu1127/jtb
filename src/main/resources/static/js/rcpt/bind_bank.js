function captchaResponse(resp) {
    captcha = resp;
}

$(function () {
    // 约定正则
    var idNumExp = /(?:^\d{15}$)|(?:^\d{18}$)|^\d{17}[\dXx]$/;
    var vCodeExp = /^.{6}$/;
    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;
    var regexp = {
        regexp: {
            IDNUM: idNumExp,
            VCODE: vCodeExp
        }
    };

// 表单提交
    $('#submit').on('click', function () {
        var name = $('#name').val().replace(/\s/g, '');
        var mobile = $('#mobile').val().replace(/\s/g, '');
        var idNo = $('#idNo').val().replace(/\s/g, '');
        var bankAccount = $('#bankAccount').val().replace(/\s/g, '');
        var txUserName = $('#txUserName').val();
        var verifyCode = $('#verifyCode').val();

        if (!name) {
            $.toptip('请输入持卡人姓名', 'error');
            return;
        }

        if (txUserName && txUserName !== name) {
            $.toptip('绑卡姓名与要确认借条的姓名不一致, 请确认借条是否正确!', 'error');
            return;
        }

        if (!idNo || !idNumExp.test(idNo)) {
            $.toptip('请输入正确的身份证号', 'error');
            return;
        }

        if (!bankAccount) {
            $.toptip('请输入银行卡号', 'error');
            return;
        }

        if (!mobile || !mobileExp.test(mobile)) {
            $.toptip('请输入正确的手机号', 'error');
            return;
        }

        if (!verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }

        if (verifyCode.length != 6) {
            $.toptip('请输入正确的手机验证码', 'error');
            return;
        }

        $.showLoading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/bind_bank",
            dataType: 'json',
            data: {mobile: mobile, name: name, idNo: idNo, bankAccount: bankAccount, verifyCode: verifyCode},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();

                    var returnUrl = $('#returnUrl').val();
                    if (!bindedUser) {
                        location.href = baseURL + 'rcpt/set_password.html?returnUrl=' + returnUrl;
                    } else if (returnUrl) {
                        location.href = baseURL + returnUrl;
                    } else {
                        location.href = baseURL + "rcpt/index.html";
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });

    });


    var getBindBankCode = function (event) {
        if (countdown > 0) {
            return;
        }

        var mobile = $('#mobile').val().replace(/\s/g, '');
        if (!mobile || !mobileExp.test(mobile)) {
            $.toptip('请输入正确手机号码', 'error');
            return;
        }

        if (needCaptcha && !captcha) {
            $.toptip('请点击进行人机识别验证', 'error');
            return;
        }

        $.showLoading('加载中...');
        $.get(baseURL + 'sms_code?type=bindBank&mobile=' + mobile + "&captcha=" + captcha, function(r){
            if(r.code == 0){
                $.hideLoading();
                countdown = 60;
                settime($('#getCode'));
            }else{
                $.hideLoading();
                $.toptip(r.msg, 'error');
            }
        });
    };

    var countdown=0;
    function settime(obj) { //发送验证码倒计时
        if (countdown === 0) {
            obj.text("获取验证码");
            obj.on('click', getChangePasswordCode);
            return;
        } else {
            obj.on('click', function() {});
            obj.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
            settime(obj)
        }, 1000);
    }

    $('#getCode').on('click', getBindBankCode);

});
