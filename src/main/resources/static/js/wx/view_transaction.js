$(function () {

    $('#resend').on('click', function () {
        location.href = baseURL + 'rcpt/confirm_transaction/' + $('#txUuid').val() + ".html";
    });

    $('#delete').on('click', function () {
        weui.confirm('借条删除后，相关记录也将一并清除, 对方不能确认, 借条不会生效. 确认删除该借条吗?', {
            title: '提醒',
            buttons: [{
                label: '不删除',
                type: 'default',
                onClick: function(){
                }
            }, {
                label: '删除',
                type: 'primary',
                onClick: function(){
                    var loading = weui.loading('提交中...');
                    $.ajax({
                        type: "DELETE",
                        url: baseURL + 'rcpt/transaction/' + $('#txId').val(),
                        success: function(r){
                            if(r.code == 0){
                                loading.hide();
                                weui.toast('删除成功', 3000);
                                location.href = baseURL + 'rcpt/index.html';
                            }else{
                                loading.hide();
                                weui.topTips(r.msg);
                            }
                        }
                    });
                }
            }]
        });
    });

    $('#confirm').on('click', function () {
        weui.confirm('该借条需要支付服务费' + $('#feeAmount').val() + '元. 确认支付吗?', {
            title: '支付',
            buttons: [{
                label: '放弃支付',
                type: 'default',
                onClick: function(){
                }
            }, {
                label: '确认支付',
                type: 'primary',
                onClick: function(){
                    var loading = weui.loading('提交中...');
                    $.ajax({
                        type: "POST",
                        url: baseURL + 'rcpt/transaction/pay/' + $('#txId').val(),
                        success: function(r){
                            loading.hide();
                            if(r.code == 0){
                                if (r.wxPay) {
                                    var txId = $('#txId').val();
                                    location.href = baseURL + 'in_pay?type=tx&id=' + txId
                                        + '&returnUrl=' + baseURL + "rcpt/transaction/" + txId + ".html"
                                        + '&successUrl=' + baseURL + "rcpt/confirm_transaction/" + $('#txUuid').val() + ".html";
                                } else {
                                    weui.toast('支付成功', 3000);
                                    location.href = baseURL + "wx/confirm_transaction/" + r.tx.txUuid + ".html";
                                }
                            }else{
                                weui.topTips(r.msg);
                            }
                        }
                    });

                }
            }]
        });
    });
});
