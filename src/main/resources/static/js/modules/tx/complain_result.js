$(function () {
    vm.reload();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        histories: {}
	},
	methods: {
		reload: function (event) {
            $.get(baseURL + "tx/txcomplain/history/"+T.p('complainId'), function(r){
                vm.histories = r.list;
            });
		}
	}
});
