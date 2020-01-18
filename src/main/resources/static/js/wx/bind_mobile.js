$(function () {
    // 约定正则
    var regexp = {
        regexp: {
            IDNUM: /(?:^\d{15}$)|(?:^\d{18}$)|^\d{17}[\dXx]$/,
            VCODE: /^.{4}$/
        }
    };

// 失去焦点时检测
//     weui.form.checkIfBlur('#form', regexp);

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
        var mobile = $('#mobile').val();
        var code = $('#verificationCode').val();
        if(!mobile || !(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(mobile))) {
            weui.topTips('请输入正确的手机号码');
            return;
        }
        if (!code || !(/^.d{6}$/.test(code))) {
            weui.topTips('请输入6位手机验证码');
            return;
        }

        var loading = weui.loading('登录中...');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/bind_mobile",
            dataType: 'json',
            data: {mobile: mobile, code: code},
            success: function(r){
                if(r.code == 0){
                    loading.hide();
                    location.href = baseURL + "wx/main";
                }else{
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    });
});