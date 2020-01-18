$(function () {
    document.querySelector('#confirm').addEventListener('click', function () {
        weui.confirm('确认已收到该笔还款吗?', {
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

    document.querySelector('#canncel').addEventListener('click', function () {
        weui.confirm('确认未收到该笔还款吗?', {
            title: '提醒',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                }
            }, {
                label: '确认未收到',
                type: 'primary',
                onClick: function () {
                    $("#keyboardDIV").show();
                    $("#password_mask").show();
                    PassWordInit(cancelRepayment);
                }
            }]
        });
    });

    function confirmRepayment(password) {
        var loading = weui.loading('提交中...');
        var repaymentId = $('#repaymentId').val();
        $.ajax({
            type: "PATCH",
            url: baseURL + "wx/repayment/" + repaymentId,
            dataType: 'json',
            data: {password: password, status: 2},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('确认成功', 3000);
                    location.href = baseURL + "wx/repayment_repay";
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }

    function cancelRepayment(password) {
        var repaymentId = $('#repaymentId').val();
        var txId = $('#txId').val();
        var loading = weui.loading('提交中...');
        $.ajax({
            type: "PATCH",
            url: baseURL + "wx/repayment/" + repaymentId,
            dataType: 'json',
            data: {password: password, status: 4},
            success: function (r) {
                if (r.code == 0) {
                    loading.hide();
                    weui.toast('驳回成功', 3000);
                    location.href = baseURL + "wx/repayment/repay/" + txId;
                } else {
                    loading.hide();
                    weui.topTips(r.msg);
                }
            }
        });
    }


});