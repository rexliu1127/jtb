$(function () {

    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;

    $('#loginBtn').on('click', function () {
        var mobile = $('#mobile').val();
         var channelId = $('#channelId').val();

         if (!channelId) {
             $.toptip('非法渠道, 请重新扫码进入!', 'error');
             return;
         }

        if (!mobile || !mobileExp.test(mobile) || mobile.length !== 11) {
            $.toptip('请输入正确的手机号', 'error');
            return;
        }

        var captcha = $('#captcha').val();
        if (!captcha) {
            $.toptip('请输入校验码', 'error');
            return;
        } else if (captcha.length !== 4) {
            $.toptip('请输入正确的校验码', 'error');
            return;
        }

        var verifyCode = $('#verifyCode').val();

        if (!verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }

        if (verifyCode.length !== 6) {
            $.toptip('请输入正确手机验证码', 'error');
            return;
        }
        // var loading = weui.loading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/login",
            dataType: 'json',
            data: {verifyCode: verifyCode, mobile: mobile, captcha: captcha},
            success: function(r){
                if(r.code == 0){
                    // loading.hide();
                    // setCookie("authTokenId", r.authTokenId);

                    location.href = baseURL + 'auth/apply.html';
                }else{
                    // loading.hide();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });

    var getChangePasswordCode = function (event) {
        if (countdown > 0) {
            return;
        }

        var mobile = $('#mobile').val();
        if (!mobile || !mobileExp.test(mobile)) {
            $.toptip('请输入正确手机号码', 'error');
            return;
        }

        var captcha = $('#captcha').val();
        if (!captcha) {
            $.toptip('请输入校验码', 'error');
            return;
        } else if (captcha.length !== 4) {
            $.toptip('请输入正确的校验码', 'error');
            return;
        }

        $.showLoading('加载中...');
        $.get(baseURL + 'sms_code2?type=login&mobile=' + mobile + '&captcha=' + captcha, function(r){
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

    $('#getCode').on('click', getChangePasswordCode);

    $('#captchaImg').click(function () {
        this.src = '../captcha.jpg?t=' + $.now();
    });
});
