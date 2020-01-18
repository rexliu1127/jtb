
var noMore = false;
var tableDiv = $('#dataTable');
var emptyDiv = $('.noData');
var loadingMore = false;
var tbody = tableDiv.children('tbody');

$(document.body).infinite();

$(document.body).infinite().on("infinite", function() {
    if (loadingMore) return;

    loadingMore = true;
    if (!noMore) {
        loadingMore = true;
        T.pageNum = T.pageNum + 1;
        loadData(T.pageNum, T.pageSize);
    }
    loadingMore = false;
});

var loadData = function(page, limit) {

    $.showLoading('加载中...');
    $.get(baseURL + "rcpt/withdrawal_list?page=" + page + "&limit=" + limit, function(r){
        $.hideLoading();
        if (r.page.length === 0) {
            noMore = true;

            if (page <= 1) {
                emptyDiv.show();
                tableDiv.hide();
            }
        } else {
            emptyDiv.hide();
            tableDiv.show();
            r.page.forEach(function(e) {
                var tr = '<tr><td>' + e.createTime.substring(0, 10) + '</td>'
                    + '<td>' + e.amount + '</td>'
                    + '<td>' + e.feeAmount + '</td>'
                    + '<td>' + e.status.displayName + '</td></tr>';
                tableDiv.append(tr);
            });
        }
    });
};

$(function(){
    loadData(T.pageNum, T.pageSize);
});
