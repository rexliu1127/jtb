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

    function reloadData() {
        var tableDiv1 = $('#css_table1');
        var emptyDiv1 = $('#emptyDiv1');
        tableDiv1.empty();


        var tableDiv2 = $('#css_table2');
        var emptyDiv2 = $('#emptyDiv2');
        tableDiv2.empty();
        tableDiv2.append('<div class="css_tr head"><div class="css_td">还款日期</div><div class="css_td">出借人姓名</div><div' +
            ' class="css_td">还款金额</div><div class="css_td">状态</div></div>');

        if ($('#searchInput').val()) {
            $searchBar.addClass('weui-search-bar_focusing');
        }

        var loading = weui.loading('加载中...');
        $.get(baseURL + "wx/repayment_to_pay_plan/data_list?userName="+$('#searchInput').val(), function(r){
            if (r.lendingList.length === 0) {
                emptyDiv1.show();
            } else {
                emptyDiv1.hide();
                r.lendingList.forEach(function(e) {
                    var tr = '<a class="weui-cell weui-cell_access" href="repayment/to_pay_view/' + e.txId
                        + '"><div class="weui-cell__hd"></div><div class="weui-cell__bd weui-cell_primary entry-header"><ul><li><span>'
                        + e.lenderName + '</span></li><li></li><li><span class="red">'
                        + (e.amount + e.interest).toFixed(2) + '元</span></li></ul></div></a><a class="weui-cell' +
                        ' weui-cell_access entry-body" href="repayment/to_pay_view/' + e.txId + '"><ul class="clearfix">'
                        + '<li>还款日期<br/><span>' + e.endDate + '</span></li><li>年利率<br/><span class="red">'
                        + e.rate + '%</span></li><li> <button class="weui-btn weui-btn_mini weui-btn_primary">查看</button></li></ul></a>';
                    tableDiv1.append(tr);
                });
            }

            if (r.repaymentList.length === 0) {
                emptyDiv2.show();
            } else {
                emptyDiv2.hide();
                r.repaymentList.forEach(function(e) {
                    var tr = '<div class="css_tr"><div class="css_td">'
                        + (e.actualEndDate ? e.actualEndDate : e.endDate)
                        + '</div><div class="css_td">' + e.lenderName
                        + '</div><div class="css_td">' + (e.amount + e.interest).toFixed(2)
                        + '</div><div class="css_td">' + e.repaymentStatusLabel + '</div></div>';
                    tableDiv2.append(tr);
                });
            }
            loading.hide();
        });
    }

    reloadData();
});