$(function () {
    $('#confirm').click(function () {
        if (createdByBorrower) {
            confirmPop('提醒', '补借条有风险,请谨慎使用. 确认该借条吗?', '补借条不能线上走账, 不支持逾期催收, 存在较大风险,请谨慎使用.'
                + (borrowerHasOverdueRecord ? '该用户存在逾期信息, 可点击查看.' : ''),
                [{
                    label: borrowerHasOverdueRecord ? '查看' : '取消',
                    onClick: function () {
                        if (borrowerHasOverdueRecord) {
                            location.href = $('#creditLink').attr('href');
                        }
                    }
                }, {
                    label: '知道风险',
                    onClick: function () {
                        var txUuid = $('#txUuid').val();
                        if (!bindedUser) {
                            location.href = baseURL + 'rcpt/bind_bank.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else if (!hasPassword) {
                            location.href = baseURL + 'rcpt/set_password.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else if (!userSigned) {
                            location.href = baseURL + 'rcpt/member/sign.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else {
                            PasswordInit(confirmTransacttion);
                        }
                    }
                }
                ]);

        } else if (createdByLender) {
            var feeAmount = $('#feeAmount').val();

            confirmPop('提醒', '补借条有风险,请谨慎使用. 确认该借条吗?', '补借条不能线上走账, 不支持逾期催收, 存在较大风险,请谨慎使用.'
                + (feeAmount > 0 ? '该借条需要支付服务费' + feeAmount + '元. 确认并支付吗?' : ''),
                [{
                    label: '取消',
                    onClick: function () {
                    }
                }, {
                    label: feeAmount > 0 ? '确认及支付' : '确认',
                    onClick: function () {
                        var txUuid = $('#txUuid').val();
                        if (!bindedUser) {
                            location.href = baseURL + 'rcpt/bind_bank.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else if (!hasPassword) {
                            location.href = baseURL + 'rcpt/set_password.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else if (!userSigned) {
                            location.href = baseURL + 'rcpt/member/sign.html?returnUrl=rcpt/confirm_transaction/'
                                + txUuid + '.html';
                        } else {
                            PasswordInit(confirmTransacttionAndPay);
                        }
                    }
                }
                ]);
        }

    });

    $('#canncel').click(function () {
        confirmPop('提醒', '确认驳回该借条吗?', '',
            [{
                label: '不驳回',
                onClick: function () {
                }
            }, {
                label: '驳回',
                onClick: function () {
                    var txUuid = $('#txUuid').val();

                    if (!bindedUser) {
                        location.href = baseURL + 'rcpt/bind_bank.html?returnUrl=rcpt/confirm_transaction/'
                            + txUuid + '.html';
                    } else {
                        PasswordInit(cancelTransaction);
                    }
                }
            }
            ]);

    });

    function confirmTransacttion(password) {
        $.showLoading('提交中...');
        var txUuid = $('#txUuid').val();
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/transaction/confirm/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('确认成功');
                    location.href = baseURL + "rcpt/index.html";
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    function confirmTransacttionAndPay(password) {
        $.showLoading('提交中...');
        var txUuid = $('#txUuid').val();
        var txId = $('#txId').val();
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/transaction/confirm/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    if (r.wxPay) {
                        location.href = baseURL + 'in_pay?type=tx&id=' + txId
                            + '&returnUrl=' + baseURL + "rcpt/confirm_transaction/" + txUuid + '.html'
                            + '&successUrl=' + baseURL + 'rcpt/index.html';
                    } else {
                        $.toast('确认成功', function() {
                            location.href = baseURL + "rcpt/index.html";
                        });
                    }
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    function cancelTransaction(password) {
        var txUuid = $('#txUuid').val();
        $.showLoading('提交中...');
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/transaction/cancel/" + txUuid,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('驳回成功', function() {
                        location.href = baseURL + "rcpt/index.html";
                    });
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    var hidePasswordPopup = function () {
        $(".bounced-R").removeClass("modal-active");
        setTimeout(function () {
            $(".mask-active").removeClass("mask-active");
            $(".mask").remove();
            emptypsw();
        }, 300);
    }

    var showPasswordPopup = function () {
        $(".bounced-R").addClass("modal-active");
        if ($(".mask").length > 0) {
            $(".mask").addClass("mask-active");
        } else {
            $("body").append('<div class="mask"></div>');
            $(".mask").addClass("mask-active");
        }

        $(".mask-active,.bouncedAncel").click(hidePasswordPopup);
    };

    //----
    var i = 0;

    $(".keyboard li .numpsw").click(function () {
        if (i < 6) {
            $(".sixpsw li").eq(i).addClass("mmdd");
            $(".sixpsw li").eq(i).attr("data", $(this).text());
            i++
            if (i == 6) {
                setTimeout(function () {
                    var data = "";
                    $(".sixpsw li").each(function () {
                        data += $(this).attr("data");
                    });


                    applyTransaction(data);

                    hidePasswordPopup();
                }, 100);
            }
        }
    });

    $(".keyboard li .delpsw").click(function () {
        if (i > 0) {
            i--
            $(".sixpsw li").eq(i).removeClass("mmdd");
            $(".sixpsw li").eq(i).attr("data", "");
        }
    });

    function emptypsw() {
        $(".sixpsw li").removeClass("mmdd");
        $(".sixpsw li").attr("data", "");
        i = 0;
    };

    $(".keyboard li .emptypsw").click(function () {
        emptypsw();
    });

});
