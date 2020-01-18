$(function () {
    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;


    var bottonEnabled = 0;

    $('#name').on('keypress', function () {
        if (!bottonEnabled) {
            enabledButton();
        }

    });


    $('#mobile').on('keypress', function () {
        if (!bottonEnabled) {
            enabledButton();
        }
    });


    $('#name').on('blur', function () {
        if (!bottonEnabled) {
            enabledButton();
        }

    });

    $('#mobile').on('blur', function () {
        if (!bottonEnabled) {
            enabledButton();
        }
    });

    function enabledButton() {
        var name = $('#name').val();
        var mobile = $('#mobile').val();

        if (name && mobile) {
            var submitBtn = $('#submit');
            submitBtn.removeClass('but_color_b');
            submitBtn.addClass('but_color_y');

            bottonEnabled = 1;
        }
    }

    $('#submit').on('click', function () {
        if (!bottonEnabled) {
            return;
        }

        var name = $('#name').val();
        var mobile = $('#mobile').val();

        if (!name) {
            $.toptip('请输入查询对象姓名', 'error');
            return;
        }

        if (!mobile || !mobileExp.test(mobile)) {
            $.toptip('请输入正确的手机号', 'error');
            return;
        }

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/member/check_credit",
            dataType: 'json',
            data: {name: name, mobile: mobile},
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    location.href = baseURL + "wx/member/credit_detail/" + r.userId;
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });

    });

    enabledButton();
});