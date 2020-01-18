function reloadData() {
    var tableDiv = $('#css_table');
    var emptyDiv = $('#emptyDiv');

    var loading = weui.loading('加载中...');
    $.get(baseURL + "wx/borrowed_transaction/list_for_credit_report/" + creditUserId + (txId >= 0 ? ('?txId=' + txId) : ''), function(r){
        if (r.page.length === 0) {
            emptyDiv.show();
        } else {
            emptyDiv.hide();
            r.page.forEach(function(e) {
                var tr = '<div class="css_tr"><div class="css_td"></div><div class="css_td">*</div><div class="css_td">'
                    + e.amount + '</div><div class="css_td">' + e.beginDate.substring(0, 10) + '</div>' +
                    '<div class="css_td">' + (e.endDate ? e.endDate.substring(0, 10) : '') + '</div>' +
                    '<div class="css_td">' + (e.repaymentDate ? e.repaymentDate.substring(0, 10) : '') + '</div>' +
                    '<div class="css_td">' + e.statusLabel + '</div></div>';
                tableDiv.append(tr);
            });
        }
        loading.hide();
    });
}

$(function () {
    reloadData();
});