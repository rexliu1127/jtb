$('#amount').on('blur', function() {
    var amount = $('#amount').val();
    var feeAmount = calFeeAmount(amount);
    $('#feeInfo').text(feeAmount);
});

function calFeeAmount(amount) {
    var feeAmount = 0;
    if (amount && amount > 0) {
            var feeRate = $('#feeRate').val();
            var maxFee = $('#maxFee').val();
            var minFee = $('#minFee').val();

            feeAmount = (amount * feeRate).toFixed(2);
            if (minFee > 0 && feeAmount < minFee) {
                feeAmount = minFee;
            }
            if (maxFee > 0 && feeAmount > maxFee) {
                feeAmount = maxFee;
            }

     }
     return feeAmount;
};

function submitWithdrawal(password) {
    $.ajax({
                type: "POST",
                url: baseURL + "rcpt/withdraw",
                dataType: 'json',
                data: {amount: $('#amount').val(), password: password},
                success: function(r){
                    $.hideLoading();
                    if(r.code == 0){
                        $.toast('提交成功', function() {
                            location.href = baseURL + 'rcpt/member.html'
                        });
                    }else{
                        $.toptip(r.msg, 'error');
                    }
                }
            });
};


$('#submit').on('click', function() {
        var amount = $('#amount').val();
        if (!amount) {
            $.toptip('请输入提现金额', 'error');
            return;
        }
        amount = parseFloat(amount);
        if (amount <= 0) {
            $.toptip('提现金额必须大于0', 'error');
            return;
        }

        var minAmount = parseFloat($('#minAmount').val());
        if (amount < minAmount) {
            $.toptip('最小提现金额为' + minAmount + '元', 'error');
            return;
        }

        var balance = parseFloat($('#balance').val());
        if (amount > balance) {
            $.toptip('余额不足', 'error');
            return;
        }

        confirmPop('提醒', '确认发起提现吗?', '提现' + amount + '元.',
                    [{
                        label: '取消',
                        onClick: function () {
                        }
                    }, {
                        label: '确认',
                        onClick: function () {
                            PasswordInit(submitWithdrawal);
                        }
                    }
                    ]);

    });

$(function(){
});
