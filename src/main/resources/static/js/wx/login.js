$(function () {

    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;

    $('#loginBtn').on('click', function () {
        var mobile = $('#mobile').val();

        if (!mobile || !mobileExp.test(mobile)) {
            $.toptip('请输入正确的手机号', 'error');
            return;
        }

        var verifyCode = $('#verifyCode').val();

        if (!verifyCode) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }

        if (verifyCode.length != 6) {
            $.toptip('请输入手机验证码', 'error');
            return;
        }

        $.showLoading('加载中...');
        $.ajax({
            type: "POST",
            url: baseURL + "wx_login",
            dataType: 'json',
            data: {verifyCode: verifyCode, mobile: mobile, type: "login", unionId: $('#unionId').val(),
                openId: $('#openId').val(),
                nickName: $('#nickName').val(), headImgUrl: $('#headImgUrl').val()},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    var returnUrl = $('#returnUrl').val();
                    if (returnUrl && returnUrl.indexOf('/') == 0) {
                        returnUrl = returnUrl.substring(1);
                        location.href = baseURL + returnUrl;
                    } else {
                        location.href = baseURL + 'wx/main';
                    }
                }else{
                    $.hideLoading();
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

        $.showLoading('加载中...');
        $.get(baseURL + 'sms_code?type=login&mobile=' + mobile, function(r){
            if(r.code == 0){
                $.hideLoading();
                countdown = 60;
                settime($('#getCode'));
            }else{
                $.hideLoading();
                $.toptip(r.msg, 'error');
            }
        });
    }

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
