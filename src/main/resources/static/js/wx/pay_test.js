$(function () {


    $('#testBtn2').on('click', function () {
        alert(navigator.userAgent);


        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        alert('是否是Android：'+isAndroid);
        alert('是否是iOS：'+isiOS);
    });

    $('#testBtn').on('click', function () {
        $.showLoading('加载中...');
        $.ajax({
            type: "POST",
            url: baseURL + "/wx/pay_test",
            dataType: 'json',
            data: {},
            success: function(r){
                if(r.code == 0){
                    $.hideLoading();
                    var onBridgeReady = function (){
                        WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', JSON.parse(r.payinfo),
                            function(res) {
                                switch(res.err_msg) {
                                    case 'get_brand_wcpay_request:cancel':
                                        $.toptip('取消支付', 'error');
                                        break;
                                    case 'get_brand_wcpay_request:fail':
                                        $.toptip('支付失败！（'+res.err_desc+'）', 'error');
                                        $.toptip(JSON.stringify(res), 'error');
                                        break;
                                    case 'get_brand_wcpay_request:ok':
                                        $.toptip('支付成功！', 'error');
                                        break;
                                    default:
                                        $.toptip(JSON.stringify(res), 'error');
                                        break;
                                }
                            }
                        );
                    };
                    if (typeof WeixinJSBridge == "undefined"){
                        if( document.addEventListener ){
                            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                        }else if (document.attachEvent){
                            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                        }
                    }else{
                        alert(1);
                        onBridgeReady();
                    }
                }else{
                    $.hideLoading();
                    $.toptip(r.msg, 'error');
                }
            }
        });
    });

});
