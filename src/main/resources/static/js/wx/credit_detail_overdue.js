function reloadData() {
    var tableDiv = $('#css_table');
    var emptyDiv = $('#emptyDiv');

    var loading = weui.loading('加载中...');
    $.get(baseURL + "wx/borrowed_transaction/list_for_credit_report/" + creditUserId + "?overdue=true" + (txId >0 ? ('&txId=' + txId) : ''), function(r){
            if (r.page.length === 0) {
            emptyDiv.show();
        } else {
            emptyDiv.hide();
            r.page.forEach(function(e) {
                var tr = '<div class="css_tr"><div class="css_td"></div><div class="css_td">*</div><div class="css_td">'
                    + e.amount + '</div><div class="css_td">' + e.beginDate + '</div>' +
                    '<div class="css_td">' + e.endDate + '</div>' +
                    '<div class="css_td">' + e.overdueDays + '</div>' +
                    '<div class="css_td">' + (e.overdueEndDate ? '已还清' : '逾期中') + '</div></div>';
                tableDiv.append(tr);
            });
        }
        loading.hide();
    });
}

$(function () {
    reloadData();
});