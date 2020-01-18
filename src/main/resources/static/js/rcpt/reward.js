
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
    $.get(baseURL + "rcpt/member/reward_list?page=" + page + "&limit=" + limit, function(r){
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
                var tr = '<tr><td>' + (e.level == 1 ? '二级奖励' : '三级奖励') + '</td>'
                    + '<td>' + e.inviteeName + '**</td>'
                    + '<td>' + e.createDate + '</td>'
                    + '<td>' + e.amount + '元</td></tr>';
                tableDiv.append(tr);
            });
        }
    });
};

$(function(){
    loadData(T.pageNum, T.pageSize);
});
