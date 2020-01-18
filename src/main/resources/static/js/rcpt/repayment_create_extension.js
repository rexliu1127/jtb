$(function () {
    var newEndDateEle = $('#newEndDate');
    var rateEle = $("#rate");
    newEndDateEle.calendar({
        value: [newEndDateEle.val()],
        dateFormat: 'yyyy-mm-dd'
    });

    // rateEle.select({
    //     title: "选择利率",
    //     input: rateEle.val(),
    //     items: ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"]
    // });

    $('#createExtension').on('click', function () {
        var day = daydiff(parseDate(newEndDateEle.val()), parseDate($('#beginDate').val()));
        if (day >= 0) {
            $.toptip('展期日期必须大于开始日期!', 'error');
            return;
        }

        day = daydiff(parseDate($('#endDate').val()), parseDate($('#newEndDate').val()));
        if (day > 10) {
            $.toptip('每次展期不能超过10天!', 'error');
            return;
        }

        var extendAmountEle = $('#extendAmount');
        if (!extendAmountEle.val()) {
            $.toptip('请输入展期金额!', 'error');
            return;
        }

        confirmPop('提醒', '确定向借款人提出展期吗?', '需要对方确认后后效. 如果展期金额小于待还金额, 表示未展期部分已还清.',
            [{
                label: '取消',
                onClick: function () {
                }
            }, {
                label: '确定',
                onClick: function () {
                    PasswordInit(createExtension);
                }
            }
            ]);

    });

    function createExtension(password) {
        $.showLoading('提交中...');
        var rate = rateEle.val();
        var newEndDate = newEndDateEle.val();
        var extendAmount = $('#extendAmount').val();
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/repayment/extension/" + $('#txId').val(),
            dataType: 'json',
            data: {password: password, rate: rate, newEndDate: newEndDate, extendAmount: extendAmount},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    $.toast('提交成功', function () {
                        location.href = baseURL + "rcpt/repayment_repay.html";
                    });
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }
});