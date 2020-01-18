$(function () {
    var newEndDateEle = $('#newEndDate');
    var rateEle = $("#rate");
    newEndDateEle.calendar({
        value: [newEndDateEle.val()],
        dateFormat: 'yyyy-mm-dd'
    });

    rateEle.select({
        title: "选择利率",
        input: rateEle.val(),
        items: ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"]
    });

    $('#createExtension').on('click', function () {
        var day = daydiff(parseDate($('#newEndDate').val()), parseDate($('#beginDate').val()));
        if (day >= 0) {
            $.toptip('展期日期必须大于开始日期!', 'error');
            return;
        }

        day = daydiff(parseDate($('#endDate').val()), parseDate($('#newEndDate').val()));
        if (day > 30) {
            $.toptip('每次展期不能超过30天!', 'error');
            return;
        }

        $.modal({
            title: "提醒",
            text: '确定向借款人提出展期吗 (需要对方确认后后效)?',
            buttons: [
                { text: "取消", className: "default"},
                { text: "确定", className: "primary", onClick: function(){
                        $("#keyboardDIV").show();
                        $("#password_mask").show();
                        PassWordInit(createExtension);
                    }}
            ]
        });
    });

    function createExtension(password) {
        $.showLoading('提交中...');
        var rate = rateEle.val();
        var newEndDate = newEndDateEle.val();
        $.ajax({
            type: "POST",
            url: baseURL + "wx/repayment/extension/" + $('#txId').val(),
            dataType: 'json',
            data: {password: password, rate: rate, newEndDate: newEndDate},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    $.toast('提交成功');
                    location.href = baseURL + "wx/repayment_repay";
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }
});