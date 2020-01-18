function captchaResponse(resp) {
    captcha = resp;
}

$(function () {
// 失去焦点时检测
//        weui.form.checkIfBlur('#form', regexp);

// 表单提交
    $('#nextStep').on('click', function () {
        var verifyCode = $('#verifyCode').val();

        if (!verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }

        if (verifyCode.length != 6) {
            $.toptip('请输入正确的手机验证码', 'error');
            return;
        }


        if (needCaptcha && !captcha) {
            $.toptip('请点击进行人机识别验证', 'error');
            return;
        }

        $.showLoading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/verify_sms_code",
            dataType: 'json',
            data: {code: verifyCode, type: $('#type').val()},
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    $.toast('验证成功', function () {
                        location.href = baseURL + $('#forwardUrl').val();
                    });
                }else{
                    $.toptip(r.msg, 'error')
                }
            }
        });
    });

    var getChangePasswordCode = function () {
        if (countdown > 0) {
            return;
        }

        var mobile = $('#mobile').val();
        var type = $('#type').val();

        if (needCaptcha && !captcha) {
            $.toptip('请点击进行人机识别验证', 'error');
            return;
        }

        $.showLoading('提交中...');
        $.get(baseURL + 'sms_code?type=' + type + '&mobile=' + userMobile + "&captcha=" + captcha, function(r){
            $.hideLoading();
            if(r.code == 0){
                // $('#verifyCodeTitle').text('验证码已发送至尾号为' + userMobile.substring(7) + '的手机');
                countdown = 60;
                settime($('#getCode'));
            }else{
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

    $('#getCode').on('click', getChangePasswordCode);

});