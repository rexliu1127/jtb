function captchaResponse(resp) {
    captcha = resp;
}

$(function () {
    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;


    // var bottonEnabled = 0;
    //
    // $('#name').on('keypress', function () {
    //     if (!bottonEnabled) {
    //         enabledButton();
    //     }
    //
    // });
    //
    //
    // $('#mobile').on('keypress', function () {
    //     if (!bottonEnabled) {
    //         enabledButton();
    //     }
    // });
    //
    //
    // $('#name').on('blur', function () {
    //     if (!bottonEnabled) {
    //         enabledButton();
    //     }
    //
    // });
    //
    // $('#mobile').on('blur', function () {
    //     if (!bottonEnabled) {
    //         enabledButton();
    //     }
    // });

    // function enabledButton() {
    //     var name = $('#name').val();
    //     var mobile = $('#idNo').val();
    //
    //     if (name && mobile) {
    //         var submitBtn = $('#submit');
    //         submitBtn.removeClass('but_color_b');
    //         submitBtn.addClass('but_color_y');
    //
    //         bottonEnabled = 1;
    //     }
    // }

    $('#submit').on('click', function () {
        // if (!bottonEnabled) {
        //     return;
        // }

        var name = $('#name').val();
        var idNo = $('#idNo').val();

        if (!name) {
            $.toptip('请输入查询对象姓名', 'error');
            return;
        }

        if (!idNo || !(idNo.length === 18 || idNo.length === 15)) {
            $.toptip('请输入正确的身份证号码', 'error');
            return;
        }

        if (needCaptcha && !captcha) {
            $.toptip('请点击进行人机识别验证', 'error');
            return;
        }

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/member/check_credit",
            dataType: 'json',
            data: {name: name, idNo: idNo, captcha: captcha},
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    location.href = baseURL + "rcpt/member/credit_detail/" + r.userId + pageExt;
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });

    });

    // enabledButton();
});