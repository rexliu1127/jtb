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
    $('#submit').on('click', function () {
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
        var rate = $('#rate').val();
        if (!rate) {
            rate = 0;
        }
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('#usageType').val();
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

        

        var tx = {amount: amount, beginDate: beginDate, endDate: endDate,
            rate: rate, otherName: otherName, remark: remark,
            usageType: usageType, usageRemark: usageRemark, type: type};

        confirmPop('提醒', '对方确认后借条立即生效? 要发起借条吗?', '借条是为了已完成借贷行为补一张借条作为电子凭证，请确保你们线下交易完成.',
            [{
                label: '不发起',
                onClick: function () {
                }
            }, {
                label: '发起',
                onClick: function () {
                    if (!bindedUser) {
                        localStorage.setItem('tx', JSON.stringify(tx));
                        location.href = baseURL + 'rcpt/bind_bank.html?returnUrl=rcpt/apply_transaction.html?type=' + type;
                    } else if (!userSigned) {
                        localStorage.setItem('tx', JSON.stringify(tx));
                        location.href = baseURL + 'rcpt/member/sign.html?returnUrl=rcpt/apply_transaction.html?type=' + type;
                    } else {
                        PasswordInit(applyTransaction);
                    }
                }
            }
            ]);
    });

    function applyTransaction(password) {
        $.showLoading('提交中...');
        var amount = $('#amount').val();
        var beginDate = $('#beginDate').val();
        var endDate = $('#endDate').val();
        var rate = $('#rate').val();
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('#usageType').val();
        var usageRemark = '';
        var type = $('#type').val();

        var tx = {amount: amount, beginDate: beginDate, endDate: endDate,
            rate: rate, otherName: otherName, remark: remark,
            usageType: usageType, usageRemark: usageRemark, type: type, password: password};
        $.ajax({
            type: "POST",
            url: baseURL + "rcpt/apply_transaction",
            dataType: 'json',
            data: tx,
            success: function(r){
                $.hideLoading();
                if(r.code == 0){
                    $.toast('提交成功');

                    if (r.tx.status.value === 5) {
                        location.href = baseURL + 'rcpt/transaction/' + r.tx.txId + '.html';
                        return;
                    }
                    location.href = baseURL + "rcpt/confirm_transaction/" + r.tx.txUuid + '.html';
                }else{
                    $.toptip(r.msg, 'error');
                }
            }
        });
    }

    beginDateEle.calendar({
        value: [beginDateEle.val()],
        dateFormat: 'yyyy-mm-dd',
        onClose: function () {
            // rebuildRateSummary();
        }
    });

    endDateEle.calendar({
        value: [endDateEle.val()],
        dateFormat: 'yyyy-mm-dd',
        onClose: function () {
            // rebuildRateSummary();
        }
    });

    $('#forgetBtn').on('click', function () {
        var amount = $('#amount').val();
        var beginDate = $('#beginDate').val();
        var endDate = $('#endDate').val();
        var rate = $('#rate').val();
        var otherName = $('#otherName').val();
        var remark = $('#remark').val();
        var usageType = $('#usageType').val();
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
            $('#rate').val(tx.rate);
            $('#otherName').val(tx.otherName);
            $('#remark').val(tx.remark);
            $('#usageType').val(tx.usageType);
            $('#type').val(tx.type);
            if (tx.type == 2) {
                $('#otherNameLabel').text('对方姓名 (借款人)');
            } else {
                $('#otherNameLabel').text('对方姓名 (出借人)');
            }
        }

        localStorage.removeItem('tx');
    }

});
