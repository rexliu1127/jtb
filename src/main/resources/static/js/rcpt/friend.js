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
        var tableDiv = $('#list');
        var emptyDiv = $('.noData');
        tableDiv.empty();

        $.showLoading('加载中...');
        $.get(baseURL + "rcpt/member/friend/list?userName="+searchValue + "&userId=" + $('#userId').val(), function(r){
            if (r.page.length === 0) {
                emptyDiv.show();
            } else {
                emptyDiv.hide();
                r.page.forEach(function(e) {
                    var tr = '<li class="display" onclick="location.href=\'credit/' + e.userId + '.html\'' +
                            '"><div' +
                        ' class="friendImg"><img' +
                        ' src="' + (e.headImgUrl ? e.headImgUrl : '../../img/accoubt/photo.svg') + '"/></div>' +
                        '<div class="flex1"><h3>' + (e.name ? e.name : e.userName + '(未实名认证)') + '</h3></div' +
                        '><div><i class="linecons-GT fr"></i></div></li>';
                    tableDiv.append(tr);
                });
            }
            $.hideLoading();
        });
    }

    reloadData();
});
