$(function(){
    weui.tab('#tab',{
        defaultIndex: 0,
        onChange: function(index){
        }
    });
});


$(document).ready(function () {
    //your code here
    var applyBtn = $('#applyTransaction');
    if (applyBtn.attr('href') === 'javascript:;') {
        applyBtn.on('click', function () {
            weui.actionSheet([
                {
                    label: '我是借款人',
                    onClick: function () {
                        // var loading = weui.loading('提交中...');
                        setTimeout("go('wx/apply_transaction_page?type=1')",300);
                        // window.location('apply_transaction_page?type=1');
                    }
                }, {
                    label: '我是出借人',
                    onClick: function () {
                        setTimeout("go('wx/apply_transaction_page?type=2')",300);
                        // location.href = 'apply_transaction_page?type=2';
                        // window.location('apply_transaction_page?type=2');
                    }
                }
            ], [
                {
                    label: '取消',
                    onClick: function () {
                    }
                }
            ], {
                className: "custom-classname",
                onClick: function () {
                }
            });
        });
    }

});
