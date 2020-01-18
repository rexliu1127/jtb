var curLevel = 1;

function changeMenu(level) {
    curLevel = level;

    $('.choose').removeClass('choose');
    $('.card0' + level).addClass('choose');

    reloadData();
};

function reloadData() {
    var emptyDiv = $('.noData');
    var tableDiv = $('#dataTable');
    var tbody = tableDiv.children('tbody');
    tbody.empty();

    $.showLoading('加载中...');
    $.get(baseURL + "rcpt/member/team_list?level="+curLevel, function(r){
        $.hideLoading();
                if (r.list.length === 0) {
                    emptyDiv.show();
                    tableDiv.hide();
                } else {
                    emptyDiv.hide();
                    tableDiv.show();

                    r.list.forEach(function(e) {
                        var tr = '<td>' + (e.name ? e.name : '') + '**</td>'
                                + '<td>' + e.createDate + '</td>'
                                + '<td>' + e.txCount + '</td>';
                        tbody.append(tr);
                    });
                }
            });
};

$(document).ready(function () {
    reloadData();
});


