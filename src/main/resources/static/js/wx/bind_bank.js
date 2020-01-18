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

// 失去焦点时检测
//        weui.form.checkIfBlur('#form', regexp);

// 表单提交
    document.querySelector('#formSubmitBtn').addEventListener('click', function () {
//            weui.form.validate('#form', function (error) {
//                console.log(error);
//                if (!error) {
//                    var loading = weui.loading('提交中...');
//                    setTimeout(function () {
//                        loading.hide();
//                        weui.toast('提交成功', 3000);
//                    }, 1500);
//                }
//            }, regexp);
        var name = $('#name').val();
        var mobile = $('#mobile').val();
        var idNo = $('#idNo').val();
        var bankAccount = $('#bankAccount').val();
        var txUserName = $('#txUserName').val();

        if (!name) {
            weui.topTips('请输入持卡人姓名');
            return;
        }

        if (txUserName && txUserName !== name) {
            weui.topTips('绑卡姓名与要确认借条的姓名不一致, 请确认借条是否正确!')
            return;
        }

        if (!idNo || !idNumExp.test(idNo)) {
            weui.topTips('请输入正确的身份证号');
            return;
        }

        if (!bankAccount) {
            weui.topTips('请输入银行卡号');
            return;
        }

        if (!mobile || !mobileExp.test(mobile)) {
            weui.topTips('请输入正确的手机号');
            return;
        }


        $('#mainPannel').hide();
        $('#verifyCodePannel').show();
    });

    document.querySelector('#nextStep').addEventListener('click', function () {
//            weui.form.validate('#form', function (error) {
//                console.log(error);
//                if (!error) {
//                    var loading = weui.loading('提交中...');
//                    setTimeout(function () {
//                        loading.hide();
//                        weui.toast('提交成功', 3000);
//                    }, 1500);
//                }
//            }, regexp);
        var name = $('#name').val();
        var mobile = $('#mobile').val();
        var idNo = $('#idNo').val();
        var bankAccount = $('#bankAccount').val();
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
            url: baseURL + "wx/bind_bank",
            dataType: 'json',
            data: {mobile: mobile, name: name, idNo: idNo, bankAccount: bankAccount, verifyCode: verifyCode},
            success: function(r){
                if(r.code == 0){
                    loading.hide();
                    weui.toast('绑卡成功', 3000);

                    var returnUrl = $('#returnUrl').val();
                    if (!bindedUser) {
                        location.href = baseURL + 'wx/set_password_page?returnUrl=' + returnUrl;
                    } else if (returnUrl) {
                        location.href = baseURL + returnUrl;
                    } else {
                        location.href = baseURL + "wx/main";
                    }
                }else{
                    loading.hide();
                    weui.topTips(r.msg)
                }
            }
        });
    });

    var getBindBankCode = function () {
        if (countdown > 0) {
            return;
        }

        var mobile = $('#mobile').val();

        var loading = weui.loading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/get_bind_bank_code",
            dataType: 'json',
            data: {mobile: mobile},
            success: function(r){
                if(r.code == 0){
                    loading.hide();
                    $('#verifyCodeTitle').text('验证码已发送至尾号为' + mobile.substring(7) + '的手机');
                    countdown = 60;
                    settime($('#getCode'));
                }else{
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

    var countdown=0;
    function settime(obj) { //发送验证码倒计时
        if (countdown === 0) {
            obj.val("获取验证码");
            return;
        } else {
            obj.on('click', getBindBankCode);
            obj.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
            settime(obj)
        }, 1000)
    }

    document.querySelector('#getCode').addEventListener('click', getBindBankCode);

});