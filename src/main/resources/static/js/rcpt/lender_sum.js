
$(function(){

    var lastSearchValue = '';
    var $searchInput = $('#searchValue');

    $searchInput
        .on('blur', function () {
            reloadData();
        })
        .on('keyup', function(e) {
            if (e.keyCode == 13) {
                reloadData();
            }
        })
    ;

    function getStatusDisplayValue(txStatus) {
        switch(txStatus) {
            case 'CONFIRMED':
                return '还款中';
                break;
            case 'DELAYED':
                return '已逾期';
                break;
            case 'COMPLETED':
                return '已还清';
                break;
            default:
                return '未知';
        }
    }

    function reloadData() {
        var searchValue = $searchInput.val();
        if (lastSearchValue && searchValue === lastSearchValue) {
            return;
        }

        lastSearchValue = searchValue;
        var emptyDiv = $('.noData');
        var tableDiv = $('#tx_list');
        tableDiv.empty();

        $.showLoading('加载中...');
        $.get(baseURL + "rcpt/lended_transaction/list?lenderName="+searchValue, function(r){
            $.hideLoading();
            if (r.page.length === 0) {
                emptyDiv.show();
            } else {
                emptyDiv.hide();
                r.page.forEach(function(e) {
                    var tr = '<li class="display" onclick="location.href=\'transaction/'+ e.txId + '.html\'"><div' +
                        ' class="borrowersPhoto"><img' +
                        ' src="' + (e.borrowerHeadImg ? e.borrowerHeadImg : '../img/accoubt/photo.svg') + '"></div><div' +
                        ' class="borrowers flex1">' +
                        '<h3>' + e.borrowerName + ' <i class="radius7PX">借款人</i></h3><span>开始日期：' + e.beginDate.substring(0, 10)
                        + '</span><br/><span>还款日期：' + e.endDate.substring(0, 10) + '</span></div>' +
                        '<div class="state"><h3>' + (e.amount).toFixed(2) +
                        '元</h3>';
                    tr = tr + '<span>' + e.statusLabel + '</span>';
                    tr = tr +
                        '</div><div class="more">' +
                        '<i class="linecons-GT"></i></div></li>';
                    tableDiv.append(tr);
                });
            }
        });
    }

    reloadData();
});