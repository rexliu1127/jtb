$(document).ready(function () {
    //your code here
    $('#password').on('input', function (e) {
        var numLen = 6;
        var pw = $('#password').val();

        var firstDiv = $('#firstStep');
        var secondDiv = $('#secondStep');
        var list = firstDiv.find('li');
        for (var i = 0; i < numLen; i++) {
            if (pw[i]) {
                $(list[i]).text('*');
            } else {
                $(list[i]).text('');
            }
        }

        if (pw.length === numLen) {
            firstDiv.hide();
            secondDiv.show();
            $('#password2').focus();
        }
    });


    $('#password2').on('input', function (e) {
        var numLen = 6;

        var passwordEle = $('#password');
        var password2Ele = $('#password2');
        var firstDiv = $('#firstStep');
        var secondDiv = $('#secondStep');


        var pw = password2Ele.val();

        var list = secondDiv.find('li');
        for (var i = 0; i < numLen; i++) {
            if (pw[i]) {
                $(list[i]).text('*');
            } else {
                $(list[i]).text('');
            }
        }

        if (pw.length === numLen) {
            var pw1 = passwordEle.val();
            if (pw !== pw1) {
                weui.alert('确认密码不匹配, 请重新输入');

                passwordEle.val('');
                password2Ele.val('');

                $('li').each(function () {
                    $(this).text('');
                });

                secondDiv.hide();
                firstDiv.show();

                return;
            }

            var loading = weui.loading('提交中...');
            $.ajax({
                type: "POST",
                url: baseURL + "wx/set_password",
                dataType: 'json',
                data: {p: pw1},
                success: function (r) {
                    if (r.code == 0) {
                        loading.hide();
                        weui.toast('设置密码成功', 3000);

                        var returnUrl = $('#returnUrl').val();
                        if (returnUrl) {
                            location.href = baseURL + returnUrl;
                        } else {
                            location.href = baseURL + "wx/main";
                        }
                    } else {
                        loading.hide();
                        weui.topTips(r.msg)
                    }
                }
            });
        }
    });

    $('#password').focus();
});


