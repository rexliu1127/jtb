$(function () {
    $('.reportBTN').on('click', function () {
        confirmPop('提醒', '销账之后该借条不可恢复, 且默认借款已还款.', '确认销账吗?',
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

    function confirmRepayment(password) {
        $.showLoading('提交中...');
        var txId = $('#txId').val();
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/repayment/complete/" + txId,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                $.hideLoading();
                if (r.code == 0) {
                    $.toast('销账成功', function () {
                        location.href = baseURL + "rcpt/repayment_repay" + pageExt;
                    });
                } else {
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

});
