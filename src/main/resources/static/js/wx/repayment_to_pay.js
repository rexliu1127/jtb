function openRepayment(txId) {
    location.href = baseURL + 'wx/repayment/create_page/' + txId;
}

function selectEvent(event) {
    var txId = event.target.value;
    $('#selectedAmount').text($('#amount_' + txId).text());
    $('#selectedTxId').val(txId);
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

    $('#pay').on('click', function () {
        var txId = $('#selectedTxId').val();
        if (!txId) {
            weui.topTips('请选择一条借条!');
            return;
        }

        openRepayment(txId);

    });

    function reloadData() {
        var tableDiv = $('#tx_list');
        var emptyDiv = $('#emptyDiv');
        tableDiv.empty();

        if ($('#searchInput').val()) {
            $searchBar.addClass('weui-search-bar_focusing');
        }

        var loading = weui.loading('加载中...');
        $.get(baseURL + "wx/repayment/to_pay_list?userName="+$('#searchInput').val(), function(r){
            if (r.page.length === 0) {
                emptyDiv.show();
            } else {
                emptyDiv.hide();
                r.page.forEach(function(e) {
                    var tr = '<li><div class="meney_con"><div class="meney_check"><input type="radio"' +
                        ' name="selectTx" class="select_tx" value="' + e.txId + '" onclick="selectEvent(event);"/></div>'
                        + '<a href="repayment_to_pay_plan/' + e.txId
                        +'"><div class="meney_l"><h1 id="amount_' + e.txId
                        + '">' + (e.amount + e.interest).toFixed(2)
                        + '元</h1><p>' + e.lenderName + ' - ' + e.usageTypeLabel + '</p></div>'
                        + '<div class="meney_r"><p>';
                    tr = tr +'' + (e.daysToPay >= 0 ? '剩余' : '逾期') + Math.abs(e.daysToPay) + '天</p>';

                    if (e.repaymentStatus === 1) {
                        tr = tr + '<span class="red-status">' + e.repaymentStatusLabel + '</span>';
                    }
                    if (e.extensionStatus === 1) {
                        tr = tr + '<span class="red-status">' + e.extensionStatusLabel + '</span>';
                    }
                    tr = tr + '</div></a></div></li>';
                    tableDiv.append(tr);
                });
            }
            loading.hide();
        });
    }

    reloadData();
});