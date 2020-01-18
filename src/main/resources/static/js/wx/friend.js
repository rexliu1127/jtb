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
        var tableDiv = $('#css_table');
        var emptyDiv = $('#emptyDiv');
        tableDiv.empty();

        if ($('#searchInput').val()) {
            $searchBar.addClass('weui-search-bar_focusing');
        }

        var loading = weui.loading('加载中...');
        $.get(baseURL + "wx/member/friend/list?userName="+$('#searchInput').val() + "&userId=" + $('#userId').val(), function(r){
            if (r.page.length === 0) {
                emptyDiv.show();
            } else {
                emptyDiv.hide();
                r.page.forEach(function(e) {
                    var tr = '<div class="css_tr"><div class="css_td img"><img' +
                        ' src="' + e.headImgUrl + '"/></div><div class="css_td" onclick="location.href=\'credit/'
                    + e.userId + '\';">'
                    + (e.name ? e.name : e.userName + '(未实名认证)') + '</div></div>';
                    tableDiv.append(tr);
                });
            }
            loading.hide();
        });
    }

    reloadData();
});