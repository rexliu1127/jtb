$(function () {
    document.querySelector('#repayTxBtn').addEventListener('click', function () {
        weui.confirm('销账之后该借条不可恢复, 且默认借款已还款, 确认销账吗?', {
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
                    PassWordInit(confirmRepayment);
                }
            }]
        });

    });

    function confirmRepayment(password) {
        var loading = weui.loading('提交中...');
        var txId = $('#txId').val();
        $.ajax({
            type: "POST",
            url: baseURL + "wx/repayment/complete/" + txId,
            dataType: 'json',
            data: {password: password},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('销账成功', 3000);
                    location.href = baseURL + "wx/repayment_repay";
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

});