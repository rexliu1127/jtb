$(function () {

    var mobileExp = /^1(3|4|5|6|7|8|9)\d{9}$/;

    $('#save').on('click', function () {
        var contact1Type = $('#contact1Type').val();
        var contact1Name = $('#contact1Name').val();
        var contact1Mobile = $('#contact1Mobile').val();
        var contact2Type = $('#contact2Type').val();
        var contact2Name = $('#contact2Name').val();
        var contact2Mobile = $('#contact2Mobile').val();
        var contact3Type = $('#contact3Type').val();
        var contact3Name = $('#contact3Name').val();
        var contact3Mobile = $('#contact3Mobile').val();

        if (contact1Mobile) {
            contact1Mobile = contact1Mobile.replace(/\D/g,'');
            $('#contact1Mobile').val(contact1Mobile);

            if (contact1Mobile && !mobileExp.test(contact1Mobile)) {
                $.toptip('请输入正确的联系人1电话', 'error');
                return;
            }
        }

        if (contact2Mobile) {
            contact2Mobile = contact2Mobile.replace(/\D/g, '');
            $('#contact2Mobile').val(contact2Mobile);
            if (contact2Mobile && !mobileExp.test(contact2Mobile)) {
                $.toptip('请输入正确的联系人2电话', 'error');
                return;
            }
        }

        if (contact3Mobile) {
            contact3Mobile = contact3Mobile.replace(/\D/g, '');
            $('#contact3Mobile').val(contact3Mobile);

            if (contact3Mobile && !mobileExp.test(contact3Mobile)) {
                $.toptip('请输入正确的联系人3电话', 'error');
                return;
            }
        }

        if (relationCount >= 1 && (!contact1Name || !contact1Mobile)) {
            $.toptip('请输入联系人1姓名和电话', 'error');
            return;
        }

        if (relationCount >= 2 && (!contact2Name || !contact2Mobile)) {
            $.toptip('请输入联系人2姓名和电话', 'error');
            return;
        }

        if (relationCount >= 3 && (!contact3Name || !contact3Mobile)) {
            $.toptip('请输入联系人3姓名和电话', 'error');
            return;
        }

        $.showLoading('加载中');
        $.ajax({
            type: "POST",
            url: baseURL + "auth/contact_info",
            dataType: 'json',
            data: {contact1Type: contact1Type, contact1Name: contact1Name, contact1Mobile: contact1Mobile,
                contact2Type: contact2Type, contact2Name: contact2Name, contact2Mobile: contact2Mobile,
                contact3Type: contact3Type, contact3Name: contact3Name, contact3Mobile: contact3Mobile},
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    $.toast("保存成功", function () {
                        location.href = 'apply.html';
                    });
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });

});