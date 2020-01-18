var imageCount = 0;
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

    $("input[type='file']").on('change', function () {
        var self = this;
        var fileName = $(this).attr("name");

        var file = self.files[0];
        var r = new FileReader();
        r.readAsDataURL(file);
        $(r).load(function () {
            var fileStream = this.result;//base64图片流
            var URL = window.URL || window.webkitURL,
                canvas = document.createElement('canvas'),
                ctx = canvas.getContext('2d');
            if (URL && File && ctx) {
                $.showLoading("处理中...");
                var fileURL = URL.createObjectURL(file),
                    img = new Image();
                img.src = fileURL;
                img.onload = function () {
                    var orientation;
                    EXIF.getData(img, function () {
                        orientation = EXIF.getTag(this, "Orientation");
                        var degree = 0, drawWidth = img.width, drawHeight = img.height, width, height;
                        //以下改变一下图片大小
                        var maxSide = Math.max(drawWidth, drawHeight);
                        var tarSize = 1600;
                        if (maxSide > tarSize) {
                            var minSide = Math.min(drawWidth, drawHeight);
                            minSide = minSide / maxSide * tarSize;
                            maxSide = tarSize;
                            if (drawWidth > drawHeight) {
                                drawWidth = maxSide;
                                drawHeight = minSide;
                            } else {
                                drawWidth = minSide;
                                drawHeight = maxSide;
                            }
                        }
                        canvas.width = width = drawWidth;
                        canvas.height = height = drawHeight;
                        switch (orientation) {//横屏竖屏转化
                            //横屏拍摄，此时home键在左侧
                            case 3:
                                degree = 180;
                                drawWidth = -width;
                                drawHeight = -height;
                                break;
                            //竖屏拍摄，此时home键在下方(正常拿手机的方向)
                            case 6:
                                canvas.width = height;
                                canvas.height = width;
                                degree = 90;
                                drawWidth = width;
                                drawHeight = -height;
                                break;
                            //竖屏拍摄，此时home键在上方
                            case 8:
                                canvas.width = height;
                                canvas.height = width;
                                degree = 270;
                                drawWidth = -width;
                                drawHeight = height;
                                break;
                        }
                        //使用canvas旋转校正
                        ctx.rotate(degree * Math.PI / 180);
                        ctx.drawImage(img, 0, 0, drawWidth, drawHeight);
                        var base64 = canvas.toDataURL('image/jpeg');
                        //上传图片
                        uploadFile(fileName, base64, fileStream);
                        canvas = null;
                        img = null;
                    });
                }
                $.hideLoading();
            } else {
                uploadFile(fileName, fileStream, fileStream);
            }

        });
    });

    function uploadFile(fileName, compact, original) {
        $.showLoading("加载中...");

        // $('#save').hide();

        $.post(baseURL + 'uploadBase64',
            {file:compact.substr(23),fileName:fileName},
            function(r){
                $.hideLoading();
                // $('#save').show();
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
            });//上传图片
    }
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
