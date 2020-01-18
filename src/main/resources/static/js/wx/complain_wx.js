var imageCount = 0;


var images = {
    localId: [],
    serverId: []
};

var chooseImage = function () {
    wx.chooseImage({
        count:1,
        sizeType: ['original', 'compressed'], //建议压缩图
        sourceType: ['album', 'camera'], // 来源是相册、相机
        success: function (res) {
            uploadImage(res.localIds[0]);
        }
    });
};

var uploadImage = function (localId) {
    if (!localId) {
        return;
    }
    $.showLoading("请稍等...");
    wx.uploadImage({
        localId: localId,
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
            //res.serverId 返回图片的微信服务器端ID
            downloadToServer(res.serverId, localId);
        }
    });
};

var downloadToServer = function(serverId, localId) {
    $.ajax({
        data: {serverId:serverId, "name": 'tx_' + userId + '_'},
        type : "POST",
        url : baseURL + "uploadWxImage",
        success : function(r) {
            $.hideLoading();
            if (r) {
                if(r.code == 0){
                    imageCount = imageCount + 1;
                    var seq = imageCount;

                    $('#idNoUrl' + seq).val(r.path);
                    $('#uploaderFiles').append(
                        '<li class="weui-uploader__file" id="imagePathLi' + seq + '" data-seq="' + seq
                        + '" style="background-image: url(../userimg/' + r.path + ')"' +
                        ' onclick="viewImage(event)"></li>');

                    $( "body" ).append('<input type="hidden" id="imagePath' + seq + '" name="imagePath" value="' + r.path + '"/>');

                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        }
    });
};

$(function () {
    $('#submit').on('click', function () {
        var remark = $('#remark').val();

        if (!remark) {
            $.toptip('请填写申诉描述', 'error');
            return;
        }

        var imagePaths = $("input[name='imagePath']").map(function(){return $(this).val();}).get();
        if (!imagePaths || imagePaths.length < 3) {
            $.toptip('请上传至少3张证据图片', 'error');
            return;
        }

        var complainType = $('input[name=complainType]:checked', '#txForm').val();

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/complain",
            dataType: 'json',
            data: {txId: $('#txId').val(), complainType: complainType, remark: remark, imagePath: imagePaths.join()},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();

                    $.toast('提交成功', function () {
                        location.href = baseURL + 'wx/main';
                    });
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
    $('#imagePathLi' + id).remove();
    $('#imagePath' + id).remove();
    $('#galleryImgDiv').hide();
}

function closeGallery() {
    $('#galleryImgDiv').hide();
}
