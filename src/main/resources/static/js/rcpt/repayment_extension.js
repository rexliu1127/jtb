$(function () {
    $('#confirmExtension').on('click', function () {
        var feeAmount = $('#extensionFee').val();

        var confirmMsg = '确认该笔借款展期吗?';
        if (feeAmount > 0) {
            confirmMsg = '该笔展期需要支付' + feeAmount + '元, ' + confirmMsg;
        }

        confirmPop('提醒', confirmMsg, '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认',
                onClick: function () {
                    PasswordInit(confirmExtension);
                }
            }
            ]);

    });

    $('#rejectExtension').on('click', function () {
        confirmPop('提醒', '拒绝该笔借款展期吗?', '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认',
                onClick: function () {
                    PasswordInit(rejectExtension);
                }
            }
            ]);
    });



    $('#cancelExtension').on('click', function () {
        confirmPop('提醒', '取消该笔借款展期吗?', '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认',
                onClick: function () {
                    PasswordInit(cancelExtension);
                }
            }
            ]);
    });

    function confirmExtension(password) {
        $.showLoading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "rcpt/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 2},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    var extensionId = $('#extensionId').val();
                    var txId = $('#txId').val();
                    if (r.wxPay) {
                        location.href = baseURL + 'in_pay?type=ex&id=' + extensionId
                            + '&returnUrl=' + baseURL + "rcpt/repayment/extension/" + txId + pageExt
                            + '&successUrl=' + baseURL + "rcpt/repayment_to_pay_plan/" + txId + pageExt;
                    } else {
                        $.toast('确认成功', function () {
                            location = baseURL + "rcpt/repayment/extension/" + txId + pageExt;
                        });
                    }
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    function cancelExtension(password) {
        $.showLoading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "rcpt/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 3},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('取消成功', function () {
                        location = location;
                    });
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }


    function rejectExtension(password) {
        $.showLoading('提交中...');
        var extensionId = $('#extensionId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "rcpt/repayment/extension/" + extensionId,
            dataType: 'json',
            data: {password: password, status: 4},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('拒绝成功', function () {
                        location = location;
                    });
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }
});