$(function () {
    $('#confirm').on('click', function () {
        confirmPop('提醒', '确认已收到该笔还款吗?', '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认',
                onClick: function () {

                    PasswordInit(confirmRepayment);
                }
            }
            ]);
    });

    $('#canncel').on('click', function () {
        confirmPop('提醒', '确认未收到该笔还款吗?', '',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确认未收到',
                onClick: function () {
                    PasswordInit(cancelRepayment);
                }
            }
            ]);
    });

    function confirmRepayment(password) {
        $.showLoading('提交中...');
        var repaymentId = $('#repaymentId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "rcpt/repayment/" + repaymentId,
            dataType: 'json',
            data: {pass: password, status: 2},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('确认成功', function () {
                        location.href = baseURL + "rcpt/repayment_repay" + pageExt;
                    });

                } else {
                    $.toptip(r.msg);
                }
            }
        });
    }

    function cancelRepayment(password) {
        var repaymentId = $('#repaymentId').val();
        var txId = $('#txId').val();
        $.showLoading('提交中...');
        $.ajax({
            type: "PATCH",
            url: baseURL + "rcpt/repayment/" + repaymentId,
            dataType: 'json',
            data: {pass: password, status: 4},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('确认成功', function () {
                        location.href = baseURL + "rcpt/repayment/repay/" + txId + pageExt;
                    });
                } else {
                    $.toptip(r.msg);
                }
            }
        });
    }


});