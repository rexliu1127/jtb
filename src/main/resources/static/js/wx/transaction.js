//your code here
var usageTypeArr = new Array();
usageTypeArr[0] = '其他';
usageTypeArr[1] = '个体经营';
usageTypeArr[2] = '消费';
usageTypeArr[3] = '助学';
usageTypeArr[4] = '创业';
usageTypeArr[5] = '租房';
usageTypeArr[6] = '旅游';
usageTypeArr[7] = '装修';
usageTypeArr[8] = '医疗';

$(function () {
    // 约定正则
    var regexp = {
        regexp: {
            IDNUM: /(?:^\d{15}$)|(?:^\d{18}$)|^\d{17}[\dXx]$/,
            VCODE: /^.{4}$/
        }
    };

    var beginDateEle = $("#beginDate");
    var endDateEle = $("#endDate");

// 失去焦点时检测
//        weui.form.checkIfBlur('#txForm', regexp);

// 表单提交
    document.querySelector('#formSubmitBtn').addEventListener('click', function () {
           // weui.form.validate('#txForm', function (error) {
           //     console.log(error);
           //     if (!error) {
           //         var loading = weui.loading('提交中...');
           //         setTimeout(function () {
           //             loading.hide();
           //             weui.toast('提交成功', 3000);
           //         }, 1500);
           //     }
           // }, regexp);
//
        var amount = $('#amount').val();
        var beginDate = beginDateEle.val();
        var endDate = endDateEle.val();
        var rate = $('#rate').attr('data-values');
        if (!rate) {
            rate = 0;
        }
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('input[name=usageType]:checked', '#txForm').val();
        var usageRemark = $('#usageRemark').val();
        var type = $('#type').val();

        if ($('input[name="agreement"]:checked').length == 0) {
            $.toptip('请同意借款协议!', 'error');
            return;
        }

        if (!amount) {
            $.toptip('请输入借款金额!', 'error');
            return;
        }
        if (!otherName) {
            $.toptip('请输入对方姓名!', 'error');
            return;
        }

        var day = daydiff(parseDate(beginDateEle.val()), parseDate(endDateEle.val()));
        if (day < 0) {
            $.toptip('结束日期必须大于开始日期!', 'error');
            return;
        }

        if (day > 9) {
            $.toptip('借条期限不能超过10天!', 'error');
            return;
        }

        var tx = {amount: amount, beginDate: beginDate, endDate: endDate,
            rate: rate, otherName: otherName, remark: remark,
            usageType: usageType, usageRemark: usageRemark, type: type};

        $.modal({
            title: "提醒",
            text: "<span class=\"red\">借条是为了已完成的借贷行为补一张借条,做为电子凭证,请确保你们线下交易完成.</span><br/><br/>对方确认后借条立即生效, 要发起该借条吗?",
            buttons: [
                { text: "不发起", className: "default"},
                { text: "发起", onClick: function(){
                        if (!bindedUser) {
                            localStorage.setItem('tx', JSON.stringify(tx));
                            location.href = baseURL + 'wx/bind_bank_page?returnUrl=/wx/apply_transaction_page?type=' + type;
                        } else {
                            $("#keyboardDIV").show();
                            $("#password_mask").show();
                            PassWordInit(applyTransaction);
                        }
                    } }
            ]
        });

    });

    function applyTransaction(password) {
        $.showLoading('提交中...');
        var amount = $('#amount').val();
        var beginDate = $('#beginDate').val();
        var endDate = $('#endDate').val();
        var rate = $('#rate').attr('data-values');
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('input[name=usageType]:checked', '#txForm').val();
        var usageRemark = '';
        var type = $('#type').val();

        var tx = {amount: amount, beginDate: beginDate, endDate: endDate,
            rate: rate, otherName: otherName, remark: remark,
            usageType: usageType, usageRemark: usageRemark, type: type, password: password};
        $.ajax({
            type: "POST",
            url: baseURL + "wx/apply_transaction",
            dataType: 'json',
            data: tx,
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    $.toast('提交成功');

                    if (r.tx.status.value === 5) {
                        location.href = baseURL + 'wx/transaction/' + r.tx.txId;
                        return;
                    }
                    location.href = baseURL + "wx/confirm_transaction/" + r.tx.txUuid;
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    beginDateEle.calendar({
        value: [beginDateEle.val()],
        dateFormat: 'yyyy-mm-dd',
        onClose: function () {
            rebuildRateSummary();
        }
    });

    endDateEle.calendar({
        value: [endDateEle.val()],
        dateFormat: 'yyyy-mm-dd',
        onClose: function () {
            rebuildRateSummary();
        }
    });

    $("#rate").select({
        title: "选择利率",
        input: '24%',
        items: [{
            title: '0%',
            value: 0
        }, {
            title: '1%',
            value: 1
        }, {
            title: '2%',
            value: 2
        }, {
            title: '3%',
            value: 3
        }, {
            title: '4%',
            value: 4
        }, {
            title: '5%',
            value: 5
        }, {
            title: '6%',
            value: 6
        }, {
            title: '7%',
            value: 7
        }, {
            title: '8%',
            value: 8
        }, {
            title: '9%',
            value: 9
        }, {
            title: '10%',
            value: 10
        }, {
            title: '11%',
            value: 11
        }, {
            title: '12%',
            value: 12
        }, {
            title: '13%',
            value: 13
        }, {
            title: '14%',
            value: 14
        }, {
            title: '15%',
            value: 15
        }, {
            title: '16%',
            value: 16
        }, {
            title: '17%',
            value: 17
        }, {
            title: '18%',
            value: 18
        }, {
            title: '19%',
            value: 19
        }, {
            title: '20%',
            value: 20
        }, {
            title: '21%',
            value: 21
        }, {
            title: '22%',
            value: 22
        }, {
            title: '23%',
            value: 23
        }, {
            title: '24%',
            value: 24
        }],
        onClose: function () {
            rebuildRateSummary();
        }
    });

    function rebuildRateSummary() {
        var amount = Number($('#amount').val());

        var rate = $('#rate').attr('data-values');
        if (!rate) {
            rate = 0;
        }
        rate = Number(rate);

        var day = daydiff(parseDate($('#beginDate').val()), parseDate($('#endDate').val()));
        if (day < 0) {
            $.toptip('结束日期必须大于开始日期!', 'error');
            return;
        }

        var interest = (amount * rate * (day + 1) / 36500.0).toFixed(2);
        var total = parseFloat(amount) + parseFloat(interest);
        $('#rateSummary').text('本金' + amount + '+利息' + interest + '=到期本息' + total + '元');
    }

    document.querySelector('#amount').addEventListener('blur', function () {
        rebuildRateSummary();
    });

    document.querySelector('#forgetBtn').addEventListener('click', function () {
        var amount = $('#amount').val();
        var beginDate = $('#beginDate').val();
        var endDate = $('#endDate').val();
        var rate = $('#rate').attr('data-values');
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('input[name=usageType]:checked', '#txForm').val();
        var usageRemark = $('#usageRemark').val();
        var type = $('#type').val();

        var tx = {amount: amount, beginDate: beginDate, endDate: endDate,
            rate: rate, otherName: otherName, remark: remark,
            usageType: usageType, usageRemark: usageRemark, type: type};

        localStorage.setItem('tx', JSON.stringify(tx));
        goForgetPasswordPage();
    });

    function onOffUsageSelect(on) {
        if (on) {
            $('#form').hide();
            $('#form2').show();
        } else {
            $('#form2').hide();
            $('#form').show();
        }
    }

    $('#usageSelect').on('click', function () {
        $('#form').hide();
        $('#usageRemark').val($('#remark').val());
        $('#form2').show();
        $('#formSubmitBtn').hide();
        $('#formSubmitBtn2').show();
    });

    $('#formSubmitBtn2').on('click', function () {
        $('#form2').hide();
        $('#form').show();
        $('#formSubmitBtn2').hide();
        $('#formSubmitBtn').show();
        $('#remark').val($('#usageRemark').val());

        var usageType = $('input[name=usageType]:checked', '#txForm').val();
        $('#usageSelectDesc').text(usageTypeArr[usageType]);
    });

    var txStr = localStorage.getItem('tx');
    if (txStr) {
        var tx = JSON.parse(txStr);

        if (tx.type == GetQueryString('type')) {
            $('#amount').val(tx.amount);
            $('#beginDate').val(tx.beginDate);
            $('#endDate').val(tx.endDate);
            $('#rate').val(tx.rate + '%');
            $('#rate').attr('data-values', tx.rate);
            $('#otherName').val(tx.otherName);
            $('#remark').val(tx.remark);
            $('#x1' + tx.usageType).attr('checked', 'checked');
            $('#usageSelectDesc').text(usageTypeArr[tx.usageType]);
            // $('#usageRemark').val(tx.usageRemark);
            $('#type').val(tx.type);
            if (tx.type == 2) {
                $('#otherNameLabel').text('对方姓名 (借款人)');
            } else {
                $('#otherNameLabel').text('对方姓名 (出借人)');
            }
            rebuildRateSummary();
        }

        localStorage.removeItem('tx');
    }
});
