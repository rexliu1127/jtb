
function openRepayment(txId) {
    location.href = baseURL + 'wx/repayment_repay_plan/' + txId;
}

function selectEvent(event) {
    var txId = event.target.value;
    // $('#selectedAmount').text($('#amount_' + txId).text());
    // $('#selectedTxId').val(txId);
}

$(function(){

    var $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    function hideSearchResult(){
        $searchResult.hide();
        $searchInput.val('');
    }
    function cancelSearch(){
        hideSearchResult();
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }

    $searchText.on('click', function(){
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });
    $searchInput
        .on('blur', function () {
            reloadData();
        })
        .on('input', function(){
            if(this.value.length) {
                $searchResult.show();
            } else {
                $searchResult.hide();
            }
        })
    ;
    $searchClear.on('click', function(){
        hideSearchResult();
        $searchInput.focus();
    });
    $searchCancel.on('click', function(){
        cancelSearch();
        $searchInput.blur();
        reloadData();
    });

    $('#searchForm').on('submit', function () {
        $searchInput.blur();
        return false;
    });

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
        var tableDiv = $('#tx_list');
        tableDiv.empty();

        $.showLoading('加载中...');

        $.get(baseURL + "rcpt/repayment/repay_list?userName="+$('#searchInput').val(), function(r){
            if (r.page.length === 0) {
            } else {
                r.page.forEach(function(e) {
                    var tr = '<li class="display"><div class="borrowersPhoto"><img' +
                        ' src="../img/accoubt/photo.svg"></div><div class="borrowers flex1">' +
                        '<h3>' + e.borrowerName + ' <i class="radius7PX">借款人</i></h3><span>还款日期：' + e.endDate + '</span></div>' +
                        '<div class="state"><h3>' + (e.amount + e.interest).toFixed(2) +
                        '元</h3><span>' + e.statusLabel + '</span></div><div class="more">' +
                        '<i class="linecons-GT"></i></div></li>';

                    tableDiv.append(tr);
                });
            }
            $.hideLoading();
        });
    }

    reloadData();
});