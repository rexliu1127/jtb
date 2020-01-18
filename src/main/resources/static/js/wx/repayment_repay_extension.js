$(function () {
    $('#confirmExtension').on('click', function () {
        var feeAmount = $('#extensionFee').val();

        var confirmMsg = '确认该笔借款展期吗?';
        if (feeAmount > 0) {
            confirmMsg = '该笔展期需要支付' + feeAmount + '元, ' + confirmMsg;
        }
        weui.confirm(confirmMsg, {
            title: '提醒',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '确认',
                type: 'primary',
                onClick: function () {
                    $("#keyboardDIV").show();
                    $("#password_mask").show();
                    PassWordInit(confirmExtension);
                }
            }]
        });

    });


    $('#rejectExtension').on('click', function () {
        weui.confirm('拒绝该笔借款展期吗?', {
            title: '提醒',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '确认',
                type: 'primary',
                onClick: function () {
                    $("#keyboardDIV").show();
                    $("#password_mask").show();
                    PassWordInit(rejectExtension);
                }
            }]
        });

    });



    $('#cancelExtension').on('click', function () {
        weui.confirm('取消该笔借款展期吗?', {
            title: '提醒',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '确认',
                type: 'primary',
                onClick: function () {
                    $("#keyboardDIV").show();
                    $("#password_mask").show();
                    PassWordInit(cancelExtension);
                }
            }]
        });

    });

    function confirmExtension(password) {
        var loading = weui.loading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "wx/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 2},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();

                    var extensionId = $('#extensionId').val();
                    var txId = $('#txId').val();
                    if (r.wxPay) {
                        location.href = baseURL + 'in_pay?type=ex&id=' + extensionId
                            + '&returnUrl=' + baseURL + "wx/repayment/extension/" + txId
                            + '&successUrl=' + baseURL + "wx/repayment_to_pay_plan/" + txId;
                    } else {
                        weui.toast('确认成功', 1000);
                        location = baseURL + "wx/repayment/extension/" + txId;
                    }
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

    function cancelExtension(password) {
        var loading = weui.loading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "wx/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 3},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('取消成功', 3000);
                    location = location;
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }


    function rejectExtension(password) {
        var loading = weui.loading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "wx/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 4},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('拒绝成功', 3000);
                    location = location;
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }
});