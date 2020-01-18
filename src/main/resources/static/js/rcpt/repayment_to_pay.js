
function openRepayment(txId) {
    location.href = baseURL + 'rcpt/repayment_to_pay_plan/' + txId + pageExt;
}


var lastSearchValue = '';
var searchValue = '';
var noMore = false;
var tableDiv = $('#tx_list');
var emptyDiv = $('.noData');
var loadingMore = false;

$(document.body).infinite();

$(document.body).infinite().on("infinite", function() {
    if (loadingMore) return;

    loadingMore = true;
    if (!noMore) {
        loadingMore = true;
        T.pageNum = T.pageNum + 1;
        loadData(searchValue, T.pageNum, T.pageSize);
    }
    loadingMore = false;
});

var loadData = function(searchValue, page, limit) {
    $.showLoading('加载中...');
    $.get(baseURL + "rcpt/repayment/to_pay_list?userName="+searchValue + "&page=" + page + "&limit=" + limit, function(r){
        $.hideLoading();
        if (r.page.length === 0) {
            noMore = true;

            if (page <= 1) {
                emptyDiv.show();
            }
        } else {
            emptyDiv.hide();
            r.page.forEach(function(e) {
                var tr = '<li class="display" onclick="openRepayment(' + e.txId + ')"><div' +
                    ' class="borrowersPhoto"><img' +
                    ' src="../img/accoubt/photo.svg"></div><div class="borrowers flex1">' +
                    '<h3>' + e.lenderName + ' <i class="radius7PX">出借人</i></h3><span>还款日期：' + e.endDate + '</span></div>' +
                    '<div class="state"><h3>' + (e.outstandingAmount + e.outstandingInterest).toFixed(2) +
                    '元</h3>';
                if (e.repaymentStatus === 1) {
                    tr = tr + '<em class="red">' + e.repaymentStatusLabel + '</em>';
                }
                if (e.extensionStatus === 1) {
                    tr = tr + '<em class="red">' + e.extensionStatusLabel + '</em>';
                }

                if (e.repaymentStatus !== 1 && e.extensionStatus !== 1) {
                    tr = tr + '<span>' + e.txStatusLabel + '</span>';
                }
                tr = tr +
                    '</div><div class="more">' +
                    '<i class="linecons-GT"></i></div></li>';
                tableDiv.append(tr);
            });
        }
    });
};

$(function(){

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

        searchValue = $searchInput.val();
        if (lastSearchValue && searchValue === lastSearchValue) {
            return;
        }

        lastSearchValue = searchValue;
        tableDiv.empty();

        T.pageSize = 10;
        T.pageNum = 1;

        loadData(searchValue, T.pageNum, T.pageSize);
    }

    reloadData();
});