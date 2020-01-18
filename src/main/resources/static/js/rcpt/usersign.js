function convertCanvasToImage(canvas) {
    return canvas.toDataURL("image/png");
};

function saveSignImg() {
    confirmPop('提醒', '确认签名正确无误吗?', '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认',
                onClick: function () {
                    PasswordInit(getAndSaveSing);
                }
            }
            ]);
};

function getAndSaveSing() {
    html2canvas(document.querySelector("#editing_area")).then(function(canvas) {
        $.showLoading("加载中...");

        var imgBase64 = convertCanvasToImage(canvas); //截取图片路径,该路径为服务器参数

        var userId = $('#userId').val();
        var fileName = 'sign/' + (Math.floor(userId / 1000)) + '/' + userId + '_' + new Date().getTime() + '.png';

        $.post(baseURL + 'uploadBase64',
            {file:imgBase64.substr(22), name:fileName, genUrl: false},
            function(r){
                if(r.code == 0){
                    saveUserSign(r.path);
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            });//上传图片
    });
};

function saveUserSign(path) {
    $.post(baseURL + 'rcpt/member/sign',
            {path: path},
            function(r){
                $.hideLoading();
                if(r.code == 0){
                    $.toast("签名保存成功!")
                    var returnUrl = $('#returnUrl').val();
                    if (returnUrl) {
                        setTimeout(function() {
                            location.href = baseURL + returnUrl;
                        }, 1000);
                    } else {
                        setTimeout(function() {
                            location.href = 'setup.html';
                        }, 1000);
                    }
                }else{
                    $.toptip(r.msg, 'error');
                }
    });//上传图片
};

$(function() {
    //初始化
    $(document).esign("canvasEdit", "sign_show", "sign_clear", "sign_ok");
    $('#submitSign').on('click', saveSignImg);
});