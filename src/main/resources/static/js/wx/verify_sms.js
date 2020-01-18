$(function () {
// 失去焦点时检测
//        weui.form.checkIfBlur('#form', regexp);

// 表单提交
    document.querySelector('#nextStep').addEventListener('click', function () {
        var verifyCode = $('#verifyCode').val();

        if (!verifyCode) {
            weui.topTips('请输入手机验证码');
            return;
        }

        if (verifyCode.length != 6) {
            weui.topTips('请输入正确的手机验证码');
            return;
        }

        var loading = weui.loading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/verify_sms_code",
            dataType: 'json',
            data: {code: verifyCode, type: $('#type').val()},
            success: function(r){
                if(r.code == 0){
                    loading.hide();
                    weui.toast('验证成功', 3000);

                    location.href = baseURL + $('#forwardUrl').val();
                }else{
                    loading.hide();
                    weui.topTips(r.msg)
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

        var loading = weui.loading('提交中...');
        $.get(baseURL + 'wx/get_sms_code?type=' + type, function(r){
            if(r.code == 0){
                loading.hide();
                $('#verifyCodeTitle').text('验证码已发送至尾号为' + userMobile.substring(7) + '的手机');
                countdown = 60;
                settime($('#getCode'));
            }else{
                loading.hide();
                weui.topTips(r.msg);
            }
        });
    }

    var countdown=0;
    function settime(obj) { //发送验证码倒计时
        if (countdown === 0) {
            obj.text("获取验证码");
            return;
        } else {
            obj.on('click', getChangePasswordCode);
            obj.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
            settime(obj)
        }, 1000)
    }

    document.querySelector('#getCode').addEventListener('click', getChangePasswordCode);

});