$(function () {

    $('#resend').on('click', function () {
        location.href = baseURL + 'rcpt/confirm_transaction/' + $('#txUuid').val() + ".html";
    });

    $('#queryBorrowerInformation').on('click', function () {
        location.href = baseURL + 'rcpt/borrower_informationlist/' + $('#borrowerUserId').val() + ".html";
    });

    $('#delete').click(function () {
        confirmPop('提醒', '确认删除该借条吗?', '借条删除后，相关记录也将一并清除, 对方不能确认, 借条不会生效.', [
            {
                label: '不删除',
                onClick: function(){
                }
            }, {
                label: '删除',
                onClick: function(){
                    $.showLoading('提交中...')
                    $.ajax({
                        type: "DELETE",
                        url: baseURL + 'rcpt/transaction/' + $('#txId').val(),
                        success: function(r){
                            $.hideLoading();
                            if(r.code == 0){
                                $.toast('删除成功');
                                location.href = baseURL + 'rcpt/index.html';
                            }else{
                                $.toptip(r.msg, 'error');
                            }
                        }
                    });
                }
            }
        ]);
    });

    $('#confirm').on('click', function () {
        confirmPop('提醒', '确认支付吗?', '该借条需要支付服务费' + $('#feeAmount').val() + '元. ', [
            {
                label: '放弃',
                onClick: function(){
                }
            }, {
                label: '确认支付',
                onClick: function(){
                    $.showLoading('提交中...')
                    $.ajax({
                        type: "POST",
                        url: baseURL + 'rcpt/transaction/pay/' + $('#txId').val(),
                        success: function(r){
                            $.hideLoading();
                            if(r.code == 0){
                                if (r.wxPay) {
                                    var txId = $('#txId').val();
                                    location.href = baseURL + 'in_pay?type=tx&payType=wechat&id=' + txId
                                        + '&returnUrl=' + baseURL + "rcpt/transaction/" + txId + ".html"
                                        + '&successUrl=' + baseURL + "rcpt/confirm_transaction/" + $('#txUuid').val() + ".html";
                                } else {
                                    $.toast('支付成功');
                                    location.href = baseURL + "rcpt/confirm_transaction/" + r.tx.txUuid + ".html";
                                }
                            }else{
                                $.toptip(r.msg, 'error');
                            }
                        }
                    });
                }
            }
        ]);
    });


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
