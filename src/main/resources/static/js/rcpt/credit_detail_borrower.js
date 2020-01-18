function reloadData() {
    var tableDiv = $('#dataTable > tbody');
    var emptyDiv = $('.noData');

    $.showLoading('加载中...');
    $.get(baseURL + "rcpt/borrowed_transaction/list_for_credit_report/" + creditUserId + (txId >= 0 ? ('?txId=' + txId) : ''), function(r){
        if (r.page.length === 0) {
            emptyDiv.show();
        } else {
            emptyDiv.hide();
            r.page.forEach(function(e) {
                var tr = '<tr>' +
                    '<td>*</td>' +
                    '<td><p class="red">' + e.amount + '</p></td>' +
                    '<td>' +
                    '<p><i class="greenBG white">起</i>' + e.beginDate.substring(0, 10) + '</p>' +
                    '<p><i class="redBG white">止</i>' + e.endDate.substring(0, 10) + '</p>' +
                    '</td>' +
                    '<td>' + (e.repaymentDate ? e.repaymentDate.substring(0, 10) : '') + '</td>' +
                    '<td><i class="greenBG white">' + e.statusLabel + '</i></td>' +
                    '</tr>';
                tableDiv.append(tr);
            });
        }
        $.hideLoading();
    });
}

$(function () {
    reloadData();
});