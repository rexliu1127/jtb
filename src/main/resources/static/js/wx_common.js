
//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;
T.pageSize = 10;
T.pageNum = 1;

//请求前缀
var baseURL = "/m/";
if (window.location.href.indexOf("/test/") > 0) {
    baseURL = "/test/";
}
// 网站名称
var siteName = '友信用';
// 页面后缀
var pageExt = '.html';

//设置页面title
if (document.title) {
    // document.title = document.title + " | " + siteName;
} else {
    document.title = siteName;
}


/**
 * 获取url中"?"符后的字符串并正则匹配
 * 如：http://xxx.xx.xx?code=aaaa，返回aaaa
 * @param {Object} name
 */
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    var context = "";
    if(r != null)
        context = r[2];
    reg = null;
    r = null;
    return context == null || context == "" || context == "undefined" ? "" : context;
}

function parseDate(str) {
    // validate year as 4 digits, month as 01-12, and day as 01-31
    if ((str = str.match (/^(\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/))) {
        // make a date
        str[0] = new Date (+str[1], +str[2] - 1, +str[3]);
        // check if month stayed the same (ie that day number is valid)
        if (str[0].getMonth () === +str[2] - 1)
            return str[0];
    }
    return undefined;
}

function daydiff(first, second) {
    return Math.round((second-first)/(1000*60*60*24));
}

function go(url) {
    location.href = baseURL + url;
}

function goForgetPasswordPage() {
    var curURL = location.href;
    location.href = baseURL + 'rcpt/verify_sms.html?type=password&forwardUrl='
        + encodeURI('rcpt/set_password.html?returnUrl=' + location.pathname.substring(baseURL.length) + location.search);
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cn) {
    var name = cn+"=";
    var allCookie = decodeURIComponent(document.cookie).split(';');
    var cval = [];
    for(var i=0; i < allCookie.length; i++) {
        if (allCookie[i].trim().indexOf(name) == 0) {
            cval = allCookie[i].trim().split("=");
        }
    }
    return (cval.length > 0) ? cval[1] : "";
}

function confirmPop(title, message, extra, buttons) {
    var box = $('.dialog-box');
    if (box) {
        box.remove();
    }

    var html = '<div class="dialog-box modal-in">' +
        '<div class="dialog-inner radius7PX">' +
        '<div class="dialog-content"><h2>' +
        (title ? title : '') +
        '</h2><p class=\'radius3PX\'>' + (message ? message : '') +
        '</p>';
    if (extra) {
        html = html + '<em>' + extra + '</em>';
    }
    html = html +
        '</div>' +
        '<div class="dialog-btn display">';
    if (buttons && buttons.length > 0) {
        if (buttons.length > 1) {
            html = html +
                '<div class="close flex1 radius7PX confirm-popup-btn-0">' + buttons[0].label + '</div>' +
                '<div class="send flex1 radius7PX confirm-popup-btn-1">' + buttons[1].label + '</div>';
        } else {
            html = html +
                '<div class="send flex1 radius7PX confirm-popup-btn-1">' + buttons[1].label + '</div>';
        }
    }
    html = html +
        '</div>' +
        '</div>' +
        '</div>';

    $(document.body).append(html);

    if (buttons && buttons.length > 0) {
        if (buttons.length > 1) {
            $('.confirm-popup-btn-0').on('click', function () {
                $(".dialog-box").remove();
                if (buttons[0].onClick) {
                    buttons[0].onClick();
                }
            });
            $('.confirm-popup-btn-1').on('click', function () {
                $(".dialog-box").remove();
                if (buttons[1].onClick) {
                    buttons[1].onClick();
                }
            });
        } else {
            $('.confirm-popup-btn-0').on('click', function () {
                $(".dialog-box").remove();
                if (buttons[0].onClick) {
                    buttons[0].onClick();
                }
            });
        }
    }
}

function hidePasswordPopup() {
    $(".bounced-R").removeClass("modal-active");
    setTimeout(function () {
        $(".mask-active").removeClass("mask-active");
        $(".mask").remove();
        $(".bounced-R").remove();
        emptypsw();
    }, 300);
}

function showPasswordPopup() {
    $(".bounced-R").addClass("modal-active");
    if ($(".mask").length > 0) {
        $(".mask").addClass("mask-active");
    } else {
        $("body").append('<div class="mask"></div>');
        $(".mask").addClass("mask-active");
    }

    $(".mask-active,.bouncedAncel").click(hidePasswordPopup);
}

function emptypsw() {
    $(".sixpsw li").removeClass("mmdd");
    $(".sixpsw li").attr("data", "");
    i = 0;
}

function PasswordInit(fn, title) {
    var html = '<div class="paypsw bounced-R"><div class="numb_box"><div class="sixpswbox"><div class="paytitle">' +
        '<h3>' + (title ? title : '请输入交易密码') + '</h3></div><ul class="sixpsw radius3PX">' +
        '<li></li><li></li><li></li><li></li><li></li><li></li></ul><div class="forgetpsw"></div>' +
        '</div><ul class="keyboard"><li><div class="numpsw">1</div></li><li class="border0101">' +
        '<div class="numpsw">2</div></li><li><div class="numpsw">3</div></li><li><div class="numpsw">4</div></li>' +
        '<li class="border0101"><div class="numpsw">5</div></li><li><div class="numpsw">6</div></li><li>' +
        '<div class="numpsw">7</div></li><li class="border0101"><div class="numpsw">8</div></li><li>' +
        '<div class="numpsw">9</div></li><li><div class="emptypsw">清空</div></li><li class="border0101">' +
        '<div class="numpsw">0</div></li><li><div class="delpsw">删除</div></li></ul></div></div>';
    $("body").append(html);

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

                    fn(data);

                    hidePasswordPopup();
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

    $(".keyboard li .emptypsw").click(function () {
        emptypsw();
    });

    showPasswordPopup();
}