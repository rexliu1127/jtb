$(function () {
    $('#save').on('click', function () {
        var name = $('#name').val();
        var idNo = $('#idNo').val();

        if (!name) {
            $.toptip('请输入姓名', 'error');
            return;
        }
        if (!idNo || (idNo.length !== 18 && idNo.length !== 15)) {
            $.toptip('请输入正确的身份证号码', 'error');
            return;
        }

        var companyName = $('#companyName').val();
        var companyAddr = $('#companyAddr').val();
        var companyTel = $('#companyTel').val();
        var companyJob=$('#companyJob').val();
        var salary=$('#salary').val();

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/personal_info",
            dataType: 'json',
            data: {name: name, idNo: idNo, companyName: companyName, companyAddr: companyAddr, companyTel: companyTel,
                companyJob:companyJob,salary:salary,index:2},
            success: function(r) {
                $.hideLoading();
                if(r.code == 0) {
                    $.toast("保存成功!", function () {
                        location.href = baseURL + 'auth/apply.html';
                    });
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });
});

