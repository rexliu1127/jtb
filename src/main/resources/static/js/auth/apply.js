var androidUploadContact = function () {
    var result = window.android.verifyContact();

    if (result === 1) {
        $.toast("认证成功", function () {
            location.href = baseURL + 'auth/apply.html';
        });
    } else if (result === 0) {
        $.toptip('获取手机联系人数据失败!', 'error');
    } else if (result === 2) {
        $.toptip('上传手机联系人数据失败!', 'error');
    }
};

var iosUploadContact = function () {
    $.showLoading("加载中...");
    try {
        window.webkit.messageHandlers.uploadMachineContact.postMessage(getCookie('authTokenId'));
    } catch (e) {
        $.hideLoading();
        $.toptip('上传失败', 'error');
        return;
    }

    $.hideLoading();
    $.toast("认证成功", function () {
        location.href = baseURL + 'auth/apply.html';
    });
};

var goToTjH5Verification = function (tianjiType, isNew) {
    if (!isNew) {
        $.toptip('您已提认证, 无需重复提交.', 'success');
        return;
    }


    $.showLoading("加载中...");
    $.ajax({
        type: "POST",
        url: baseURL + "auth/tj/collect_user",
        dataType: 'json',
        data: {tianjiType: tianjiType},
        success: function (r) {
            $.hideLoading();
            if (r.code == 0) {
                // loading.hide();
                // setCookie("authTokenId", r.authTokenId);

                var remoteRes = r.remoteRes;
                if (remoteRes.error == 200) {
                    location.href = remoteRes.result.redirectUrl;
                } else {
                    $.toptip(remoteRes.msg, 'error');
                }
            } else {
                // loading.hide();
                $.toptip(r.msg, 'error');
            }
        }
    });
};

var goToYxH5Verification = function (type, isNew) {
    if (!isNew) {
        $.toptip('您已提认证, 无需重复提交.', 'success');
        return;
    }

    $.showLoading("加载中...");
    $.ajax({
        type: "POST",
        url: baseURL + "auth/yx/h5",
        dataType: 'json',
        data: {yiXiangType: type},
        success: function (r) {
            $.hideLoading();
            if (r.code == 0) {
                // loading.hide();
                // setCookie("authTokenId", r.authTokenId);

                location.href = r.url;
            } else {
                // loading.hide();
                $.toptip(r.msg, 'error');
            }
        }
    });
};

var goForH5CreditPage = function (type, status) {
    if (status === 1) {
        $.toptip('您已提认证, 无需重复提交.', 'success');
        return;
    }

    $.showLoading("加载中...");
    $.ajax({
        type: "GET",
        url: baseURL + "auth/credit/h5/" + type,
        dataType: 'json',
        success: function (r) {
            $.hideLoading();
            if (r.code == 0) {
                location.href = r.url;
            } else {
                $.toptip(r.msg, 'error');
            }
        }
    });
};
var showHideMore = function () {
    var moreDiv = $('#more_cert');
    if (moreDiv.is(":visible")) {
        moreDiv.hide();
    } else {
        moreDiv.show();
    }
};

var submitAuth = function(lat, lng) {
    $.ajax({
        type: "POST",
        url: baseURL + "auth/submit",
        dataType: 'json',
        data: {latitude: lat, longitude: lng},
        success: function (r) {
            if (r.code == 0) {
                // loading.hide();
                $.toast("提交成功", function () {
                    location.href = baseURL + 'auth/apply.html?showCS=true';
                });
            } else {
                // loading.hide();
                $.toptip(r.msg, 'error');
            }
        }
    });
};
var goForH5Index = function (type, status) {
    if (status === 1) {
        $.toptip('您已提认证, 无需重复提交.', 'success');
        return;
    }
    location.href ="/mapp/index.html?s=h5";
};
//判断账户余额是否充足
var identityAuthentication = function () {
    $.ajax({
        type: "POST",
        url: baseURL + "auth/verifyBalance",
        dataType: 'json',
        success: function (r) {
            if (r.code == 0) {
                location.href ="personal_info_id.html";
            } else {
                $.toptip(r.msg, 'error');
               //alert("账户余额不足，请联系管理员充值");
            }
        }
    });
};

$(function () {
    $('#submit').on('click', function () {
        if (jsapi_ticket && jsapi_ticket.length > 0) {
            wx.getLocation({
                success: function (res) {
                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                    submitAuth(latitude, longitude);
                },
                cancel: function (res) {
                    $.toptip('请同意获取地理位置信息!', 'error');
                }
            });
        } else {
            submitAuth();
        }
    });

    $('.advancedBTN').click(function () {
        var extraCertElems = $('.extra-cert');
        if (extraCertElems.hasClass('hidden')) {
            extraCertElems.removeClass('hidden');
            $(this).html("收展<i></i>");
        } else {
            extraCertElems.addClass('hidden');
            $(this).html("提升额度认证<i></i>");
        }
    });

    $(".submitBtn").delegate(".showCS","click",function(){
        $("#sucessBox").show();
    });
    $(".laybg").click(function(){
        $(this).parent("div").hide();
    });

    if (GetQueryString('showCS') == 'true') {
        $("#sucessBox").show();
    } else if (GetQueryString('error') == 'yd') {
        $.toptip('实名人像对比失败, 请重新提交身份证认证!', 'error');
    }

    setTimeout(function(){
        location.href = baseURL + 'auth/apply.html';
    }, 10000);
});
