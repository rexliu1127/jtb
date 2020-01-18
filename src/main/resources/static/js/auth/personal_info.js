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

        var companyJob=$('#companyJob').val();
        var salary=$('#salary').val();
        var sesamePoints=$('#sesamePoints').val();

        if (!name) {
            $.toptip('请输入姓名', 'error');
            return;
        }
        if (!idNo || (idNo.length !== 18 && idNo.length !== 15)) {
            $.toptip('请输入正确的身份证号码', 'error');
            return;
        }

        // if(!qqNo){
        //     $.toptip('请输入QQ号码', 'error');
        //     return;
        // }
        //
        // if(!wechatNo){
        //     $.toptip('请输入微信号码', 'error');
        //     return;
        // }
        //
        // if(!companyName){
        //     $.toptip('请输入公司名称', 'error');
        //     return;
        // }
        //
        // if(!companyJob){
        //     $.toptip('请输入公司职位', 'error');
        //     return;
        // }
        //
        // if(!salary){
        //     $.toptip('请输入薪资', 'error');
        //     return;
        // }
        //
        // if(!sesamePoints){
        //     $.toptip('请输入芝麻分', 'error');
        //     return;
        // }
        //
        // if(!companyAddr){
        //     $.toptip('请输入公司地址', 'error');
        //     return;
        // }

        // if(!companyTel){
        //     $.toptip('请输入公司联系方式', 'error');
        //     return;
        // }
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

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/personal_info",
            dataType: 'json',
            data: {name: name, idNo: idNo, qqNo: qqNo, idUrl1: idUrl1, idUrl2: idUrl2, idUrl3: idUrl3,
                wechatNo: wechatNo, companyName: companyName, companyAddr: companyAddr, companyTel: companyTel,
                companyJob:companyJob,salary:salary,sesamePoints:sesamePoints},
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    $.toast("保存成功!", function () {
                        location.href = baseURL + 'auth/apply.html';
                    });
                }else{
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

        var seq = fileName.substr(fileName.length - 1);
        $('#save').hide();
        $('#idUrl' + seq + 'Files').empty();
        $('#idNoUrl' + seq).val('');

        $.post(baseURL + 'uploadBase64',
            {file:compact.substr(23),name:fileName},
            function(r){
                $.hideLoading();
                $('#save').show();
                if(r.code == 0){
                    $('#idNoUrl' + seq).val(r.path);

                    $('#idUrl' + seq + 'Files').append(
                        '<li class="weui-uploader__file" data-seq="' + seq
                        + '" style="background-image: url(../userimg/' + r.path + ')"' +
                        ' onclick="showImage(event)"></li>');

                }else{
                    $.toptip(r.msg, 'error');
                }
            });//上传图片
    }

});

function showImage(event) {
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
