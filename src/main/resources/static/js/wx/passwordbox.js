function PassWordInit(work){
    var check_pass_word='';
    var passwords = $('#password').get(0);
    $(function(){

        var div ='\
        <div id="key">\
            <ul id="keyboard" class="mui-clearfix">\
                <li class="symbol"><span class="off">1</span><em></em></li>\
                <li class="symbol"><span class="off">2</span><em>ABC</em></li>\
                <li class="symbol"><span class="off">3</span><em>DEF</em></li>\
                <li class="symbol"><span class="off">4</span><em>GHI</em></li>\
                <li class="symbol"><span class="off">5</span><em>JKL</em></li>\
                <li class="symbol"><span class="off">6</span><em>MNO</em></li>\
                <li class="symbol"><span class="off">7</span><em>PQRS</em></li>\
                <li class="symbol"><span class="off">8</span><em>TUV</em></li>\
                <li class="symbol"><span class="off">9</span><em>WXYZ</em></li>\
                <li class="cancle">取消</li>\
                <li class="symbol"><span class="off">0</span></li>\
                <li class="delete lastitem">删除</li>\
            </ul>\
        </div>\
		';

        var character,index=0;
        $("input.pass").attr("disabled",true);
        $("#password").attr("disabled",true);
        $("#keyboardDIV").append(div);

        $('#keyboard li').click(function(){
            if ($(this).hasClass('delete')) {
                $(passwords.elements[--index%6]).val('');
                if($(passwords.elements[0]).val()==''){
                    index = 0;
                }
                return false;
            }
            if ($(this).hasClass('cancle')){
                $("#keyboardDIV").hide()
                $("#key").remove()
                $("#password_mask").hide()
                for (var i = 0; i < passwords.elements.length; i++) {
                    $(passwords.elements[i]).val('');
                }
                return false;
            }
            if ($(this).hasClass('symbol') || $(this).hasClass('tab')){
                character = $(this).find('span').text();
                $(passwords.elements[index++%6]).val(character);
                if($(passwords.elements[5]).val()!=''){
                    index = 0;
                }
                if($(passwords.elements[5]).val()!='') {
                    var temp_rePass_word = '';
                    for (var i = 0; i < passwords.elements.length; i++) {
                        temp_rePass_word += $(passwords.elements[i]).val();
                    }
                    check_pass_word = temp_rePass_word;
                    $("#keyboardDIV").hide();
                    $("#key").remove()
                    $("#password_mask").hide()
                    for (var i = 0; i < passwords.elements.length; i++) {
                        $(passwords.elements[i]).val('');
                    }

                    var action = 'modify';
                    work(check_pass_word);
                }
            }
            return false;
        });
    });
    (function () {
        function tabForward(e) {
            e = e || window.event;
            var target = e.target || e.srcElement;
            if (target.value.length === target.maxLength) {
                var form = target.form;
                for (var i = 0, len = form.elements.length-1; i < len; i++) {
                    if (form.elements[i] === target) {
                        if (form.elements[i++]) {
                            form.elements[i++].focus();
                        }
                        break;
                    }
                }
            }
        }
        var form = document.getElementById("password");
        form.addEventListener("keyup", tabForward, false);
        var set_text='\<span>请输入</span>\<span style="color:red; font-weight:bold;">交易密码</span>\<span></span>\
        ';
        $("#set_text").html(set_text);
    })();
}
