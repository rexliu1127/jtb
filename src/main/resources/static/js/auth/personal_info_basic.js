$(function () {
    $('#save').on('click', function () {
        var name = $('#name').val();
        var idNo = $('#idNo').val();

        var wechatNo = $('#wechatNo').val();
        var sesamePoints=$('#sesamePoints').val();

        if (!name) {
            $.toptip('请输入姓名', 'error');
            return;
        }
        if (!idNo || (idNo.length !== 18 && idNo.length !== 15)) {
            $.toptip('请输入正确的身份证号码', 'error');
            return;
        }

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/personal_info",
            dataType: 'json',
            data: {name: name, idNo: idNo, wechatNo: wechatNo, sesamePoints:sesamePoints,index:1},
            success: function(r){
                $.hideLoading();
                if(r.code == 0) {
                    $.toast("保存成功!", function () {
                        location.href = baseURL + 'auth/personal_info_s2.html';
                    });
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });

});

