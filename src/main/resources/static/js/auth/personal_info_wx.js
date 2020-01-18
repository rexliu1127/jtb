
var images = {
    localId: [],
    serverId: []
};

var chooseImage = function () {
    var seq = $(this).attr('data-id');
    wx.chooseImage({
        count:1,
        sizeType: ['original', 'compressed'], //建议压缩图
        sourceType: ['album', 'camera'], // 来源是相册、相机
        success: function (res) {
            uploadImage(res.localIds[0], seq);
        }
    });
};

var uploadImage = function (localId, seq) {
    if (!localId) {
        return;
    }
    $.showLoading("请稍等...");
    wx.uploadImage({
        localId: localId,
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
            //res.serverId 返回图片的微信服务器端ID
            downloadToServer(res.serverId, localId, seq);
        }
    });
};

var downloadToServer = function(serverId, localId, seq) {
    $('#save').hide();
    $.ajax({
        data: {serverId:serverId, "name": userId + "_id" + seq + '_'},
        type : "POST",
        url : baseURL + "uploadWxImage",
        success : function(r) {
            $('#save').show();
            if (r) {
                $.hideLoading();
                var filesDiv = $('#idUrl' + seq + 'Files');
                if(r.code == 0){
                    filesDiv.empty();

                    $('#idNoUrl' + seq).val(r.path);

                    filesDiv.append(
                        '<li class="weui-uploader__file" data-seq="' + seq
                        + '" style="background-image: url(../userimg/' + r.path + ');"' +
                        ' onclick="viewImage(event)"></li>');

                }else{
                    filesDiv.empty();
                    $.toptip(r.msg, 'error');
                }
            }
        }
    });
};

$(function () {
    $('#save').on('click', function () {
        var name = $('#name').val();
        var idNo = $('#idNo').val();
        var qqNo = $('#qqNo').val();

        var wechatNo = $('#wechatNo').val();
        var companyName = $('#companyName').val();
        var companyAddr = $('#companyAddr').val();
        var companyTel = $('#companyTel').val();

        var idUrl1 = $('#idNoUrl1').val();
        var idUrl2 = $('#idNoUrl2').val();
        var idUrl3 = $('#idNoUrl3').val();

        if (!name) {
            $.toptip('请输入姓名', 'error');
            return;
        }
        if (!idNo || (idNo.length !== 18 && idNo.length !== 15)) {
            $.toptip('请输入正确的身份证号码', 'error');
            return;
        }

        if (!idUrl1) {
            $.toptip('请上传身份证正面照片', 'error');
            return;
        }
        if (!idUrl2) {
            $.toptip('请上传身份证反面照片', 'error');
            return;
        }
        if (!idUrl3) {
            $.toptip('请上传手持身份证照片', 'error');
            return;
        }

        $.showLoading('加载中...');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/personal_info",
            dataType: 'json',
            data: {name: name, idNo: idNo, qqNo: qqNo, idUrl1: idUrl1, idUrl2: idUrl2, idUrl3: idUrl3,
                wechatNo: wechatNo, companyName: companyName, companyAddr: companyAddr, companyTel: companyTel},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    if (action != 'null') {
                        location.href = baseURL + 'auth/apply.html';
                    } else {
                        location.href = baseURL + 'auth/contact_info.html';
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });
});

function viewImage(event) {
    var target = $(event.target);
    var seq = target.attr('data-seq');
    var ele = $('#galleryImg');
    ele.attr('data-seq', seq);
    ele.css('background-image', target.css('background-image'));
    $('#galleryImgDiv').show();
}

function deleteImage() {
    var ele = $('#galleryImg');
    var id = ele.attr('data-seq');
    $('#idUrl' + id + 'Files').empty();
    $('#idNoUrl' + id).val('');
    $('#galleryImgDiv').hide();
}

function closeGallery() {
    $('#galleryImgDiv').hide();
}
