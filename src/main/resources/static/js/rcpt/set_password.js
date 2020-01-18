$(document).ready(function () {
    var showPasswordPopup = function () {
        $(".bounced-R").addClass("modal-active");
        if ($(".mask").length > 0) {
            $(".mask").addClass("mask-active");
        } else {
            $("body").append('<div class="mask"></div>');
            $(".mask").addClass("mask-active");
        }

        $(".mask-active,.bouncedAncel").click(function () {
            $(".bounced-R").removeClass("modal-active");
            setTimeout(function () {
                $(".mask-active").removeClass("mask-active");
                $(".mask").remove();
                emptypsw();
            }, 300);
        });
    };

    //----
    var i = 0;

    $(".keyboard li .numpsw").click(function () {
        if (i < 6) {
            $(".sixpsw li").eq(i).addClass("mmdd");
            $(".sixpsw li").eq(i).attr("data", $(this).text());
            i++
            if (i == 6) {
                setTimeout(function () {
                    var data = "";
                    $(".sixpsw li").each(function () {
                        data += $(this).attr("data");
                    });


                    var loading = $.showLoading('提交中...');
                    $.ajax({
                        type: "POST",
                        url: baseURL + "rcpt/set_password",
                        dataType: 'json',
                        data: {p: data},
                        success: function (r) {
                                if (r.code == 0) {
                                    $.hideLoading();
                                    $.toast('设置密码成功');

                                    var returnUrl = $('#returnUrl').val();
                                    var signImgPath = $('#signImgPath').val();

                                    if (!returnUrl) {
                                        returnUrl = "rcpt/index.html";
                                    }
                                    if (signImgPath) {
                                        location.href = baseURL + returnUrl;
                                    } else {
                                        location.href = baseURL + "rcpt/member/sign.html?returnUrl=" + returnUrl;
                                    }
                                } else {
                                    $.hideLoading();
                                    $.toptip(r.msg);
                                }
                        }
                    });
                }, 100);
            }
        }
    });

    $(".keyboard li .delpsw").click(function () {
        if (i > 0) {
            i--
            $(".sixpsw li").eq(i).removeClass("mmdd");
            $(".sixpsw li").eq(i).attr("data", "");
        }
    });

    function emptypsw() {
        $(".sixpsw li").removeClass("mmdd");
        $(".sixpsw li").attr("data", "");
        i = 0;
    };

    $(".keyboard li .emptypsw").click(function () {
        // emptypsw()
    });

    showPasswordPopup();

});


