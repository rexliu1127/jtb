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
        var headerTr = '<tr><th>出借人</th><th>借款金额</th><th>时间</th><th>状态</th></tr>';

        var tableDiv = $('#css_table');
        var emptyDiv = $('#emptyDiv');
        tableDiv.empty();
        tableDiv.append(headerTr);

        if ($('#searchInput').val()) {
            $searchBar.addClass('weui-search-bar_focusing');
        }

        var loading = weui.loading('加载中...');
        $.get(baseURL + "wx/borrowed_transaction/list?lenderName="+$('#searchInput').val(), function(r){
            if (r.page.length === 0) {
                emptyDiv.show();
            } else {
                emptyDiv.hide();
                r.page.forEach(function(e) {
                    var tr = '<tr><td><div class="pic_pic"><span><img src="' + (e.lenderHeadImg ? e.lenderHeadImg : '../images/icon_guest.png')
                        + '"/></span>' +
                        e.lenderName + '</div></td><td>' + e.amount + '</td><td>' +
                        e.beginDate.substring(0, 10) + '</td><td>' +
                        e.statusLabel + '</td></tr>';
                    tableDiv.append(tr);
                });
            }
            loading.hide();
        });
    }

    reloadData();
});