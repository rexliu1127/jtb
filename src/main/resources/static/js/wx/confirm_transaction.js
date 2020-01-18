$(function () {
    document.querySelector('#confirm').addEventListener('click', function () {
        if (createdByBorrower) {
            weui.confirm('<span class="red">补借条有风险,请谨慎使用.</span> 补借条不能线上走账, 不支持逾期催收, 存在较大风险,请谨慎使用.'
                + (borrowerHasOverdueRecord ? '<br/><span class="red">该用户存在逾期信息, 可点击查看.</span>' : ''), {
                title: '提醒',
                buttons: [{
                    label: borrowerHasOverdueRecord ? '查看' : '取消',
                    type: 'default',
                    onClick: function () {
                        if (borrowerHasOverdueRecord) {
                            location.href = $('#creditLink').attr('href');
                        }
                    }
                }, {
                    label: '知道风险',
                    type: 'primary',
                    onClick: function () {
                        var txUuid = $('#txUuid').val();
                        if (!bindedUser) {
                            location.href = baseURL + 'wx/bind_bank_page?returnUrl=wx/confirm_transaction/'
                                + txUuid
                        } else {
                            $("#keyboardDIV").show();
                            $("#password_mask").show();
                            PassWordInit(confirmTransacttion);
                        }
                    }
                }]
            });
        } else if (createdByLender) {
            var feeAmount = $('#feeAmount').val();

            weui.confirm('<span class="red">补借条有风险,请谨慎使用.</span> 补借条不能线上走账, 不支持逾期催收, 存在较大风险,请谨慎使用. '
                + (feeAmount > 0 ? '该借条需要支付服务费' + feeAmount + '元. 确认并支付吗?' : ''),
                {
                title: '提醒',
                buttons: [{
                    label: '取消',
                    type: 'default',
                    onClick: function () {
                    }
                }, {
                    label: '知道风险并支付',
                    type: 'primary',
                    onClick: function () {
                        var txUuid = $('#txUuid').val();
                        if (!bindedUser) {
                            location.href = baseURL + 'wx/bind_bank_page?returnUrl=wx/confirm_transaction/'
                                + txUuid
                        } else {
                            $("#keyboardDIV").show();
                            $("#password_mask").show();
                            PassWordInit(confirmTransacttionAndPay);
                        }
                    }
                }]
            });
        }

    });

    document.querySelector('#canncel').addEventListener('click', function () {
        weui.confirm('确认驳回该借条吗?', {
            title: '提醒',
            buttons: [{
                label: '不驳回',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '驳回',
                type: 'primary',
                onClick: function () {
                    var txUuid = $('#txUuid').val();

                    if (!bindedUser) {
                        location.href = baseURL + 'wx/bind_bank_page?returnUrl=wx/confirm_transaction/'
                            + txUuid + '&txUuid=' + txUuid;
                    } else {
                        $("#keyboardDIV").show();
                        $("#password_mask").show();
                        PassWordInit(cancelTransaction);
                    }
                }
            }]
        });
    });

    function confirmTransacttion(password) {
        var loading = weui.loading('提交中...');
        var txUuid = $('#txUuid').val();
        $.ajax({
            type: "POST",
            url: baseURL + "wx/transaction/confirm/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('确认成功', 3000);
                    location.href = baseURL + "wx/main";
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

    function confirmTransacttionAndPay(password) {
        var loading = weui.loading('提交中...');
        var txUuid = $('#txUuid').val();
        var txId = $('#txId').val();
        $.ajax({
            type: "POST",
            url: baseURL + "wx/transaction/confirm/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    if (r.wxPay) {
                        location.href = baseURL + 'in_pay?type=tx&id=' + txId
                            + '&returnUrl=' + baseURL + "wx/confirm_transaction/" + txUuid
                            + '&successUrl=' + baseURL + 'wx/main';
                    } else {
                        weui.toast('确认成功', 3000);
                        location.href = baseURL + "wx/main";
                    }
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

    function cancelTransaction(password) {
        var txUuid = $('#txUuid').val();
        var loading = weui.loading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "wx/transaction/cancel/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('驳回成功', 3000);
                    location.href = baseURL + "wx/main";
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

});
