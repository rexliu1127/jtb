function captchaResponse(resp) {
    vm.captcha = resp;
}

var vm = new Vue({
    el:'#rrapp',
    data:{
        mobile: '',
        password: '',
        rePassword: '',
        code: '',
        name: '',
        email: '',
        captcha: '',
        countDown: 0,
        error: false,
        errorMsg: '',
        src: 'captcha.jpg',
        mobileExp: /^1(3|4|5|6|7|8|9)\d{9}$/,
        success: false,
        luosimaoSiteKey: T.luosimao.siteKey
    },
    beforeCreate: function(){
        if(self != top){
            top.location.href = self.location.href;
        }
    },
    methods: {
        refreshCode: function(){
            this.src = "captcha.jpg?t=" + $.now();
        },
        getCode: function(){
            if (vm.countDown > 0) {
                return;
            }

            vm.error = false;

            if (!vm.mobile || !vm.mobileExp.test(vm.mobile)) {
                vm.error = true;
                vm.errorMsg = '请输入正确的手机号码';
                return;
            }

            if (isLuosimaoCaptchaEnabled() && !vm.captcha) {
                vm.error = true;
                vm.errorMsg = '请点击进行人机识别验证';
                return;
            }

            $.get(baseURL + 'sms_code?type=login&mobile=' + vm.mobile + "&captcha=" + vm.captcha, function(r){
                if(r.code == 0){
                    vm.countDown = 60;
                    setTimeout(function() {
                        vm.doCountDown();
                    }, 1000);
                }else{
                    $.toptip(r.msg, 'error');
                }
            });
        },
        doCountDown: function() {
            if (vm.countDown > 0) {
                vm.countDown = vm.countDown -1;
                if (vm.countDown > 0) {
                    setTimeout(function() {
                        vm.doCountDown();
                    }, 1000);
                }
            }
        },
        register: function () {
            vm.error = false;

            if (!vm.mobile || !vm.mobileExp.test(vm.mobile)) {
                vm.error = true;
                vm.errorMsg = '请输入正确的手机号码';
                return;
            }

            if (!vm.code) {
                vm.error = true;
                vm.errorMsg = '请输入手机验证码';
                return;
            }

            if (!vm.name) {
                vm.error = true;
                vm.errorMsg = '请输入公司名称';
                return;
            }

            if (!vm.email) {
                vm.error = true;
                vm.errorMsg = '请输入正确的Email地址';
                return;
            }

            if (!vm.password) {
                vm.error = true;
                vm.errorMsg = '请输入登录密码';
                return;
            }

            if (!vm.rePassword) {
                vm.error = true;
                vm.errorMsg = '请再次输入登录密码';
                return;
            }

            if (vm.password.length < 8) {
                vm.error = true;
                vm.errorMsg = '请至少输入8位登录密码';
                return;
            }

            if (vm.password !== vm.rePassword) {
                vm.error = true;
                vm.errorMsg = '两次输入登录密码不一样';
                return;
            }

            if (isLuosimaoCaptchaEnabled() && !vm.captcha) {
                vm.error = true;
                vm.errorMsg = '请点击进行人机识别验证';
                return;
            }

            $.ajax({
                type: "POST",
                url: baseURL + "sys/register",
                data: {mobile: vm.mobile, password: vm.password, name: vm.name, email: vm.email, captcha: vm.captcha, code: vm.code},
                dataType: "json",
                success: function(r){
                    if(r.code == 0){//登录成功
                        vm.success = true;
                    }else{
                        vm.error = true;
                        vm.errorMsg = r.msg;
                    }
                }
            });
        }
    }
});