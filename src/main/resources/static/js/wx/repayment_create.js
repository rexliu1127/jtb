var repaymentType = new Array();
repaymentType[0] = '';
repaymentType[1] = '支付宝';
repaymentType[2] = '微信';
repaymentType[3] = '银行';
repaymentType[4] = '现金';

$(function () {
    $("#repaymentTypeLabel").select({
        title: "选择还款方式",
        input: repaymentType[4],
        items: [{
            title: repaymentType[1],
            value: 1
        }, {
            title: repaymentType[2],
            value: 2
        }, {
            title: repaymentType[3],
            value: 3
        }, {
            title: repaymentType[4],
            value: 4
        }],
        onChange: function (v) {
            $('#repaymentType').val(v.values);
            $("#repaymentTypeLabel").text(v.titles);
        }
    });

    $('#createRepayment').on('click', function () {

        $.modal({
            title: "提醒",
            text: "确定已经线下还款 (需要对方确认后后效)?",
            buttons: [
                { text: "取消", className: "default"},
                { text: "确定", className: "primary", onClick: function(){
                        $("#keyboardDIV").show();
                        $("#password_mask").show();
                        PassWordInit(createRepayment);
                    } }
            ]
        });
    });

    function createRepayment(password) {
        $.showLoading('提交中...')
        var repaymentType = $('#repaymentType').val();
        $.ajax({
            type: "POST",
            url: baseURL + "wx/repayment/" + $('#txId').val(),
            dataType: 'json',
            data: {password: password, repaymentType: repaymentType},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    $.toast('提交成功');
                    location.href = baseURL + "wx/repayment_to_pay";
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }
});